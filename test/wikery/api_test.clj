(ns wikery.api-test
  (:require [wikery.api :refer :all]
            [midje.sweet :refer :all]))

(def query-source "http://dumps.wikimedia.org/enwiki/latest/enwiki-latest-abstract23.xml")

;; (facts "about querying for title and abstract"
;;        (wik-use query-source)
;;        (fact "querying for Holmsund retrieves this structure"
;;              (query )))
