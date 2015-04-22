(ns wikery.io-test

  (:use clojure.java.io
        midje.sweet
        wikery.io)
  (:require [clojure.zip :as zip]
            [clojure.data.zip.xml :as zx])
  (:import org.apache.commons.io.FilenameUtils))

(def sample-source "resources/sample.xml")
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
      ;;abstract->map expects a seq
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
               (wik-save sample-source)
               (exists (json-path sample-source)) => true))
  (let [loaded (first (wik-load sample-source))]
    (facts "about then loading it"
           (fact "reading it yields a map"
                 (:title(first loaded))
                 => "Wikipedia: Ahmad Reza Pourdastan"))
    (facts "about then querying it"
           (fact "querying for \"Ahmad\" would yield this structure"
                 (wik-query "Ahmad") =>
                 {:q "Ahmad",
                  :results [{
                             :title "Wikipedia: Ahmad Reza Pourdastan"
                             :url "http://en.wikipedia.org/wiki/Ahmad_Reza_Pourdastan"
                             :abstract "Ahmad Reza Pourdastan (Persian: امیر سرتیپ احمدرضا پوردستان) is an Iranian general currently serving as commanding officer of the ground forces of the Iranian Army.<Ref>"
                             }]
                  })))

  ;;cleanup
  (delete-file (as-file (json-path sample-source)) true)
  )
