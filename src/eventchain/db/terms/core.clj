(ns eventchain.db.terms.core
  (:require [clojure.string :as str]
            [datomic.api :as d]))

(def schema-term
  [{:db/ident :app.term/value
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "Value of term"}
   {:db/ident :app.term/type
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "Type of term"}])

(def schema-type
  [{:db/ident :app.type/term
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "Term of type"}
   {:db/ident :app.type/constructors
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/doc "Constructors of type"}
   {:db/ident :app.type/from
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "From type"}
   {:db/ident :app.type/to
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "To type"}])

;; Super Type

(def super-type
  {:value "Super"})

;; Bool Type

(declare bool-type)

(def bool-term
  {:value "Bool"
   :type super-type})

(def true-term
  {:value "True"
   :type bool-term})

(def false-term
  {:value "False"
   :type bool-term})

(def bool-type
  {:term bool-term
   :constructors [true-term
                  false-term]})

;; Pet Type

(declare pet-type)

(def string-term
  {:value "String"
   :type super-type})

(def dog-term
  {:value "String -> Pet"
   :type super-type})

(def pet-term
  {:value "Pet"
   :type super-type})

(def dog-type
  {:term dog-term
   :from string-term
   :to pet-term})

(def dog-constructor
  {:value "Dog"
   :type dog-type})

(def pet-type
  {:term pet-term
   :constructors [dog-constructor]})

