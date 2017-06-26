(ns eventchain.db.types.core
  (:require [clojure.string :as str]
            [datomic.api :as d]))

(def schema-type
  [{:db/ident :app.type/constructors
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/doc "Constructors of type"}
   {:db/ident :app.type/constructor
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "Constructor of type"}
   {:db/ident :app.type/type
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "Type of type"}])

(def schema-constructor
  [{:db/ident :app.constructor/symbol
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/value
    :db/doc "Symbol of constructor"}
   {:db/ident :app.constructor/args
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/doc "Args of constructor"}])

(def type-of-type
  {:constructor {:symbol "Type"}})

(def true-constructor
  {:symbol "True"})

(def false-constructor
  {:symbol "False"})

(def bool-type
  {:type type-of-type
   :constructors
   [true-constructor
    false-constructor]})

(def true-term
  {:type bool-type
   :constructor true-constructor})

(def false-term
  {:type bool-type
   :constructor false-constructor})

(def arrow-constructor
  {:symbol "->"
   :args
   []})
