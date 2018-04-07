(ns scoring.core-test
  (:require [clojure.test :refer :all]
            [scoring.core :refer :all]))

(deftest test-int-parsing
  (testing "bad int parses should be nil"
    (is (= (int-or-nil :age "5") {:age 5}))
    (is (= (int-or-nil :age "dave") nil))))




