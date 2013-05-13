(ns cljss.box
  (:require [cljss.units.length :as len]
            [clojure.algo.generic.arithmetic :as gen])
  (:use clojure.tools.trace))



(def default-box (sorted-map))

(def ^:private box-properties-names
  #{:border :padding :margin :width :height})

(def ^:private box-sels
  #{:left :right :top :bottom
    :tb
    :lr
    :wh
    :all})

(defn- property [n sel]
  (keyword (str (name n) "-" (name sel))))



(defmulti ^:private mm-resize
  (fn [box prop sel new-size]
    (if (coll? sel) :sels sel)))

(defmethod mm-resize :default [box prop sel new-size]
  (assoc box (property prop sel) new-size))

(defmethod mm-resize :wh [box prop sel new-size]
  (assoc box prop new-size))

(defmethod mm-resize :sels [box prop sel new-size]
  (reduce #(mm-resize %1 prop %2 new-size) box sel))

(defmethod mm-resize :tb [box prop sel new-size]
  (mm-resize box prop [:top :bottom] new-size))

(defmethod mm-resize :lr [box prop sel new-size]
  (mm-resize box prop [:left :right] new-size))

(defmethod mm-resize :all [box prop sel new-size]
  (mm-resize box prop [:lr :tb] new-size))


(def ^:private prop? box-properties-names)

(defn- sel? [sel]
  (or (box-sels sel)
      (and (coll? sel)
           (every? box-sels sel))))

(defn resize
  "Changes the a size of a box."
  ([box prop new-size]
   (resize box prop :wh new-size))
  ([box prop sel new-size]
   (when-not (and (prop? prop)
                  (sel? sel))
     (throw (ex-info (str "Can't resize for " prop " or " sel " unknown") {})))
   (mm-resize box prop sel new-size)))
