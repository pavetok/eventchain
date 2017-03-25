(ns eventchain.events
  (:require [clojure.string :as str]
            [clojure.spec :as s]
            [datomic.api :as d]))

(def event-schema
  [{:db/ident :event/type
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "Event type"}])

(defn event-new
  [type-label]
  [{:event/type type-label}])

(defn event-count
  [db]
  (count (d/q '[:find ?n :where [_ :event/type ?n]] db)))
