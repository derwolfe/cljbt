(ns cljbt.ch10
  "concurrency - stuff for chapter 10"
  (:require [manifold.deferred :as md]
            [byte-streams :as bs]
            [aleph.http :as http]))


;; this is a the atom containing all of the state
(def quotes (atom []))
(def quote-addr "http://www.braveclojure.com/random-quote")

(defn count-words
  [words]
  (frequencies words))

(defn fetch!
  []
  (md/chain
   (http/get quote-addr)
   :body
   bs/to-string
   (fn [quote] (swap! quotes conj quote))))

;; could make a stream, realize-each, read-from stream too
(defn fetch-n-quotes
  [n]
  (md/chain
   (apply
    md/zip'
    (for [_ (range n)]
      (fetch!)))
   (fn [_] @quotes)))
