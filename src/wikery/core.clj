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

;;I would like to have dynamic querying forms,
;;so I'm building the base for that here
(def query-base (partial zx/xml-> wik-zipper :doc))

;;Querying the structure
(defn query [tag & {:keys [value pos]}]
  ( nth (query-base tag zx/text) (dec pos)))
