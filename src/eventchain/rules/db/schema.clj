(ns eventchain.rules.db.schema
  (:require [clojure.string :as str]
            [datomic.api :as d]))

(def rule-schema
  [{:db/ident :evch.rule/type
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "rule type"}
   {:db/ident :evch.rule/desc
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "rule description"}
   {:db/ident :evch.rule/head
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "rule head"}
   {:db/ident :evch.rule/body
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "rule body"}])

(def head-schema
  [{:db/ident :evch.head/predicate
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "head predicate"}])

(def body-schema
  [{:db/ident :evch.body/predicate
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/doc "body predicate"}])

(def predicate-schema
  [{:db/ident :evch.predicate/functor
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "predicate functor"}])

(def functor-schema
  [{:db/ident :evch.functor/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "functor name"}
   {:db/ident :evch.functor/argument
     :db/valueType :db.type/ref
     :db/cardinality :db.cardinality/many
     :db/doc "functor argument"}])

(def argument-schema
  [{:db/ident :evch.argument/constant
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "argument constant"}
   {:db/ident :evch.argument/variable
     :db/valueType :db.type/ref
     :db/cardinality :db.cardinality/one
     :db/doc "argument variable"}])

(def variable-schema
  [{:db/ident :evch.variable/name
     :db/valueType :db.type/string
     :db/cardinality :db.cardinality/one
     :db/doc "variable name"}])

(defn rule-new
  [name]
  [{:evch.rule/desc name}])

(defn rule-count
  [db]
  (count (d/q '[:find ?n
                :where
                [_ :evch.rule/desc ?n]]
              db)))
