(ns e2e_taxi_example.core
  (:require [figwheel.client :as fw :include-macros true]
            [reagent.core :as r]))

(fw/watch-and-reload
  :websocket-url   "ws://localhost:3449/figwheel-ws"
  :jsload-callback 'mount-root)

(enable-console-print!)

(defonce state (atom {:message "Hello Reagent world"}))

(defn root-component []
  (let [counter (r/atom 0)]
    (fn []
      [:div
       [:h1 "Counter value is: "
        [:span {:id "counter"} @counter]]
       [:button {:id "counter-btn"
                 :on-click #(swap! counter inc)} "click me to increment!"]])))

(defn mount-root []
  (r/render [root-component]
                  (.getElementById js/document "app")))

(defn init! []
  (mount-root))

(init!)
