(ns eventchain.core-test
  (:require [clojure.test :refer :all]
            [datomic.api :as d]
            [eventchain.core :refer :all]
            [eventchain.types :refer :all]
            [eventchain.events :refer :all]
            [eventchain.rules.db.schema :refer :all]))

(def ^:dynamic *conn* nil)

(defn fresh-database
  []
  (let [db-name (gensym)
        db-uri (str "datomic:mem://" db-name)]
    (d/create-database db-uri)
    (let [conn (d/connect db-uri)]
      @(d/transact conn type-schema)
      @(d/transact conn event-schema)
      @(d/transact conn rule-schema)
      conn)))

(defn with-database
  [f]
  (binding [*conn* (fresh-database)]
    (f)))

(use-fixtures :each with-database)

(deftest create-database
  (is (not (nil? *conn*)))
  (is (not (nil? (d/db *conn*))))
  (is (= 0 (rule-count (d/db *conn*)))))

(deftest create-type
  @(d/transact *conn* (type-new :MyType))
  (is (= 1 (type-count (d/db *conn*)))))

(deftest create-event
  @(d/transact *conn* (type-new :MyType))
  @(d/transact *conn* (event-new :MyType))
  (is (= 1 (event-count (d/db *conn*)))))

(deftest create-rule
  @(d/transact *conn* (rule-new "my-rule"))
  (is (= 1 (rule-count (d/db *conn*)))))
