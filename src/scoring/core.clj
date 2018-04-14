(ns scoring.core
  (:require [clojure.string :as string])
  (:require [clojure.data.json :as json])
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))


(defn l [] (use 'scoring.core :reload))

(defn int-or-nil [key data]
  (try {key (Integer. data)} (catch Exception e nil)))


(defn parse-time-or-nil [time] time
  (try
    {:time (let [[intpart dec] (string/split time #"\.")
                 [ss mm hh] (reverse (map #(Integer. %) (string/split intpart #":")))
                 [hours minutes seconds] (map #(if (nil? %) 0 %) [hh mm ss])]
             (+ (* 60 60 hours) (* 60 minutes) seconds (Float. (string/join "" ["0." dec]))))}
    (catch Exception e nil))
  )

(defn to-sex
  "parse the gender from the row, and also foreign status if present"
  [data]
  (let [d (string/upper-case data)
        sw (fn [p] (string/starts-with? d p))]
    (cond
      (sw "M") {:sex :male}
      (sw "F") {:sex :female}
      (sw "*M") {:sex :male :foreign true}
      (sw "*F") {:sex :female :foreign true})))

(defn list-getter [lst]
  #(try (nth lst %) (catch Exception e "")))

; TODO: flip it when the format is last, first 
(defn row-to-athlete-result [row]
  "turn a row into an athlete object"
  (let [itm (comp string/trim (list-getter row))]
    (merge {:name (itm 1)}
           (int-or-nil :age (itm 2))
           (to-sex (itm 3))
           (parse-time-or-nil (itm 4))
           )))

(defn scores [base]
  "an infinite list of descending scores per rank starting with the base score"
  (map (fn [i] {:score (* base (/ 5 (+ 5 i)))}) (range)))

(defn ranking-list
  "an infinite list of ranks, used for females, males, and overall"
  [key] (map (fn [i] {key (+ i 1)}) (range)))

(defn strip-comment
  "Some lines had # comments, strip those out"
  [str]
  (string/trim (first (string/split str #"#"))))

(defn to-race-struct [data race-id]
  (let [itm (fn [i] (string/trim (get (nth data i) 0)))
        points (Integer. (strip-comment (itm 3)))
        racers (map merge (map row-to-athlete-result (drop 4 data)) (ranking-list :overall-rank))
        score-list (scores points)
        sexer (fn [sex rank-key] (map #(dissoc % :sex)
                                      (map merge (filter (fn [athlete] (= (:sex athlete) sex)) racers) score-list
                                                 (ranking-list rank-key))))
        ]
    {:name          (itm 0)
     ;:date          (apply t/date-time (map #(Integer. %) (string/split (itm 1) #"-")))
     :date          (itm 1)
     :url           (itm 2)
     :points        points
     :male-racers   (sexer :male :male-rank)
     :female-racers (sexer :female :female-rank)
     :race-id       race-id
     }))

(defn load-race-data [fn id]
  (when (string/ends-with? fn ".csv")
    (println "processing " fn)
    (with-open [in-file (io/reader fn)]
      (with-open [out-file (io/writer (str fn ".json"))]
        (let [rd (to-race-struct (csv/read-csv in-file) id)]
          (binding [*out* out-file]
            (json/pprint rd)))))))


(defn load-all-races []
  (map load-race-data (rest (file-seq (java.io.File. "data"))) (range)))

(defn races [] (load-all-races))

