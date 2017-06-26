(ns eventchain.db.space.more)

(def super-type
  {:symbol "Type"})

(def string-type
  {:symbol "String"
   :type {:to super-type}})

(def string-term
  {:symbol "i-am-a-tile"
   :type {:to string-type}})

;; Simple Tile Type

(def tile-term
  {:symbol "MkTile"
   :type {:from string-type
          :to super-type}})

(def tile-type
  {:symbol "Tile"
   :type {:to super-type}
   :constructors [tile-term]})

(def some-tile
  {:symbol "Tile i-am-a-tile"
   :type {:to tile-type}
   :components [tile-term
                string-term]})
