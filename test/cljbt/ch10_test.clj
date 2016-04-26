(ns cljbt.ch10-test
  "Tests for the ch10"
  (:require [cljbt.ch10 :as ch10]
            [clojure.string :as str]
            [manifold.deferred :as md]
            [clojure.test :refer :all]))

(deftest fetch-n-quotes-tests
  (testing "fetches some stuff"
    (is (< 0 (count @(ch10/fetch-n-quotes 5))))))

(deftest count-words
  (testing "counts"
    (is (= {"a" 1 "b" 1} (ch10/count-words "a b \n c")))))

;; (deftest heal!-tests
;;   (testing "heals"
;;     (let [target (ref {:hp 15
;;                        :max-hp 30
;;                        :bag {}})
;;           healer (ref {:hp 40
;;                        :max-hp 40
;;                        :bag {:potion 30}})]
;;       (is (= 15 (:hp @target)))
;;       (ch10/heal! target healer)
;;       (is (= 45 (:hp @target)))
;;       (is (= {} (:bag @target))))))

(deftest better-heal-tests
  (testing "heals"
    (let [state {:jan {:hp 15
                       :bag {}}
                 :bob {:hp 40
                       :bag {:potion 30}}}
          after (ch10/better-heal state :jan :bob)]
      (is (= 45 (:hp (:jan after))))
      (is (= {} (:bag (:bob after)))))))

(deftest heal-state
  (let [state (atom {:jan {:hp 15
                           :bag {}}
                     :bob {:hp 40
                           :bag {:potion 30}}})]
    (ch10/heal-state state :jan :bob)
    (is (= 45 (:hp (:jan @state))))
    (is (= {} (:bag (:bob @state))))))
