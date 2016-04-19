(ns cljbt.ch9-test
  "Tests for the ch9"
  (:require [cljbt.ch9 :as ch9]
            [clojure.string :as str]
            [manifold.deferred :as md]
            [clojure.test :refer :all]))

(def example "http://www.example.com/")
(def duckduckgo "https://api.duckduckgo.com/?q=valley+forge+national+park&format=json&pretty=1")

(deftest fetch-resource!-tests
  (testing "gets duckduckgo page 1"
    (is (not (empty? @(ch9/fetch-resource! duckduckgo)))))
  (testing "calls random website that supports being called"
    (is (not (empty? @(ch9/fetch-resource! example))))))

(deftest fetch-all!-tests
  (testing "returns a vector containing each result"
    (let [results @(ch9/fetch-all! [duckduckgo example])]
      (is (= 2 (count results))))))

;; (deftest fastest-first-tests
;;   (testing "returns the first realized deferred"
;;     (let [d1 (md/deferred)
;;           d2 (md/deferred)
;;           result "I win."
;;           ds [d1 d2]]
;;       (is (= result @(ch9/fastest-first ds))))))
