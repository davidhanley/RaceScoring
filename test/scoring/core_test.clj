(ns scoring.core-test
  (:require [clojure.test :refer :all]
            [scoring.core :refer :all]))

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


