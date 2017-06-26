(ns eventchain.db.space.core
  (:require [clojure.string :as str]
            [datomic.api :as d]))

(def schema-space
  [{:db/ident :app.space/starting-point
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/isComponent true
    :db/doc "Space starting point"}
   {:db/ident :app.space/points
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/isComponent true
    :db/doc "Space points"}
   {:db/ident :app.space/transitions
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/isComponent true
    :db/doc "Space transitions"}])

(def schema-point
  [{:db/ident :app.point/x
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "X coordinate"}
   {:db/ident :app.point/y
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "Y coordinate"}
   {:db/ident :app.point/z
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "Z coordinate"}])

(def schema-transition
  [{:db/ident :app.transition/from
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "From point in the transition"}
   {:db/ident :app.transition/to
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "To point in the transition"}])

(def schema-route
  [{:db/ident :app.route/transitions
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/doc "Route transitions"}])

