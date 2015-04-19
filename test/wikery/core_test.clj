(ns wikery.core-test
  (:use clojure.java.io
        midje.sweet
        wikery.core)
  (:require [clojure.zip :as zip]
            [clojure.data.zip.xml :as zx])
  (:import [org.apache.commons.io
            FileUtils
            FilenameUtils]))


(def sample-source "http://dumps.wikimedia.org/enwiki/latest/enwiki-latest-abstract23.xml")
(def parsed-source (ref ""))

(facts "about the abstracts file"
       (fact "wikipedia source exists"
             (let [available (try
                               (> ( .available (input-stream sample-source)) 0)
                               (catch Exception e false))]
               available => true))
       (fact "parsing a source yields a lazy seq"
             (dosync
              (ref-set parsed-source (wik-source->seq sample-source))
              (type @parsed-source) => clojure.data.xml.Element) ))

(facts "about reformatting the parsed file"
       (let [zipper (zip/xml-zip @parsed-source)
             abs-map (abstract->map (first (zx/xml1-> zipper :doc)) )]
         (fact "formatting the first title yields a map with these keys"
               (keys abs-map) => '(:title :url :abstract))
         (fact "the title is Wikipedia: Ahmad Reza Pourdastan"
               (:title abs-map) => "Wikipedia: Ahmad Reza Pourdastan")
         (facts "about io of reformatted parsed files"
                (fact "saving the abstract creates a json file"
                      (save abs-map)
                      (.exists (as-file sample-source)))
                )))
