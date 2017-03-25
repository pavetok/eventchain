(ns eventchain.events
  (:require [clojure.string :as str]
            [clojure.spec :as s]
            [datomic.api :as d]))

(def event-schema
  [{:db/ident :evch.event/type
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "Event type"}])

(defn event-new
  [type-label]
  [{:evch.event/type [:evch.type/label type-label]}])

(defn event-count
  [db]
  (count (d/q '[:find ?n
                :where
                [_ :evch.event/type ?n]]
              db)))
