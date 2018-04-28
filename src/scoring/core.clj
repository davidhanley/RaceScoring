(ns scoring.core
  (:require [clojure.string :as string])
  (:require [clojure.data.json :as json])
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))


(defn l [] (use 'scoring.core :reload))


(defn parse-time-or-nil [time]
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

; (defn int-or-nil [key data]
;  (try {key (Integer. data)} (catch Exception e nil)))

(defn parse-age-to-birth-year [age race-date]
  (try {:birth-year (- (first race-date) (Integer. age))}
       (catch Exception e nil)))

; TODO: flip it when the format is last, first
(defn row-to-athlete-result [row race-date]
  "turn a row into an athlete object"
  (let [itm (comp string/trim (list-getter row))]
    (merge {:name (itm 1)}
           (parse-age-to-birth-year (itm 2) race-date)
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
        race-date (map #(Integer. %) (string/split (itm 1) #"-"))
        racers (map merge (map #(row-to-athlete-result % race-date) (drop 4 data)) (ranking-list :overall-rank))
        score-list (scores points)
        sexer (fn [sex rank-key] (doall                     ; force this to happen since it's in a with-file
                                   (map #(dissoc % :sex)
                                        (map merge
                                             (filter (fn [athlete] (= (:sex athlete) sex)) racers)
                                             score-list
                                             (ranking-list rank-key)))))
        ]
    {:name          (itm 0)
     :date          race-date
     :url           (itm 2)
     :points        points
     :male-racers   (sexer :male :male-rank)
     :female-racers (sexer :female :female-rank)
     :race-id       race-id
     }))

(defn load-race-data [filename id]
  (with-open [in-file (io/reader filename)]
    (to-race-struct (csv/read-csv in-file) id)))

(defn new-or-newer [fns]
  (let [timestamps (map #(.lastModified (java.io.File. %)) fns)]
    (or (zero? (second timestamps)) (> (first timestamps) (second timestamps)))))

(defn process-race-data [filename id]
  (when (string/ends-with? filename ".csv")
    (let [target-file (str filename ".json")]
      (when (new-or-newer [filename target-file])
        (println "processing " filename)
        (with-open [out-file (io/writer target-file)]
          (binding [*out* out-file]
            (json/pprint (load-race-data filename id))))))))

(defn process-all-races []
  (map process-race-data (map str (rest (file-seq (java.io.File. "data")))) (range)))

(defn races [] (process-all-races))

