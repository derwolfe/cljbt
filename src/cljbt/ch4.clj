(ns cljbt.ch4
  "stuff for chapter 4"
  (:require
   [clojure.java.io :as io]))

(def suspects-file (io/file (io/resource "suspects.csv")))

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\n")))

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\" :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

;; exercises here onward

(defn glitter-filter->names
  "Given an ISeq of maps, each containing a name, return an ISeq of those names"
  [people-maps]
  (map :name people-maps))

(defn validate-suspect
  "Validate that the provided properties are present and non-nil on the target "
  [properties suspect]
  (let [valid-property (fn [property]
                         {property (not (nil? (property suspect)))})
        validated (into {} (map #(valid-property %) properties))
        valid-at-all? (fn [validated-suspect]
                 (every? true? (vals validated-suspect)))]
    {:is-valid (valid-at-all? validated)
     :validation-map validated}))


(defn add-suspect-to-file!
  "Add a new suspect to the file"
  [suspect]
  (let [{:keys [is-valid validation-map]} (validate-suspect [:glitter-index :name] suspect)]
    (if is-valid
      (let [{:keys [name glitter-index]} suspect
            row (str "\n" name "," glitter-index)]
        (spit suspects-file row :append true)
        (println (slurp suspects-file)))
      (println "Validation Error" validation-map))))


(defn maps->csv
  "convert the suspect maps back into a csv string"
  [suspect-maps]
  (let [->csv (fn [suspect]
                (str (:name suspect) "," (:glitter-index suspect)))]
    (clojure.string/join "\n" (map ->csv suspect-maps))))


(def t {:name "Edward Cullen", :glitter-index 10})
(def b {:name "Edward Cullen", :glitter 10})
