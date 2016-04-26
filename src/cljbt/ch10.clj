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

;; (def state (atom {:bob {:hp 15
;;                         :bag {}}
;;                   :jan {:hp 40
;;                         :bag {:potion 40}}}))


;; I prefer the following to using refs
;; This has a single state atom
;; To heal, there is a function that will be used to update the game state
;; the function can be tested in isolation, since it is a pure function.
;; The state transformer only invokes the function; this way a single
;; transformation is responsible for healing a player and removing the item.
(defn better-heal
  "heal a player"
  [state target healer]
  (let [target-data (target state)
        healer-data (healer state)
        potion-value (:potion (:bag healer-data))
        ;; could probably just use update
        s1 (update-in state [target :hp] + potion-value)]
    (update-in s1 [healer :bag] dissoc :potion)))

(defn heal-state
  [state target healer]
  (swap! state better-heal target healer))
