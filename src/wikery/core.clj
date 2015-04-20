(ns wikery.core
  (:use clojure.java.io)
  (:require [clojure.data.xml :as xml]
            [clojure.zip :as zip]
            [clojure.data.zip.xml :as zx]
            [clojure.data.json :as json])
  (:import org.apache.commons.io.FilenameUtils)
  (:gen-class))

(def ^:private input (ref ""))

;;Creates lazy seq with the parsed source
(defn wik-source->seq [source]
  (dosync
   (ref-set input source)
   (-> source
       input-stream
       xml/parse)))

;;function to create json path
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
(defn wik-save [abstracts]
  (with-open [out (writer (str "resources/"
                               (FilenameUtils/getBaseName @input)
                               ".json"))]
    (json/write (map abstract->map abstracts) out)))

;;Load stored json, if we don't have a stored file we retrieve it from the URL
(defn wik-load [source]
  (let [json-file (json-path source)]
    (if (.exists (as-file json-file))
      (with-open [in (reader json-file)]
        (json/read in :key-fn keyword))
      (wik-source->seq source))))
