(ns wikery.core-test
  (:use clojure.java.io
        midje.sweet
        wikery.core))

(facts "about the abstracts file"
       (fact "wikipedia sample abstracts file is in place"
             true => (.exists (as-file "resources/enwiki-latest-abstract23.xml"))))

(facts "about querying the parsed file"
       (fact "the first title of the file is -> Wikipedia: Ahmad Reza Pourdastan"
             (query :title :pos 1) => "Wikipedia: Ahmad Reza Pourdastan")
       (fact "the title 10 of the file is ->  Wikipedia: Charles Lennox Wyke"
             (query :title :pos 10) => "Wikipedia: Charles Lennox Wyke"))

(facts "about reformatting the parsed file"
       )
