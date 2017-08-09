(ns eventchain.experiments
  (:require [clojure.string :as str]
            [clojure.template :as t]
            [clojure.walk :as w]
            [datomic.api :as d]
            [eventchain.types :refer :all]
            [eventchain.events :refer :all]
            [eventchain.morphism :refer :all]))

(defn fresh-database
  []
  (let [db-name (gensym)
        db-uri (str "datomic:mem://" db-name)]
    (d/create-database db-uri)
    (let [conn (d/connect db-uri)]
      @(d/transact conn type-schema)
      @(d/transact conn event-schema)
      @(d/transact conn my-schema)
      conn)))

(def conn (fresh-database))

(d/q '[:find ?n
       :where
       [_ :db/ident ?n]]
     (d/db conn))

;; types

(:tx-data @(d/transact conn [{:type "OrderCreated"}]))
(:tx-data @(d/transact conn [{:type "OrderAccepted"}]))

(d/q '[:find ?n .
       :where
       [_ :type ?n]]
     (d/db conn))

(d/pull (d/db conn) '[*] [:type "OrderCreated"])

;; events

(:tx-data @(d/transact conn [{:event/type [:type "OrderCreated"]}]))

(d/q '[:find ?n .
       :where
       [?e :event/type ?n]]
     (d/db conn))

;; morphisms

(def mrphsm
  {:morphism/head
   {:head/patterns
    [{:pattern/entity "?event"
      :pattern/attr :event/type
      :pattern/value [:type "OrderAccepted"]}]}
   :morphism/body
   {:body/patterns
    [{:pattern/entity "?event"
      :pattern/attr :event/type
      :pattern/value [:type "OrderCreated"]}]}})

(:tx-data @(d/transact conn [mrphsm]))

(d/q '[:find [?morphism ...]
       :in $ ?type
       :where
       [?morphism :morphism/body ?body]
       [?body :body/patterns ?pattern]
       [?pattern :pattern/attr :event/type]
       [?pattern :pattern/value ?type]]
     (d/db conn) [:type "OrderCreated"])

(def facts
  (d/q '[:find ?entity ?attr ?value
         :in $ ?type
         :where
         [?morphism :morphism/body ?body]
         [?body :body/patterns ?body-pattern]
         [?body-pattern :pattern/attr :event/type]
         [?body-pattern :pattern/value ?type]
         [?morphism :morphism/head ?head]
         [?head :head/patterns ?head-pattern]
         [?head-pattern :pattern/entity ?entity]
         [?head-pattern :pattern/attr ?attr]
         [?head-pattern :pattern/value ?value]]
       (d/db conn) [:type "OrderCreated"]))

(d/pull (d/db conn) '[*] 17592186045426)
(d/pull (d/db conn) '[:morphism/head :head/pattern] 17592186045426)

(:tx-data @(d/transact conn [[:db/add "type-id" :type "TestType"]]))

(:tx-data @(d/transact conn (mapv #(into [:db/add] %) facts)))

(d/q '[:find (pull ?e [*])
       :where
       [?e :type _]]
     (d/db conn))

(d/q '[:find (pull ?e [*])
       :where
       [?e :event/type _]]
     (d/db conn))

(t/apply-template
 '[?event]
 '[["?event" :some "value"]]
 '[12])

(def rules
  '[[(vars-rule ?pattern ?var)
     [?pattern :pattern/entity ?var]
     [?pattern :pattern/attr ?var]
     [?pattern :pattern/value ?var]]])

(def body-vars
  (d/q '[:find [?variable ...]
         :in $ ?type
         :where
         [?morphism :morphism/body ?body]
         [?body :body/patterns ?pattern]
         [?pattern _ ?value]
         [(string? ?value)]
         [(.startsWith ^String ?value "?")]
         [(symbol ?value) ?variable]]
       (d/db conn) [:type "OrderCreated"]))

(def body-patterns
  (d/q '[:find ?entity ?attr ?value
         :in $ ?type
         :where
         [?morphism :morphism/body ?body]
         [?body :body/patterns ?pattern]
         [?pattern :pattern/entity ?entity]
         [?pattern :pattern/attr ?attr]
         [?pattern :pattern/value ?value]]
       (d/db conn) [:type "OrderCreated"]))

(defn symbolize
  [fact]
  (mapv #(if (str/starts-with? % "?") (symbol %) %) fact))

(def symbolized-body-patterns
  (mapv symbolize body-patterns))

(def body-vars-values
  (d/q {:find [body-vars]
         :in '[$ ?type]
         :where symbolized-body-patterns}
       (d/db conn) [:type "OrderCreated"]))

(def head-patterns
  (d/q '[:find ?entity ?attr ?value
         :in $ ?type
         :where
         [?morphism :morphism/head ?head]
         [?head :head/patterns ?pattern]
         [?pattern :pattern/entity ?entity]
         [?pattern :pattern/attr ?attr]
         [?pattern :pattern/value ?value]]
       (d/db conn) [:type "OrderCreated"]))

(def symbolized-head-patterns
  (mapv symbolize head-patterns))

(t/apply-template body-vars symbolized-head-patterns body-vars-values)

(w/postwalk-replace (zipmap body-vars body-vars-values) symbolized-head-patterns)
