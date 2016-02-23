(ns e2e-scenarios.e2e.runner
  (:require [cljs.test :refer-macros [run-tests]]
            [cljs.nodejs :as node]
            [cljs-webdriver-scenario.scenario :as scenario]
            [e2e-scenarios.e2e.counter-test]))

(def child-process (node/require "child_process"))
(def chromedriver (atom nil))

(def electron-options
  #js {:host "localhost"
       :port 9515
       :desiredCapabilities #js {:browserName "chrome"
                                 :chromeOptions #js {:binary (str (.cwd js/process) "/electron/electron")
                                 :args #js [(str "app=" (.cwd js/process) "/app")
                                            "--allow-sc"]}}})

(defn -main [& args]
  (reset! chromedriver
          (.spawn child-process "chromedriver" #js ["--url-base=wd/hub" "--port=9515"]))

   (-> @chromedriver
       (.-stdout)
       (.on "data" (fn [buffer]
                     (println (.toString buffer))
                     (println "----------")
                     (run-tests
                       'e2e-scenarios.e2e.counter-test)))))

(defmethod cljs.test/report [:cljs.test/default :end-run-tests] [_]
   (.kill @chromedriver "SIGTERM"))

(node/enable-util-print!)
(scenario/set-options! electron-options)
(enable-console-print!)
(set! *main-cli-fn* -main)