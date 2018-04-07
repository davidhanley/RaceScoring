(ns scoring.core-test
  (:require [clojure.test :refer :all]
            [scoring.core :refer :all]
            [clojure.java.io :as io]
            [clojure.data.csv :as csv]))

(deftest test-int-parsing
  (testing "bad int parses should be nil"
    (is (= (int-or-nil :age "5") {:age 5}))
    (is (= (int-or-nil :age "dave") nil))))

(deftest test-gender-parsing
  (testing "if gender parsing works "
    (is (= (to-sex "m") {:sex :male}))
    (is (= (to-sex "f") {:sex :female}))
    (is (= (to-sex "*f") {:sex :female :foreign true}))
    (is (= (to-sex "*m") {:sex :male :foreign true}))
    )
  )

(deftest row-parse-test
  (testing "if row parsing works"
    (is (= (row-to-athlete-result ["", "dave", "42", "m"]) {:name "dave", :age 42, :sex :male}))
    (is (= (row-to-athlete-result ["", "dave", "42", "*m"]) {:name "dave", :age 42, :sex :male :foreign true}))
    (is (=
          (row-to-athlete-result ["1", "Piotr Lobodzinski", "", "*M", "10:31:00 AM", "Bielsk Podlaski, WARSAW", "4Flex/Adidas,Details"])
          {:name "Piotr Lobodzinski", :sex :male, :foreign true}
          ))
    ))


(def esbru (load-race-data "data/2017-esbru.csv" 666))

(def esbru-males (:male-racers esbru))
(def esbru-females (:female-racers esbru))
(def first-esbru-male (first esbru-males))
(def fourth-esbru-male (nth esbru-males 3))
(def first-esbru-female (first esbru-females))
(def second-esbru-female (second esbru-females))

(deftest parse-2017-empire
  (testing "see if i parse this race header right"
    (is (= (:name esbru) "2017 empire state run up"))
    (is (= (:points esbru) 200)))

  (testing "did i get the males right (counting foreigners"
    (is (= first-esbru-male {:name "Piotr Lobodzinski", :foreign true, :overall-rank 1, :score 200, :male-rank 1}))
    (is (= fourth-esbru-male {:name "Sproule Love", :overall-rank 4, :score 125, :male-rank 4})))

  (testing "did i get the females right (counting foreigners"
    (is (= first-esbru-female {:name "Suzy Walsham", :foreign true, :overall-rank 9, :score 200, :female-rank 1}))
    (is (= second-esbru-female {:name "Cindy Harris", :overall-rank 15, :score 500/3, :female-rank 2}))
    )


  )