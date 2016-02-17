(ns e2e-taxi-example.e2e.runner
  (:require [cljs.test :refer-macros [run-tests]]
            [cljs.nodejs :as node]
            [cljs-webdriver-scenario.scenario :as scenario]
            [e2e-taxi-example.e2e.counter-test]))

(def electron-options
  #js {:host "localhost"
       :port 9515
       :desiredCapabilities #js {:browserName "chrome"
                                 :chromeOptions #js {:binary (str (.cwd js/process) "/electron/electron")
                                 :args #js [(str "app=" (.cwd js/process) "/app")]}}})

(defn -main [& args]
  (run-tests
    'e2e-taxi-example.e2e.counter-test))

(node/enable-util-print!)
(scenario/set-options! electron-options)
(enable-console-print!)
(set! *main-cli-fn* -main)