(ns cljbt.ch5)

(defn attr
  "Return a given character's attribute; the character must conform to a specific shape"
  [character attribute]
  (attribute (:attributes character)))


(defn my-comp
  "Return a function that is the composition of all functions passed in as arguments"
  [& functions]
  (fn [param]
    (let [func (first functions)
          result (func param)
          remaining (rest functions)]
      (loop [result result
             remaining-fun remaining]
        (if (empty? remaining-fun)
          result
          (recur ((first remaining-fun) result) (rest remaining-fun)))))))
