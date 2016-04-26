(defproject cljbt "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [manifold "0.1.4"]
                 [aleph "0.4.1"]]
  :plugins [[lein-cljfmt "0.3.0"]]
  :main ^:skip-aot cljbt.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
