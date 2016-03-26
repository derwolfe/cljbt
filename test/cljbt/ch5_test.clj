(ns cljbt.ch5-test
  "Tests for the ch5 functions"
  (:require [cljbt.ch5 :as ch5]
            [clojure.test :refer :all]))

(deftest attr-tests
  (testing "accesses correct attr"
    (let [character {:name "Smooches McCutes"
                     :attributes {:intelligence 10
                                  :strength 4
                                  :dexterity 5}}]
      (dorun
       (for [attribute (seq (:attributes character))
             :let [attr-under-test (first attribute)
                   expected-value (second attribute)]]
         (is (= expected-value
                (ch5/attr character attr-under-test))))))))
