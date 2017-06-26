(defproject eventchain "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha16"]
                 [com.datomic/datomic-free "0.9.5561"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [org.clojure/test.check "0.9.0"]]
  :main ^:skip-aot eventchain.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
