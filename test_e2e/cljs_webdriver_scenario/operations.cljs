(ns cljs-webdriver-scenario.operations
  (:require [cljs.nodejs :as node]))

(def child-process (node/require "child_process"))
(def linux-command "scrot")
(def osx-command "screencapture")

(defn screenshot [client dirname filename]
  (.call client (fn []
    (.spawnSync child-process
                "sleep"
                (clj->js ["0.15"]))
    (.spawnSync child-process
                "mkdir"
                (clj->js ["-p" (str "screenshots" "/" dirname)]))
    (.spawnSync child-process
                linux-command
                (clj->js [(str "screenshots" "/" dirname "/" filename)])))))