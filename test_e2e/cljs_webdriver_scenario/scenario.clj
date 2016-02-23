(ns cljs-webdriver-scenario.scenario
  (:require [cljs.test :refer-macros [deftest async]]
            [clojure.string :as string]))

(defn error-handler-form [atom-name]
  `(.then
     identity
     (fn [err#] (swap! ~atom-name conj err#))))

(defn string->screenshot [scenario-name step-number s]
  (let [filename (str step-number "_" (string/replace s #"\s" "_") ".png")]
  `(~'cljs-webdriver-scenario.operations/screenshot ~scenario-name ~filename)))

(defn parse-step [scenario-name step-number step]
  (cond
    (string? (first step)) (map #(string->screenshot scenario-name step-number %) step)
    (list? (first step)) step
    :else (throw (Exception. "Wrong syntax of defscenario."))))

(defn parse-actions [scenario-name actions]
  (let [steps (partition-by list? actions)
        title-steps (take-nth 2 steps)
        command-steps (take-nth 2 (drop 1 steps))
        steps-commands-first (-> (interleave command-steps title-steps)
                                 (conj ["initial"]))]
    (mapcat #(parse-step scenario-name %1 %2) (map #(/ % 2) (range)) steps-commands-first)))

(defmacro defscenario [scenario-name & actions]
  (let [error-log (gensym "error-log")]

    `(~'cljs.test/deftest ~scenario-name
       (let [~error-log (clojure.core/atom [])
             client# (cljs-webdriver-scenario.scenario/client)]
         (~'cljs.test/async ~'done
          (-> client#
              (.init)
              (.then identity (fn [err#] (swap! ~error-log conj err#)))

              ~@(interpose (error-handler-form error-log) (parse-actions (name scenario-name) actions))

              (.end)
              (.then identity (fn [err#] (swap! ~error-log conj err#)))
              (.finally (fn []
                          (when (~'seq (deref ~error-log))
                            (cljs.test/is (empty? (deref ~error-log)) "There were errors when running scenario."))
                          (~'done)))))))))