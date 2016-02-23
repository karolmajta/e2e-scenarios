(ns cljs-webdriver-scenario.scenario
  (:require [cljs.nodejs :as node]
            [cljs.test :refer-macros [deftest async is]]
            [cljs-webdriver-scenario.operations]))

(def webdriverio (node/require "webdriverio"))

(def
  ^{:dynamic true
    :private true}
  *options*

  #js {:host "localhost"
       :port 9515
       :desiredCapabilities #js {:browserName "chrome"}})

(defn set-options! [opts]
  (set! *options* opts))

(defn client []
  (.remote webdriverio *options*))