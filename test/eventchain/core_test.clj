(ns eventchain.core-test
  (:require [clojure.test :refer :all]
            [datomic.api :as d]
            [eventchain.core :refer :all]))

(def ^:dynamic *conn* nil)

(defn fresh-database []
  (let [db-name (gensym)
        db-uri (str "datomic:mem://" db-name)]
    (d/create-database db-uri)
    (let [conn (d/connect db-uri)]
      @(d/transact conn rule-schema)
      conn)))

(defn with-database [f]
  (binding [*conn* (fresh-database)]
    (f)))

(use-fixtures :each with-database)

(deftest create-database
  (is (not (nil? *conn*)))
  (is (not (nil? (d/db *conn*))))
  (is (= 0 (rule-count (d/db *conn*)))))

(deftest create-rule
  @(d/transact *conn* (rule-new "my-rule"))
  (is (= 1 (rule-count (d/db *conn*)))))
