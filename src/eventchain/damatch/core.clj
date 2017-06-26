(ns eventchain.damatch.core
  (:require [clojure.zip :as zip]
            [clojure.spec.alpha :as s]
            [clojure.template :as t]
            [clojure.spec.test.alpha :as spec.test]))

(defmulti tree-branch? class)
(defmethod tree-branch? :default [_] false)
(defmethod tree-branch? clojure.lang.IPersistentCollection [_] true)

(defmulti tree-children class)
(defmethod tree-children clojure.lang.IPersistentVector [v] (seq v))
(defmethod tree-children clojure.lang.IPersistentMap [m] (seq m))
(defmethod tree-children clojure.lang.IPersistentList [l] (seq l))
(defmethod tree-children clojure.lang.ISeq [s] s)
(defmethod tree-children clojure.lang.IPersistentSet [s] (seq s))
(prefer-method tree-children clojure.lang.IPersistentList clojure.lang.ISeq)

(defmulti tree-make-node (fn [node _] (class node)))
(defmethod tree-make-node clojure.lang.IPersistentVector [_ children]
  (vec children))
(defmethod tree-make-node clojure.lang.IPersistentMap [_ children]
  (apply hash-map (apply concat children)))
(defmethod tree-make-node clojure.lang.IPersistentList [_ children]
  children)
(defmethod tree-make-node clojure.lang.ISeq [_ children]
  (apply list children))
(defmethod tree-make-node clojure.lang.IPersistentSet [_ children]
  (set children))
(prefer-method tree-make-node clojure.lang.IPersistentList clojure.lang.ISeq)

(defn tree-zipper [root]
  (zip/zipper tree-branch? tree-children tree-make-node root))

(defn variable?
  [candidate]
  (symbol? candidate))

(defn underscore?
  [candidate]
  (= candidate '_))

(defn match?
  [data pattern]
  (loop [ctx {:match? true
              :vars {}}
         p-loc (tree-zipper pattern)
         d-loc (tree-zipper data)]
    (let [p-node (zip/node p-loc)
          d-node (zip/node d-loc)
          p-next (zip/next p-loc)
          d-next (zip/next d-loc)
          falsify #(assoc ctx :match? false)]
      (cond
        (zip/end? p-loc) ctx
        (zip/end? d-loc) (falsify)
        (zip/branch? p-loc) (recur ctx p-next d-next)
        (underscore? p-node) (recur ctx p-next d-next)
        (variable? p-node) (assoc-in ctx [:vars p-node] d-node)
        (not= p-node d-node) (falsify)
        :else (recur ctx p-next d-next)))))

(defn pattern-match?
  [data pattern]
  (match? data pattern))

(defn data-match?
  [pattern data]
  (match? data pattern))

(defn foo-foo
  [data pattern template]
  (let [result (match? data pattern)
        names (vec (map key (:vars result)))
        values (vals (:vars result))]
    (when (:match? result) (t/apply-template names template values))))

(defn bla-bla
  [data [pattern template]]
  (foo-foo data pattern template))

(defn pattern-match
  [data matchers]
  (some (partial bla-bla data) (partition 2 matchers)))

(defn bar-bar
  [pattern [data template]]
  (foo-foo data pattern template))

(defn data-match
  [pattern matchers]
  (some (partial bar-bar pattern) (partition 2 matchers)))
