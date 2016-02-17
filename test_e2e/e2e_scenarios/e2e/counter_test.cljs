(ns e2e-scenarios.e2e.counter-test
  (:require [cljs.test :refer-macros [deftest async is]]
            [cljs-webdriver-scenario.scenario :refer [client]
                                              :refer-macros [defscenario]]))

;; the two tests shown below are functionally equivalent.



;; This is how we would define test without the `defscenario` macro.
;; please note the amount of boilerplate. We need to handle both
;; "happy path" and "error path" after each action. Error path occures
;; for example if given selector is not found.
;;
;; There is also some boilerplate related to creting the selenium client
;; and dealing with the fact that the test is async by nature.
;;
;; If we were to use deftest, then we would have to repeat this stuff
;; over and over again...

(deftest test-counter-using-deftest
  (let [error-log (atom [])]
    (async done
      (-> (client)
          (.init)
          (.then identity #(swap! error-log conj %))
          (.click "#counter-btn")
          (.then identity #(swap! error-log conj %))
          (.click "#counter-btn")
          (.then identity #(swap! error-log conj %))
          (.getText "#counter")
          (.then identity #(swap! error-log conj %))
          (.then #(is (= "2" %)))
          (.then identity #(swap! error-log conj %))
          (.end)
          (.then identity #(swap! error-log conj %))
          (.finally (fn []
                      (when (seq @error-log)
                        (is (empty? @error-log) "There were errors when running scenario."))
                      (done)))))))


;; defscenario macro handles the boilerplate for you and allows you to
;; express tests as if they were synchronous. You just state the sequence
;; of actions that you want to perform, and the rest is taken care of
;;
;; Also, you only have to deal with the "happy path". If you use wrong selectors
;; or something goes awry, you will be notified by a failure, and appropriate
;; error log will be attached to the error message.

(defscenario test-counter-using-scenario
 (.click "#counter-btn")
 (.click "#counter-btn")
 (.getText "#counter")
 (.then #(is (= "2" %))))