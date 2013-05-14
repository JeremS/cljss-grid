(ns cljss.grid
  (:require [cljss.box :as box]
            [cljss.units.length :as len]
            [clojure.algo.generic.arithmetic :as gen])
  (:use cljss.core
        cljss.box

        clojure.repl))

;; inspired by https://github.com/nathansmith/960-Grid-System/blob/master/code/css/960_12_col.css

(def column-mixin
  "Positionnig mixin for column elements."
  {:float :left
   :display :inline})

(def clearer
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

(def container-mixin
  (list
   :overflow :hidden
   :margin-left :auto
   :margin-right :auto
   clearfixed))

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

(defn push-offset
  "Compute the lenght for a push or a pull."
  [grid n]
  (gen/+ (width grid n) (:gutter grid)))

(defn push
  "Generates a map mixin or assoc to one,
  the necessary css properties to push an element."
  ([grid n]
   (push grid default-box n))
  ([grid box n]
   (assoc box
     :position :relative
     :left (push-offset grid n))))

(defn pull
  "Generates a map mixin or assoc to one,
  the necessary css properties to pull an element."
  ([grid n]
   (pull grid default-box n))
  ([grid box n]
   (gen/- (push grid box n))))

(defn prefix
  "Generates a map mixin (or assoc to one),
  the necessary css properties to prefix an element.
  Prefixing allows to void space at the left of
  the prefixed element."
  ([grid n]
   (prefix grid default-box n))
  ([grid box n]
   (resize box :padding :left (width grid n))))

(defn suffix
  "Generates a map mixin (or assoc to one),
  the necessary css properties to suffix an element.

  Suffixing allows to void space at the right of the
  suffixed element."
  ([grid n]
   (suffix grid default-box n))
  ([grid box n]
   (resize box :padding :right (width grid n))))


(defn column
  "Generates a mixin specifing size and push/pull properties
  of an element given a grid and possibly some options:
   - :push see push fn
   - :pull see pull fn
   - :prefix see prefix fn
   - :suffix see suffix fn"
  [{g :gutter :as grid} span & {->p :push <-p :pull pre :prefix suf :suffix}]
  (when (and ->p <-p)
    (throw (ex-info "Can't push and pull at the same time." {})))
  (cond-> default-box
          true (resize :width (width grid span))
          true (resize :margin :lr (half g))
          ->p ((partial push grid) ->p)
          <-p ((partial pull grid) <-p)
          pre ((partial prefix grid) pre)
          suf ((partial suffix grid) suf)))

(alter-meta! #'column assoc :arglists '([grid span & opts]))

(defn sem-column
  "Returns the merging of the result of the column function
  with the default column mixin."
  [grid span & opts]
  (merge column-mixin
         (apply column grid span opts)))



