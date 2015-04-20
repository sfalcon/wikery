(ns wikery.core-test
  (:use clojure.java.io
        midje.sweet
        wikery.core)
  (:require [clojure.zip :as zip]
            [clojure.data.zip.xml :as zx])
  (:import org.apache.commons.io.FilenameUtils))

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
              (type @parsed-source) => clojure.data.xml.Element)))

(let [zipper (zip/xml-zip @parsed-source)
      ;;abstract->map expects a list
      abs-map (abstract->map (zx/xml1-> zipper :doc zip/node))
      exists (fn [x] (.exists (as-file x)))]
  (facts "about reformatting the parsed file"
         (fact "formatting the first title yields a map with these keys"
               (keys abs-map) => '(:title :url :abstract))
         (fact "the title is Wikipedia: Ahmad Reza Pourdastan"
               (:title abs-map) => "Wikipedia: Ahmad Reza Pourdastan"))
  (facts "about io of reformatted parsed files"
         (fact "there is no file before saving the abstract"
               (exists (json-path sample-source)) => false)
         (fact "and there is a json file after saving it"
               (wik-save (list (zx/xml1-> zipper :doc zip/node)))
               (exists (json-path sample-source)) => true))
  (facts "about then loading it"
         (fact "reading it yields a map"
               (:title (first (wik-load sample-source)))
               => "Wikipedia: Ahmad Reza Pourdastan"))

  ;;cleanup
  ;(delete-file (as-file (json-path sample-source)) true)
  )
