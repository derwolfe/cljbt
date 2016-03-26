(ns cljbt.ch5)

(defn attr
  "Return a given character's attribute; the character must conform to a specific shape"
  [character attribute]
  (attribute (:attributes character)))
