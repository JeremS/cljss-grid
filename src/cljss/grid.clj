(ns cljss.grid
  (:require [cljss.units.length :as len]
            [clojure.algo.generic.arithmetic :as gen])
  (:use cljss.core))

;; inspired by https://github.com/nathansmith/960-Grid-System/blob/master/code/css/960_24_col.css

(def container-mixin
  "Mixin defining a row."
  {:overflow :hidden
   :margin-left :auto
   :margin-right :auto})

(def clear
  "Mixin rendering an element invisible with the property
  of 'clearing both'.

  To use on an element that will serve as an invisible grid row."
  '(:clear :both
    :display :block
    :overflow :hidden
    :visibility :hidden
    :width 0
    :height 0))

(defrules
  ^{:doc "Mixin used to enforce a 'grid row' behavior of an element."}
  clearfixed
  [(-> & after)
   :clear :both]

  [#{(-> & before) (-> & after)}
   :content (css-str \.)
   :display :block
   :overflow :hidden
   :visibility :hidden
   :font-size 0
   :line-height 0
   :width 0
   :height 0])


(def alpha
  "Mixin for the first column of an internal column."
  {:margin-left "0px"})

(def omega
  "Mixin for the last column of an internal column."
  {:margin-right "0px"})


(def ^:private gen-div (ns-resolve 'clojure.algo.generic.arithmetic '/))

(defn- half [something]
  (gen-div something 2))


(defn make-grid [unit column gutter]
  "Constructs a grid spec according to the parameters:
   - unit: a function that constructs a unit length
   - column: width of a column
   - gutter: width of a gutter"
  {:unit unit
   :column (unit column)
   :gutter (unit gutter)})

(def default-grid
  "A grid with 30px columns, 10px gutters."
  (make-grid len/px 30 10))

(defn general-width [{:keys [unit column gutter]} span]
  "Compute the total width of a grid."
  (if (zero? span)
    (unit 0)
    (gen/* span (gen/+ column gutter))))

(defn width
  "Compute the width of an element given the
  number of columns it spans on."
  [{g :gutter u :unit :as grid} span]
  (if (zero? span)
    (u 0)
    (gen/- (general-width grid span) g)))

(def push-pull-style
  "Default style of a pushable/pullable column."
  {:position :relative})

(defn push-offset
  "Compute the length of a push or a pull."
  [grid n]
  (gen/+ (width grid n) (:gutter grid)))

(defn push
  "Generates a map containing the push offeting style."
  [grid n]
  {:left (push-offset grid n)})

(defn pull
  "Generates a map containing the pull offeting style."
  [grid n]
  {:left (gen/- (push-offset grid n))})

(defn prefix
  "Generates a map mixin containing the necessary css
  properties to prefix an element.

  Prefixing allows to void space at the left of
  the prefixed element."
  [grid n]
  {:padding-left (width grid n)})

(defn suffix
  "Generates a map mixin containing the necessary css
  properties to suffix an element.

  Prefixing allows to void space at the right of
  the suffixed element."
  [grid n]
  {:padding-right (width grid n)})

(defn make-column [{g :gutter :as grid}]
  "Positionnig mixin for column elements."
  {:float :left
   :display :inline
   :margin-left (half g)
   :margin-right (half g)})


(defn make-column-width
  "Generates a mixin specifing the width property
  of an element given a grid and the number of grid columns
  the element spans onto."
  [grid span]
  {:width (width grid span)})

(def semantic-container-mixin
  "Add the container style to a semantic row."
  (list container-mixin clearfixed))

(defn semantic-column
  "Returns a mixin making an element a column spanning
  onto `span` grid columns."
  [grid span]
  (merge (make-column grid)
         (make-column-width grid span)))

(defn semantic-push [grid n]
  "Returns a mixin making an element pushed
  of `n`columns."
  (merge push-pull-style
         (push grid n)))

(defn semantic-pull [grid n]
  "Returns a mixin making an element pulled
  of `n`columns."
  (merge push-pull-style
         (pull grid n)))