(ns eventchain.sdd.core-test
  (:require [eventchain.sdd.core :as sut]
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]
            [clojure.spec.test.alpha :as spec.test]))

(defn passed?
  [result]
  (-> result
      first
      :clojure.spec.test.check/ret
      :result))

(deftest my-div-test-2
  (is (= true (passed? (spec.test/check `sut/my-div)))))

(deftest my-div-test
  (let [result (-> `sut/my-div spec.test/check)]
    (clojure.pprint/pprint (-> result first :clojure.spec.test.check/ret))
    (is (= true (passed? result)))
    ))
