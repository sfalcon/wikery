(ns wikery.api
  "JSON api"
  (:use compojure.core
        ring.middleware.params)
  (:require [cheshire.core :as json]
            [clojure.string :as str]
            [wikery.io :as wik-io]))

;;Define the source
;;(def source "http://dumps.wikimedia.org/enwiki/latest/enwiki-latest-abstract23.xml")
(def src "resources/enwiki-latest-abstract23.xml")
;;Set as input
(wik-io/set-src! src)

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string data)})

(defroutes handler
  (GET "/search" request
       (let [query (str/split (request :query-string) #"=")]
         (json-response (wik-io/wik-query (second query))))))

(def app handler)
