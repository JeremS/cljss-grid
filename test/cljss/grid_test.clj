(ns cljss.grid-test
  (:require [midje.sweet :as m]
            [cljss.data.length :as len]
            [clojure.algo.generic.arithmetic :as gen])
  (:use cljss.grid))


(def grid-960-24 
  (grid len/px 24 30 10))

(def grid-960-12
  (grid len/px 12 60 20))

(m/fact "We can know the max width of a page give a grid"
        (total-width grid-960-24) => (len/px 960)
        (total-width grid-960-12) => (len/px 960))

(m/fact "We can calculate the width of a column given a grid and the number 
        of grid coluùn the column span on"
        
        (width grid-960-24 0) => (len/px 0)
        (width grid-960-12 0) => (len/px 0)
        
        (width grid-960-24 1) => (len/px 30)
        (width grid-960-12 1) => (len/px 60)
        
        (width grid-960-24 4) => (len/px 150)
        (width grid-960-12 4) => (len/px 300))
