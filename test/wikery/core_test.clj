(ns wikery.core-test
  (:use midje.sweet
        wikery.core))



(facts "about parsing the abstracts file"
       (fact "wikipedia sample abstracts file is in place"
             true => (.exists (as-file "resources/enwiki-latest-abstract23.xml")))
       (fact "the first title of the file is -> Wikipedia: Ahmad Reza Pourdastan"
             (query :title :pos 1) => "Wikipedia: Ahmad Reza Pourdastan"))
