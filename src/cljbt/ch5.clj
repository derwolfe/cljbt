(ns cljbt.ch5)

(defn attr
  "Return a given character's attribute; the character must conform to a specific shape"
  [character attribute]
  (attribute (:attributes character)))

(defn my-comp
  "Return a function that is the composition of all functions passed in as arguments"
  [& functions]
  ;; this should probably use reduce
  (fn [param]
    (let [func (first functions)
          result (func param)
          remaining (rest functions)]
      (loop [result result
             remaining-fun remaining]
        (if (empty? remaining-fun)
          result
          (recur ((first remaining-fun) result) (rest remaining-fun)))))))

(defn my-assoc-in
  "Associate a nested key/value pair with a map"
  [m [k & ks] v]
  (if ks
    ;; cheated; looked up def as I got stuck,
    (assoc m k (my-assoc-in (get m k) ks v))
    (assoc m k v)))

(defn my-update-in
  [m [k & ks] f & args]
  (if ks
    (assoc m k (apply my-update-in (get m k) ks f args))
    (assoc m k (apply f (get m k) args))))
