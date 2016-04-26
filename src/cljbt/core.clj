(ns cljbt.core)

;; (defn better-symmetrize-body-parts
;;   [asym-body-parts]
;;   (reduce (fn [final-body-parts part]
;;             (into final-body-parts (set [part (matching-part part)])))
;;           []
;;           asym-body-parts))

;; (defn symmetrize-alien-body-parts
;;   [asym-body-parts]
;;   (reduce (fn [final-body-parts part]
;;             (into final-body-parts (set [part (match-radially part number-of-appendages)])))
;;           []
;;           asym-body-parts))

;; (defn hit-alien
;;   [asym-body-parts]
;;   (let [sym-parts (symmetrize-alien-body-parts asym-body-parts)
;;         body-part-size-sum (reduce + (map :size sym-parts))
;;         target (rand body-part-size-sum)]
;;     (loop [[part & remaining] sym-parts
;;            accumulated-size (:size part)]
;;       (if (> accumulated-size target)
;;         part
;;         (recur remaining (+ accumulated-size (:size (first remaining))))))))


;; (defn hit
;;   [asym-body-parts]
;;   (let [sym-parts (better-symmetrize-body-parts asym-body-parts)
;;         body-part-size-sum (reduce + (map :size sym-parts))
;;         target (rand body-part-size-sum)]
;;     (loop [[part & remaining] sym-parts
;;            accumulated-size (:size part)]
;;       (if (> accumulated-size target)
;;         part
;;         (recur remaining (+ accumulated-size (:size (first remaining))))))))

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

;; this could use a function to generate legs, arms, wobbles, etc.
(def number-of-appendages 5)
(def alien-body-parts [{:name "head" :size 3}
                       {:name "garfle-1" :size 1}
                       {:name "garfle-2" :size 1}
                       {:name "garfle-3" :size 1}
                       {:name "garfle-4" :size 1}
                       {:name "garfle-5" :size 1}
                       {:name "mouth" :size 1}
                       {:name "nose" :size 1}
                       {:name "neck" :size 2}
                       {:name "chest" :size 10}
                       {:name "back" :size 10}
                       {:name "abdomen" :size 6}
                       {:name "tentacle-1" :size 2}
                       {:name "tentacle-2" :size 2}
                       {:name "tentacle-3" :size 2}
                       {:name "tentacle-4" :size 2}
                       {:name "tentacle-5" :size 2}])

(def hobbit {:species "hobbit"
             :body-parts asym-hobbit-body-parts})

(def alien {:species "alien"
            :total-parts 5
            :body-parts alien-body-parts})

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn match-radially
  "Given N parts for a given thing, find the radially symmetric member. E.g,
  Given 5 total legs, the 1st leg is symmetric with the 5th, the second with
  the fourth, and the third with itself."
  [part total-parts]
  (let [[part-name index-str] (clojure.string/split (:name part) #"-")]
    (if (nil? index-str)
      part
      {:name (str part-name "-" (- (+ total-parts 1) (Integer/parseInt index-str)))
       :size (:size part)})))

(defn symmetrize-parts
  [being]
  (let [{:keys [species body-parts total-parts]} being
        parts-fn (case species
                   "alien" #(match-radially % total-parts)
                   "hobbit" #(matching-part %))]
    (reduce (fn [final-body-parts part]
              (into final-body-parts (set [part (parts-fn part)])))
            []
            body-parts)))

(defn smack
  [being]
  (let [sym-parts (symmetrize-parts being)
        body-part-size-sum (reduce + (map :size sym-parts))
        target (rand body-part-size-sum)]
    (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur remaining (+ accumulated-size (:size (first remaining))))))))

(defn mapset
  [fn vals]
  (set (map fn vals)))

(defn dec-maker
  [dec-value]
  #(- % dec-value))

(defn dumb-map
  [func vals]
  ;; use an accumulator `result` to capture each step
  (loop [result []
         values vals]
    (if-not (empty? values)
      (recur (conj result (func (first values)))
             (rest values))
      result)))

;; cljbt.core> (dumb-map inc [1 2 3 4])
;; [2 3 4 5]

;; bring it together for all manner of beasts
