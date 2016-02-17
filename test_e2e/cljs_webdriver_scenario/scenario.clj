(ns cljs-webdriver-scenario.scenario
  (:require [cljs.test]))

(defn error-handler-form [atom-name]
  `(.then
     identity
     (fn [err#] (swap! ~atom-name conj err#))))

(defmacro defscenario [scenario-name & actions]
  (let [error-log (gensym "error-log")]

    `(cljs.test/deftest ~scenario-name
       (let [~error-log (clojure.core/atom [])]
         (~'async ~'done
          (-> (cljs-webdriver-scenario.scenario/client)
              (.init)
              (.then identity (fn [err#] (swap! ~error-log conj err#)))

              ~@(interpose (error-handler-form error-log) actions)

              (.end)
              (.then identity (fn [err#] (swap! ~error-log conj err#)))
              (.finally (fn []
                          (when (~'seq (deref ~error-log))
                            (cljs.test/is (empty? (deref ~error-log)) "There were errors when running scenario."))
                          (~'done)))))))))