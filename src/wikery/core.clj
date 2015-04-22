(ns wikery.core
  (:use wikery.api
        ring.adapter.jetty)
  (:gen-class))

;;Ensure port 8080 is free
(defn -main []
  (run-jetty #'app {:port 8080}))
