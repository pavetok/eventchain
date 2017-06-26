(ns eventchain.db.terms.more)

(def schema-term
  [{:db/ident :app.term/symbol
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "Symbol of term"}
   {:db/ident :app.term/type
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "Type of term"}
   {:db/ident :app.term/constructors
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/doc "Constructors of type"}
   {:db/ident :app.term/components
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/doc "Components of type"}])

(def schema-arrow
  [{:db/ident :app.arrow/from
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "From type"}
   {:db/ident :app.arrow/to
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "To type"}])

;; Super Type

(def super-type
  {:symbol "Type"})

;; Bool Type

(declare bool-type)

(def true-term
  {:symbol "True"
   :type {:to bool-type}})

(def false-term
  {:symbol "False"
   :type {:to bool-type}})

(def bool-type
  {:symbol "Bool"
   :type {:to super-type}
   :constructors [true-term
                  false-term]})

;; String Type

(def string-type
  {:symbol "String"
   :type {:to super-type}})

;; Pet Type

(declare pet-type)

(def dog-term
  {:symbol "Dog"
   :type {:from string-type
          :to pet-type}})

(def pet-type
  {:symbol "Pet"
   :type {:to super-type}
   :constructors [dog-term]})

;; Composite Term

(def emma-term
  {:symbol "Emma"
   :type {:to string-type}})

(def pet-emma
  {:symbol "Dog Emma"
   :type {:to pet-type}
   :components [dog-term
              emma-term]})

;; Composite Type

(def list-term
  {:symbol "List"
   :type {:from super-type
          :to super-type}})

(def list-var
  {:symbol "ty"
   :type {:to super-type}})

(def list-nil-type
  {:symbol "List ty"
   :type {:to super-type}
   :components [list-term
                list-var]})

(def list-nil-term
  {:symbol "Nil"
   :type {:to list-nil-type}})

;; List String

(def list-string-type
  {:symbol "List String"
   :type {:to super-type}
   :components [list-term
                string-type]})

(def list-string-term
  {:symbol "Nil"
   :type {:to list-string-type}})

;; Function
(def id-eq-term
  {})

(def id-func-term
  {:symbol "id"
   :type {:from string-type
          :to string-type}
   :equations [id-eq-term]})

(def id-apply-term
  {:symbol "id Emma"
   :type {:to string-type}
   :components [id-func-term
                emma-term]})
