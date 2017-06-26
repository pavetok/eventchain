(ns eventchain.sdd.core
  (:require [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [datomic.api :as d]
            [clojure.spec.test.alpha :as spec.test]))

(defn my-div [x y]
  "divide two numbers"
  (/ x y))

(s/fdef my-div
        :args (s/and (s/cat :first number? :second number?)
                     #(not (zero? (:second %))))
        :ret number?)
