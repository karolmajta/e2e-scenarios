(defproject e2e-scenarios "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.122" :exclusions [org.apache.ant/ant]]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [figwheel "0.4.0"]
                 [reagent "0.5.1"]
                 [ring/ring-core "1.4.0"]]
  :plugins [[lein-cljsbuild "1.1.2"]
            [lein-externs "0.1.5"]
            [lein-shell "0.4.1"]
            [lein-figwheel "0.4.0" :exclusions [org.clojure/core.cache]]]
  :source-paths ["src_tools"]
  :aliases {"install-gulp" ["shell" "npm" "install" "-g" "gulp"]
            "npm-deps" ["shell" "npm" "install"]
            "download-electron" ["shell" "grunt" "download-electron"]
            "init-descjop" ["do" "install-gulp," "npm-deps," "download-electron"]
            "startapp" ["trampoline" "shell" "./electron/electron" "app"]}
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds
              {:main     {:id           "e2e-scenarios-example"
                          :source-paths ["src"]
                          :incremental  true
                          :jar          true
                          :assert       true
                          :compiler     {:output-to      "app/js/cljsbuild-main.js"
                                         :externs        ["app/js/externs.js"
                                                          "node_modules/closurecompiler-externs/path.js"
                                                          "node_modules/closurecompiler-externs/process.js"]
                                         :warnings       true
                                         :elide-asserts  true
                                         :target         :nodejs

                                         ;; no optimize compile (dev)
                                         ;;:optimizations :none
                                         ;; when no optimize uncomment
                                         ;;:output-dir "app/js/out"

                                         ;; simple compile (dev)
                                         :optimizations  :simple

                                         ;; advanced compile (prod)
                                         ;; :optimizations :advanced

                                         ;; :source-map "app/js/test.js.map"
                                         :pretty-print   true
                                         :output-wrapper true}}
               :frontend {:id           "e2e-scenarios-example-reagent"
                          :source-paths ["src_front"]
                          :incremental  true
                          :jar          true
                          :assert       true
                          :compiler     {:output-to      "app/js/front.js"
                                         :externs        ["app/js/externs.js"]
                                         :warnings       true
                                         :elide-asserts  true

                                         ;; no optimize compile (dev)
                                         ;;:optimizations :none
                                         ;; when no optimize uncomment
                                         :output-dir     "app/js/out"

                                         ;; simple compile (dev)
                                         ;;:optimizations :simple

                                         ;; advanced compile (prod)
                                         :optimizations  :none

                                         ;;:source-map "app/js/test.js.map"
                                         :pretty-print   true
                                         :output-wrapper true}}
               :e2e      {:id           "e2e-scenarios-example-e2etests"
                          :source-paths ["test_e2e"]
                          :incremental  true
                          :jar          false
                          :assert       true
                          :compiler     {:target         :nodejs
                                         :output-to      "target/test_e2e/main.js"
                                         :externs        ["app/js/externs.js"]
                                         :warnings       true
                                         :elide-asserts  true
                                         :language-in :ecmascript5
                                         :language-out :ecmascript5

                                         ;; no optimize compile (dev)
                                         ;;:optimizations :none
                                         ;; when no optimize uncomment
                                         ;:output-dir     "target/test_e2e/"

                                         ;; simple compile (dev)
                                         :optimizations :simple

                                         ;; advanced compile (prod)
                                         ;:optimizations  :none

                                         ;;:source-map "app/js/test.js.map"
                                         :pretty-print   true
                                         :output-wrapper true}}}
              :test-commands {"e2e" ["node" "target/test_e2e/main.js"]}}
  :figwheel {:http-server-root "public"
             :ring-handler figwheel-middleware/app
             :server-port 3449})
