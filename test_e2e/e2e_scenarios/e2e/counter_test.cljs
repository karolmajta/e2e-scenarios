(ns e2e-scenarios.e2e.counter-test
  (:require [cljs.test :refer-macros [is]]
            [cljs-webdriver-scenario.scenario :refer-macros [defscenario]]))

(defscenario test-counter-using-scenario

 "button gets clicked three times"
 (.click "#counter-btn")
 (.click "#counter-btn")
 (.click "#counter-btn")

 "button gets clicked once again"
 (.click "#counter-btn")

 "counter value equals 5"
 (.getText "#counter")
 (.then #(is (= "4" %)))
)