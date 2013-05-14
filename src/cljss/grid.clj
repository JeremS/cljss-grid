(ns cljss.grid
  (:require [cljss.box :as box]
            [cljss.units.length :as len]
            [clojure.algo.generic.arithmetic :as gen])
  (:use cljss.core
        cljss.box))


;; inspired by https://github.com/nathansmith/960-Grid-System/blob/master/code/css/960_12_col.css

(defn- half [something]
  (gen// something 2))


(defn make-grid [unit column gutter]
  {:unit unit
   :column (unit column)
   :gutter (unit gutter)})

(def default-grid
  (make-grid len/px 30 10))

(defn general-width [{:keys [unit column gutter]} span]
  (if (zero? span)
    0
    (gen/* span (gen/+ column gutter))))

(defn width [{:keys [unit column gutter]} span]
  (if (zero? span)
    0
    (gen/- (gen/* span (gen/+ column gutter)) gutter)))



(defn push [grid box n]
  (assoc box
    :position :relative
    :left (width grid n)))

(defn pull [grid box n]
   (gen/- (push grid box n)))

(defn prefix [grid box n]
  (resize box :padding :left (width grid n)))

(defn suffix [grid box n]
  (resize box :padding :right (width grid n)))


(defn column [{g :gutter :as grid} span & {->p :push <-p :pull pre :prefix suf :suffix}]
  (when (and ->p <-p)
    (throw (ex-info "Can't push and pull or at the same time." {})))
  (cond-> default-box
          true (resize :width (width grid span))
          true (resize :margin :lr (half g))
          ->p ((partial push grid) ->p)
          <-p ((partial pull grid) <-p)
          pre ((partial prefix grid) pre)
          suf ((partial suffix grid) suf)))

(def column-mixin
  (assoc default-box
    :float :left
    :display :inline))


(def clearer
  '(:clear :both
    :display :block
    :overflow :hidden
    :visibility :hidden
    :width 0
    :height 0))

(defrules clearfixed
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

(def container-mixin
  (list
   :overflow :hidden
   :margin-left :auto
   :margin-right :auto
   clearfixed))

