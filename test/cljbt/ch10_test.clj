(ns cljbt.ch10-test
  "Tests for the ch10"
  (:require [cljbt.ch10 :as ch10]
            [clojure.string :as str]
            [manifold.deferred :as md]
            [clojure.test :refer :all]))

(deftest fetch-n-quotes-tests
  (testing "fetches some stuff"
    (is (= 5 (count @(ch10/fetch-n-quotes 5))))))

(deftest count-words
  (testing "counts"
    (is (= {"a" 1 "b" 2} (ch10/count-words ["a b b"])))))
