(ns eventchain.terms.core
  (:require [clojure.string :as str]))

;; terms:
;; - string (arrow from unit to string)
;; - arrow!!!
;; - dependent
;; - constructor
;; - container

;; keys:
;; - db/ident
;; - uuid
;; - IRI
;; - fully qualified name
;; - hashcode
;; - mnemonics

;; names:
;; - human readable
;; - machine readable

(def terms
  {:Type {:fqn "core.Type"
          :type [:Type]}
   :Unit {:fqn "core.Unit"
          :type [:Type]}
   :Bool {:fqn "core.Bool"
          :type [:Type]
          :constructors [:True :False]}
   :True {:fqn "core.True"
          :type [:Bool]}
   :False {:fqn "core.False"
           :type [:Bool]}
   :String {:fqn "core.String"
            :type [:Type]}

   :Pet {:fqn "my.Pet"
         :type [:Type]
         :constructors [:Dog]}
   :Dog {:fqn "my.Dog"
         :type [:String :Pet]}
   :emma {:type [:Pet]
          :components [:Dog "Emma"]}

   :Shape {:fqn "my.Shape"
           :type [:Type]
           :constructors [:Circle :Rectangle]}
   :Circle {:fqn "my.Circle"
            :type [:Nat :Shape]}
   :Rectangle {:fqn "my.Rectangle"
               :type [:Nat :Nat :Shape]}
   :rectangle {:type [:Shape]
               :components [:Rectangle 1 2]}

   :Status {:fqn "ps.Status"
            :type [:Type]
            :constructors [:Studying]}
   :Studying {:fqn "ps.Studying"
              :type [:Status]}
   :CLM {:fqn "ps.CLM"
         :type [:Status :CLM]}
   :CLM-Studying {:type [:Type]
                  :components [:CLM :Studying]}})

