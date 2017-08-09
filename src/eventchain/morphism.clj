(ns eventchain.morphism
  (:require [clojure.string :as str]
            [datomic.api :as d]))

; spec
; (s/def :evch/morphism
;   (s/cat :head :evch.morphism/predicate
;          :delimiter keyword?
;          :body (s/+ :evch.morphism/predicate)))
;
; (s/def :evch.morphism/predicate
;   vector?)
;
; (def morphism
;   '[[?a :uncle ?c] :- [?a :parent ?b] [?b :brother ?c]])
;
; (def morphism
;   '[[?new :event/type [:type "OrderAccepted"]]
;     [?new :event/order ?order]
;     :-
;     [?old :event/type [:type "OrderCreated"]]
;     [?old :event/order ?order]])
;
; (s/conform :evch/morphism morphism)
; (s/explain :evch/morphism morphism)

; schema

(def morphism-schema
  [{:db/ident :morphism/head
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/isComponent true}
   {:db/ident :morphism/body
     :db/valueType :db.type/ref
     :db/cardinality :db.cardinality/one
     :db/isComponent true}])

(def head-schema
  [{:db/ident :head/patterns
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/isComponent true}])

(def body-schema
  [{:db/ident :body/patterns
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/isComponent true}])

(def pattern-schema
  [{:db/ident :pattern/entity
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   {:db/ident :pattern/attr
     :db/valueType :db.type/ref
     :db/cardinality :db.cardinality/one}
   {:db/ident :pattern/value
     :db/valueType :db.type/ref
     :db/cardinality :db.cardinality/one}])

(def my-schema
  (vec
    (concat
      morphism-schema
      head-schema
      body-schema
      pattern-schema)))
