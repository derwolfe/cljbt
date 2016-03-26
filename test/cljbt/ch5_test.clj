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

(deftest my-comp-tests
  (testing "returns a function for a single function"
    (let [my-comp-out-fn (ch5/my-comp inc dec inc)
          real-comp-out-fn (comp inc dec inc)
          parameter 1
          expected 2
          out-from-real-comp (real-comp-out-fn parameter)
          out-from-my-comp (my-comp-out-fn parameter)]
      (is (= expected out-from-real-comp out-from-my-comp)))))
