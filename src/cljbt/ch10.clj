(ns cljbt.ch10
  "concurrency - stuff for chapter 10"
  (:require [manifold.deferred :as md]
            [byte-streams :as bs]
            [clojure.string :as str]
            [aleph.http :as http]))

;; this is a the atom containing all of the state
(def quotes (atom {}))
(def quote-addr "http://www.braveclojure.com/random-quote")

(defn count-words
  [words]
  (->> (str/split-lines words)
       first
       str/lower-case
       ;; eww why invoke self.
       (#(str/split % #"\s+"))
       frequencies))

(defn fetch!
  []
  (md/chain
   (http/get quote-addr)
   :body
   bs/to-string
   (fn [quote]
     (swap!
      quotes
      (fn [old]
        (merge-with +
                    old
                    (count-words quote)))))))

;; could make a stream, realize-each, read-from stream too
(defn fetch-n-quotes
  [n]
  (md/chain
   (apply
    md/zip'
    (for [_ (range n)]
      (fetch!)))
   (fn [_] @quotes)))


;; lets say that jan heals bob
(defn heal!
  "Requires that the healer have a potion; this potion will be used as much as
  possible to get the character back to max-health."
  [target healer]
  (dosync
   ;; update the healer, remove the potion as it will be entirely used up
   ;; XXX this doesn't work. (:potion (:bag healer)) seems to be returning nil
   (let [potion-val (:potion (:bag healer))]
     (alter target update :hp + potion-val)
     (alter healer update :bag dissoc :potion))))
