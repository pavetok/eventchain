(ns eventchain.db.object.core
  (:require [clojure.string :as str]
            [datomic.api :as d]))

(def schema-object
  [{:db/ident :app.object/point
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "Object point"}])
