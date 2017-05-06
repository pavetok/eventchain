(ns eventchain.types
  (:require [clojure.string :as str]
            [clojure.spec :as s]
            [datomic.api :as d]))

(def type-schema
  [{:db/ident :type
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity}])

(defn type-new
  [label]
  [{:evch.type/label label}])

(defn type-count
  [db]
  (count (d/q '[:find ?n
                :where
                [_ :evch.type/label ?n]]
              db)))
