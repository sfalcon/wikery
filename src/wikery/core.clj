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

;;Convert a zipper node structure into the record format desired
(defn abstract->map [node]
  (let [loc (zip/xml-zip node)
        map-keys [:title :url :abstract]
        qform (fn [key] (zx/xml1-> loc key zx/text))]
    (apply array-map ;;array-map will respect the order of the keys we set
           (interleave map-keys (map qform map-keys)))))

;;convert parsed abstracts into json queryable format
(defn save [abstracts]
  (with-open [file-name (FilenameUtils/getName @input)
              out (output-stream (str "resources/" file-name))]
    (json/write (map abstract->map abstracts)) out))
