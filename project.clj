(defproject wikery "0.1.0-SNAPSHOT"
  :description
  "System that reads a wiki format file of abstracts and allows querying"
  :url ""
  :license {:name "CDDL-1.0"
            :url "http://opensource.org/licenses/CDDL-1.0"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/data.zip "0.1.1"]
                 [org.clojure/data.json "0.2.6"]
                 [org.apache.commons/commons-io "1.3.2"]]
  :plugins [[lein-marginalia "0.8.0"]]
  :aliases {"autotest" ["midje" ":autotest"]}
  :main ^:skip-aot wikery.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[midje "1.6.3"]]}})
