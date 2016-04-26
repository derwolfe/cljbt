(ns cljbt.ch9
  "concurrency - stuff for chapter 9"
  (:require [manifold.deferred :as md]
            [byte-streams :as bs]
            [aleph.http :as http]))

;; this is super duper brittle re tls
(defn fetch-resource!
  [place]
  (md/chain
   (http/get place)
   (fn [r] (print r) r)
   :body
   bs/to-string))

(defn fetch-all!
  "returns a deferred containing the results of each call.

  This will fire a success when *each* deferred is complete."
  [places]
  (apply
   md/zip'
   (for [place places]
     (fetch-resource! place))))
