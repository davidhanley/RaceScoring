(defproject racescoring "0.0.1"
  :description "TRUSA scoring system"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/data.csv "0.1.4"]
                 [org.clojure/data.json "0.2.6"]
                 ]

  :aot [scoring.core]
  :main scoring.races

   )
