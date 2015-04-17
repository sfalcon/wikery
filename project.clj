(defproject wikery "0.1.0-SNAPSHOT"
  :description
  "System that reads a wiki format file of abstracts and allows querying"
  :url ""
  :license {:name "CDDL-1.0"
            :url "http://opensource.org/licenses/CDDL-1.0"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.xml "0.0.8"]]
  :plugins [[lein-marginalia "0.8.0"]]
  :aliases {"autotest" ["midje" ":autotest"]}
  :main ^:skip-aot wikery.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[midje "1.6.3"]]}})
