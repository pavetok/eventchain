(ns eventchain.types
  (:require [clojure.string :as str]
            [clojure.spec :as s]
            [datomic.api :as d]))

(def simple-type-schema
  [{:db/ident :evch.type/label
    :db/valueType :db.type/keyword
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/doc "Type label"}])

; (def simple-type-schema
;   [{:db/ident :MyType
;     :db/doc "Type label"}])

(defn type-new
  [label]
  [{:evch.type/label label}])

(defn type-count
  [db]
  (count (d/q '[:find ?n
                :where
                [_ :evch.type/label ?n]]
              db)))
