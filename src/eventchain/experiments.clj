(ns eventchain.experiments
  (:require [clojure.string :as str]
            [clojure.spec :as s]
            [datomic.api :as d]
            [eventchain.types :refer :all]
            [eventchain.events :refer :all]
            [eventchain.rules :refer :all]))

(defn fresh-database []
  (let [db-name (gensym)
        db-uri (str "datomic:mem://" db-name)]
    (d/create-database db-uri)
    (let [conn (d/connect db-uri)]
      @(d/transact conn simple-type-schema)
      @(d/transact conn event-schema)
      @(d/transact conn rule-schema)
      conn)))

(def conn (fresh-database))

(d/q '[:find ?n
       :where
       [_ :db/ident ?n]]
      (d/db conn))

(:tx-data @(d/transact conn simple-type-schema))

(:tx-data @(d/transact conn (type-new :MyType)))

(d/q '[:find ?n
       :where
       [_ :evch.event/type ?n]]
      (d/db conn))

(d/pull (d/db conn) '[*] [:evch.type/label :MyType])

(:tx-data @(d/transact conn (event-new :MyType)))

(d/q '[:find ?n
       :where
       [_ :evch.event/type ?n]]
      (d/db conn))
