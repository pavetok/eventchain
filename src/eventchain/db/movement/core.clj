(ns eventchain.db.movement.core
  (:require [clojure.core.match :as m]))

(def object
  {:point 1})

(def transition
  {:from 1
   :to 2})

(def move-fn
  {:cases
   [[0] "foo"
    [1] "bar"
    ]})

(defn apply-fn
  [f & args]
  f)

(apply-fn move-fn [object, transition])

(assoc-in object [:point] (:to transition))

(m/match 1 1 :1)
(m/match [1] [1] :1)
(m/match-let [x 1] [1] :1)

(defn case-of
  [val cases]
  (loop [n 0]
    (let [case (nth (partition 2 cases) n)
          m (first case)]
      (if (= val m)
       (second case)
       (recur (inc n))))))

(case-of
 :m1
 [:m1 :v1
  :m2 :v2
  :m1 :v3])
