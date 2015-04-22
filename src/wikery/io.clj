(ns wikery.io
  "Parsing, loading and saving of abstracts"
  (:use clojure.java.io)
  (:require [clojure.data.xml :as xml]
            [clojure.zip :as zip]
            [clojure.data.zip.xml :as zx]
            [cheshire.core :as json])
  (:import org.apache.commons.io.FilenameUtils)
  (:gen-class))

;;Input source being worked on
(def ^:private input (ref ""))

;;Set input for queries
(defn set-src! [source]
  (dosync
   (ref-set input source)))

;;Creates lazy seq with the parsed source
(defn wik-source->seq [source]
  (dosync
   (ref-set input source)
   (-> source
       input-stream
       xml/parse)))

;;Function to create json path
(defn json-path [x]
  (str "resources/"
       (FilenameUtils/getBaseName x)
       ".json"))

;;Convert a zipper node structure into the record format desired
(defn abstract->map [node]
  (let [loc (zip/xml-zip node)
        map-keys [:title :url :abstract]
        qform (fn [key] (zx/xml1-> loc key zx/text))]
    (apply array-map ;;array-map will respect the order of the keys we set
           (interleave map-keys (map qform map-keys)))))

;;Convert parsed abstracts into json queryable format
(defn wik-save [source]
  (with-open [out (writer (str "resources/"
                               (FilenameUtils/getBaseName source)
                               ".json"))]
    (json/generate-stream (->> (wik-source->seq source)
                               :content
                               (map abstract->map))
                          out)))

;;Load stored json, if we don't have a stored file we create it from the URL
(defn wik-load [source]
  (let [json-file (json-path source)]
    (dosync
     (ref-set input source)
     (if (.exists (as-file json-file))
       (json/parsed-seq (reader json-file) keyword)
       (do
         (wik-save source)
         (wik-load source))))))

;;Create a filtering function with a regex pattern
(defn def-filterf [pattern]
  (fn [doc]
    (or (re-find pattern (:title doc))
        (re-find pattern (:abstract doc)) ) ))

(defn wik-query [term]
  (let [f (def-filterf (re-pattern term))
        wik-data (first (wik-load @input))]
    (do
      (println term)
      (cond (empty? term) ""
            (empty? wik-data) ""
            :else {
                   :q term
                   :results (filterv f wik-data)
                   }))))
