(ns wikery.core
  (:use clojure.java.io)
  (:require [clojure.data.xml :as xml]
            [clojure.zip :as zip]
            [clojure.data.zip.xml :as zx])
  (:gen-class))

;;Hardcoding the file for starters
(def abstracts-file "resources/enwiki-latest-abstract23.xml")

;;Let's create a lazy data structure of the parsed xml
(def parsed-abstracts
  (-> abstracts-file
      input-stream
      xml/parse))

;;A zipper to navigate the xml structure
(def wik-zipper (zip/xml-zip parsed-abstracts))

;;Preferable to have dynamic querying forms,
;;this attempts to build a base for that
(def query-base (partial zx/xml-> wik-zipper :doc))

;;Querying the structure
(defn query [tag & {:keys [value pos]}]
  (let [final-query (if (= :all tag)
                      (partial query-base zip/node)
                      (partial query-base tag zx/text))]
    (nth (final-query) (dec pos))))



;;Convert the structure into the map format desired
(defn abstract->map [node]
  (let [loc (zip/xml-zip node)]
    {:title    (zx/xml1-> loc :title    zx/text)
     :url      (zx/xml1-> loc :url      zx/text)
     :abstract (zx/xml1-> loc :abstract zx/text)}))
