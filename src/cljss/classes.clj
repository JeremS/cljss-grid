(ns cljss.grid.classes
  (:require [clojure.algo.generic.arithmetic :as gen]
            [cljss.grid :as grid])
  (:use cljss.core))

;; Adaptation of the 24 columns 960 grid framework

(def width (partial grid/width grid/default-grid ))

(def general-width (partial grid/general-width grid/default-grid ))

(def make-column-width (partial grid/make-column-width grid/default-grid))

(def push (partial grid/push grid/default-grid))
(def pull (partial grid/pull grid/default-grid))
(def prefix (partial grid/prefix grid/default-grid))
(def suffix (partial grid/suffix grid/default-grid))

(def column-mixin (grid/make-column grid/default-grid))


(defn make-classes [base start  stop]
  (for [n (range start (inc stop))]
    (str \. base n)))

(def column-classes
  (make-classes "col" 0 24))

(def push-classes
  (make-classes "push" 1 23))

(def pull-classes
  (make-classes "pull" 1 23))

(def prefix-classes
  (make-classes "prefix" 1 23))

(def suffix-classes
  (make-classes "suffix" 1 23))


(defrules grid-rules
  [:body :min-width (general-width 24)]
  (css-comment "generated grid classes")

  [:.container_24 grid/container-mixin]

  [(set column-classes) column-mixin]

  [:.alpha grid/alpha]
  [:.omega grid/omega]


  [(set (concat push-classes pull-classes))
   grid/push-pull-style]


  [:.container_24
   (map #(vector %1 (make-column-width %2))
        column-classes
        (range))

   [:.col0 :display :none]


   (map #(vector %1 (push %2))
        push-classes
        (range 1 100))

   (map #(vector %1 (pull %2))
        pull-classes
        (range 1 100))

   (map #(vector %1 (prefix %2))
        prefix-classes
        (range 1 100))

   (map #(vector %1 (suffix %2))
        suffix-classes
        (range 1 100))]

  [#{:.clearfix :.container_24} grid/clearfixed]

  )



(def output-dir (str (System/getProperty "user.dir")
                     (System/getProperty "file.separator")
                     "css"
                     (System/getProperty "file.separator")))

(def css-file (str output-dir "style.css"))


(spit css-file (apply compact-css grid-rules))


