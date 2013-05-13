(ns cljss.grid-test
  (:require [cljss.units.length :refer (px)]
            [clojure.algo.generic.arithmetic :as gen])
  (:use cljss.grid
        midje.sweet))


(fact "We compute the width of a box given its column span"
  (width default-grid 1)
  => (px 30)

  (width default-grid 10)
  => (gen/+ (gen/* 10 (px 30))
            (gen/* 9 (px 10))))


