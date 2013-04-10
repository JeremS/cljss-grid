(ns cljss.grid
  (:require [cljss.box :as box]
            [cljss.data.length :as len]
            [clojure.algo.generic.arithmetic :as gen]))


(defrecord Grid [unit columns column gutter])

(defn grid [unit columns column gutter]
  (Grid. unit columns column gutter))

(def default-grid
  (grid len/px 24 30 10))

(defn total-width
  "Gives the total width of a the grid."
  [{:keys [unit columns column gutter]}]
  (unit (* columns (+ column gutter))))


(defn width [{:keys [unit column gutter]} span]
  (unit (if (zero? span)
    0
     (- (* span (+ column gutter)) gutter))))




