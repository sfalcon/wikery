(ns wikery.core-test
  (:use midje.sweet
        clojure.java.io)
  (:require [wikery.core    :refer :all]
            [wikery.parsing :refer :all]))

(fact "wikipedia sample abstracts file is in place"
      (.exists
       (as-file "resources/enwiki-latest-abstract23.xml")) => true)

(facts "about parsing the abstracts file"
       (fact "the first title of the file is -> Wikipedia: Ahmad Reza Pourdastan"
             (query :title :pos 1) => "Wikipedia: Ahmad Reza Pourdastan"))
