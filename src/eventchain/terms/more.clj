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
  {:Type {:ns :core
          :type [:Type]
          :term [:Type]
          :symbol "Type"}
   :Unit {:ns :core
          :type [:Type]
          :term [:Unit]
          :symbol "Unit"}
   :Bool {:ns :core
          :type [:Type]
          :term [:Bool]
          :symbol "Bool"
          :constructors [:True :False]}
   :True {:ns :core
          :type [:Bool]
          :term [:True]
          :symbol "True"}
   :False {:ns :core
           :type [:Bool]
           :term [:False]
           :symbol "False"}
   :String {:ns :core
            :type [:Type]
            :term [:String]
            :symbol "String"}

   :Pet {:ns :my
         :type [:Type]
         :term [:Pet]
         :symbol "Pet"
         :constructors [:Dog]}
   :Dog {:ns :my
         :type [:String :Pet]
         :term [:Dog]
         :symbol "Dog"}
   :Emma {:ns :my
          :type [:Pet]
          :term [:Dog "Emma"]}

   :Shape {:ns :my
           :type [:Type]
           :term [:Shape]
           :symbol "Shape"
           :constructors [:Circle :Rectangle]}
   :Circle {:ns :my
            :type [:Nat :Shape]
            :term [:Circle]
            :symbol "Circle"}
   :Rectangle {:ns :my
               :type [:Nat :Nat :Shape]
               :term [:Rectangle]
               :symbol "Rectangle"}
   :rectangle {:ns :my
               :type [:Shape]
               :term [:Rectangle 1 2]}

   ;; CLM status
   :Status {:ns :ps
            :type [:Type]
            :term [:Status]
            :symbol "Status"
            :constructors [:Studying]}
   :Studying {:ns :ps
              :type [:Status]
              :term [:Studying]
              :symbol "Studying"}
   ;; CLM type
   :ClmType {:ns :ps
             :type [:Type]
             :symbol "ClmType"
             :constructors [:Error]}
   :type {:type [:ClmType]
          :symbol "type"}
   :Error {:ns :ps
           :type [:ClmType]
           :symbol "Error"}
   ;; CLM
   :CLM {:ns :ps
         :type [:Status :ClmType :CLM]
         :term [:CLM]
         :symbol "CLM"}
   :CLM-s-e {:ns :ps
             :type [:Type]
             :term [:CLM :Studying :Error]}
   ;; Transition
   :Transition {:type [:Type :Type :Type]
                :symbol "Transition"
                :constructors [:Simple :Composite]}
   :Simple {:type [[:Transition :a :b]]
            :symbol "Simple"}
   :Composite {:type [[:Transition :a :b]
                      [:Transition :b :c]
                      [:Transition :a :c]]
               :symbol "Composite"}
   :CLM-Studying-type {:type [:Type]
                       :term [:CLM :Studying :type]}
   :CLM-Received-type {:type [:Type]
                       :term [:CLM :Received :type]}
   :CLM-CorrectionIsDone-type {:type [:Type]
                               :term [:CLM :CorrectionIsDone :type]}
   :Analyse {:type [[:Transition :CLM-Studying-type :CLM-Received-type]]
             :symbol "Analyse"}
   :Implement {:type [[:Transition :CLM-Received-type :CLM-CorrectionIsDone-type]]
               :symbol "Implement"}
   :Foo {:type [[:Transition :CLM-Studying-type :CLM-CorrectionIsDone-type]]
         :symbol "Foo"
         :term [:Composite :Analyse :Implement]}})

