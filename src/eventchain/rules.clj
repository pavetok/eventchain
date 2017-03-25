(ns eventchain.rules
  (:require [clojure.string :as str]
            [clojure.spec :as s]
            [datomic.api :as d]))

(def rule-schema
  [{:db/ident :evch.rule/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "Rule name"}])

(defn rule-new
  [name]
  [{:evch.rule/name name}])

(defn rule-count
  [db]
  (count (d/q '[:find ?n
                :where
                [_ :evch.rule/name ?n]]
              db)))
