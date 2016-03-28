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

(deftest my-assoc-in-tests
  (testing "associates a value in a nested structure where that value does not exist"
    (let [original {:top {:bottom 1}}
          expected {:top {:bottom 1 :blue 42}}
          result (ch5/my-assoc-in original [:top :blue] 42)]
      (is (= expected result)))))

(deftest my-update-in-tests
  (testing "associates a value no args"
    (let [original {:top {:bottom 1}}
          expected {:top {:bottom 2}}
          result (ch5/my-update-in original [:top :bottom] inc)]
      (is (= expected result))))
  (testing "updates val with args"
    (let [original {:top {:bottom 1}}
          expected {:top {:bottom 8}}
          result (ch5/my-update-in original [:top :bottom] * 2 2 2)]
      (is (= expected result)))))
