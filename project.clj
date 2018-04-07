(defproject racescoring "0.0.1"
  :description "TRUSA scoring system"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.csv "0.1.4"]
                 [clj-time "0.7.0"]
                 [org.clojure/core.memoize "0.5.6"]
                 ]

  :aot [scoring.core]
  :main scoring.core

   )
