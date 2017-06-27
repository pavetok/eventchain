(ns eventchain.damatch.core-test
  (:require [eventchain.damatch.core :as sut]
            [clojure.test :refer :all]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as spec.test]))

(def minimal-maps
  [{:1 1}
   {:1 {}}
   {:1 []}
   '{:1 _}
   '{:1 ?x}
   '{?x 1}])

(def normal-maps
  [{:1 1 :2 2}])

(def empty-colls
  [[]
   {}
   ()
   #{}
   (sequence [])
   (sequence {})
   (sequence ())
   (sequence #{})])

(def minimal-colls
  [[[]]
   [1]
   '[_]
   '[?x]
   '(1)
   '(_)
   '(?x)
   #{1}
   '#{_}
   '#{?x}])

(def normal-colls
  [[1 2]
   '[1 ?x]
   '[1 _]
   '(1 2)
   '(1 ?x)
   '(1 _)
   #{1 2}
   '#{1 ?x}
   '#{1 _}])

(def primitives
  [0
   \a
   "a"
   true
   :a
   nil])

(def variables
  ['_
   'x
   '?x])

(def any-values
  (concat
   empty-colls
   minimal-maps
   minimal-colls
   normal-maps
   normal-colls
   primitives
   variables))

(deftest test-tree-branch?
  (testing "empty colls"
    (doseq [empty-coll empty-colls]
      (is (= true (sut/tree-branch? empty-coll)))))
  (testing "minimal colls"
    (doseq [minimal-coll minimal-colls]
      (is (= true (sut/tree-branch? minimal-coll)))))
  (testing "minimal maps"
    (doseq [minimal-map minimal-maps]
      (is (= true (sut/tree-branch? minimal-map)))))
  (testing "primitives"
    (doseq [primitive primitives]
      (is (= false (sut/tree-branch? primitive))))))

(deftest test-tree-children
  (testing "empty colls"
    (doseq [empty-coll empty-colls]
      (is (= nil (sut/tree-children empty-coll)))))
  (testing "minimal colls"
    (doseq [minimal-coll minimal-colls]
      (is (= (seq minimal-coll) (sut/tree-children minimal-coll)))))
  (testing "minimal maps"
    (doseq [minimal-map minimal-maps]
      (is (= (seq minimal-map) (sut/tree-children minimal-map))))))

(deftest test-tree-make-node
  (testing "empty colls"
    (doseq [empty-coll empty-colls]
      (is (= empty-coll (sut/tree-make-node empty-coll empty-coll)))))
  (testing "minimal colls"
    (doseq [minimal-coll minimal-colls]
      (is (= minimal-coll (sut/tree-make-node minimal-coll minimal-coll)))))
  (testing "minimal maps"
    (doseq [minimal-map minimal-maps]
      (is (= minimal-map (sut/tree-make-node minimal-map (vec minimal-map)))))))

(deftest test-match?
  (testing "primitive should match with yourself"
    (doseq [primitive primitives]
      (is (= true (:match? (sut/match? primitive primitive))))))
  (testing "empty coll should not match with primitive"
    (doseq [empty-coll empty-colls
            primitive primitives]
      (is (= false (:match? (sut/match? empty-coll primitive))))))
  (testing "any value should match with empty coll"
    (doseq [any-value any-values
            empty-coll empty-colls]
      (is (= true (:match? (sut/match? any-value empty-coll))))))
  (testing "any value should match with variable"
    (doseq [any-value any-values
            variable variables]
      (is (= true (:match? (sut/match? any-value variable))))))
  (testing "empty coll should match with yourself"
    (doseq [empty-coll empty-colls]
      (is (= true (:match? (sut/match? empty-coll empty-coll))))))
  (testing "minimal coll should match with yourself"
    (doseq [minimal-coll minimal-colls]
      (is (= true (:match? (sut/match? minimal-coll minimal-coll))))))
  (testing "minimal map should match with yourself"
    (doseq [minimal-map minimal-maps]
      (is (= true (:match? (sut/match? minimal-map minimal-map))))))
  (testing "normal map should match with minimal map"
    (doseq [normal-map normal-maps
            minimal-map minimal-maps]
      (is (= true (:match? (sut/match? normal-map minimal-map))))))
  (testing "empty coll should not match with minimal coll"
    (doseq [empty-coll empty-colls
            minimal-coll minimal-colls]
      (is (= false (:match? (sut/match? empty-coll minimal-coll))))))
  (testing "normal coll should match with minimal coll"
    (doseq [normal-coll normal-colls
            minimal-coll minimal-colls]
      (is (= true (:match? (sut/match? normal-coll minimal-coll))))))
  (testing "minimal coll should not match with normal coll"
    (doseq [minimal-coll minimal-colls
            normal-coll normal-colls]
      (is (= false (:match? (sut/match? minimal-coll normal-coll)))))))

(deftest debug-test
  (is (= 1 (sut/pattern-match {:1 1} '[{:1 _} 1])))
  (is (= 1 (sut/pattern-match {:1 1} '[{:1 x} x])))
  (is (= 1 (sut/data-match '{:1 _} [{:1 1} 1]))))


;; some experiments property/spec-based testing

(defspec tree-branch?-should-correctly-handle-empty-colls
  (count empty-colls)
  (prop/for-all [empty-coll (gen/elements empty-colls)]
                (= true (sut/tree-branch? empty-coll))))

(defn passed?
  [result]
  (-> result
      first
      :clojure.spec.test.check/ret
      :result))

(s/def ::primitives (s/or :number number?
                          :char char?
                          :string string?))

(s/def ::coll-of-primitives (s/or :vector (s/coll-of ::primitives :kind vector?)
                                  :list (s/coll-of ::primitives :kind list?)
                                  :set (s/coll-of ::primitives :kind set?)
                                  :seq (s/coll-of ::primitives :kind seq?)))

(s/def ::map-of-primitives (s/map-of keyword? ::primitives))

(s/fdef sut/tree-branch?
        :args (s/cat :0 (s/or :coll ::coll-of-primitives
                              :map ::map-of-primitives))
        :fn #(= (:ret %) (-> % :args :0 coll?))
        :ret boolean?)

(deftest spec-test-tree-branch?
  (is (= true (passed? (spec.test/check `sut/tree-branch? {:clojure.spec.test.check/opts {:num-tests 25}})))))
