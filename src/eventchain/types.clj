(ns eventchain.types
  (:require [clojure.string :as str]
            [clojure.spec :as s]
            [datomic.api :as d]))

(def simple-type-schema
  [{:db/ident :type/label
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "Type label"}])

(defn type-new
  [label]
  [{:type/label label}])

(defn type-count
  [db]
  (count (d/q '[:find ?n :where [_ :type/label ?n]] db)))
