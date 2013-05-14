(ns cljss.page-test
  (:require [clojure.algo.generic.arithmetic :as gen])
  (:use cljss.core
        cljss.grid
        [hiccup.page :only (html5 include-css)]))

;; css generation

(def w (partial width default-grid ))
(def gw (partial general-width default-grid ))
(def col (partial column default-grid))
(def offset (partial push-offset default-grid))

(defn make-classes [base start  stop]
  (for [n (range start (inc stop))]
    (str \. base n)))

(def column-classes
  (make-classes "col" 0 24))

(def push-classes
  (make-classes "push" 1 23))

(def pull-classes
  (make-classes "pull" 1 23))


(defrules rules1
  (css-comment "general")
  [:#container
   :width (gw 24)]

  (css-comment "page specific")
  [:section
   [:div
    container-mixin
    :margin-bottom :5px
    [:div :text-align :center]]]

  [[:.etalon :div :div]
   :background-color :gray]

  [[:#ex1 :div :div]
   [(-> & (nth-child :even)) :background-color :red]
   [(-> & (nth-child :odd)) :background-color :blue]]

  [[:#ex2 :div :div]
   :box-sizing :border-box
   :border [:1px :solid :blue]]

  [[:#ex3 :div :div]
   :box-sizing :border-box
   :border [:1px :solid :blue]]

  (css-comment "generated grid classes")
  [(set column-classes) column-mixin]
  (map #(vector %1 (col %2) )
       column-classes
       (range))

  [:.col0 :display :none]

  [(set (into push-classes pull-classes))
   :position :relative]

  (map #(vector %1 :left (offset %2))
       push-classes
       (range 1 100))

  (map #(vector %1 :left (gen/- (offset %2)))
       pull-classes
       (range 1 100)))



;; html generation

(def seq1 (for [n (range 25)]
            [n (- 24 n)]))


(defn make-div1 [n]
  [:div {:class (str "col" n)}
        (str (w n))])

(defn make-row1 [[a b]]
  [:div (make-div1 a)
   (make-div1 b)])

(def section-etalon
  [:section.etalon
   [:p "Grid:"]
   [:div
    (for [n (range 1 25)]
      [:div.col1 (str n)])]])

(def section1
  [:section#ex1
   [:p "The differents sizes: "]
   (map make-row1 seq1)])

(def section2
  [:section#ex2
   [:p "Example of push/pull: "]
   [:div
    [:div.col8 "First in html"]
    [:div.col8 "Second in html"]
    [:div.col8 "Third in html"]]
   [:div
    [:div.col8.push16 "First in html"]
    [:div.col8 "Second in html"]
    [:div.col8.pull16 "Third in html"]]])

(def section3
  [:section#ex3
   (for [n (range 1 24)]
     [:div
      [:div {:class (str "col1 " "push" n)} (str "p" n)]])])

(defn make-page [& body]
  (html5
   [:head (include-css "./style.css")]
   [:body
    [:div#container body]]))


(def output-dir (str (System/getProperty "user.dir")
                     (System/getProperty "file.separator")
                     "example"
                     (System/getProperty "file.separator")))

(def css-file (str output-dir "style.css"))
(def html-file (str output-dir "index.html"))


(spit css-file (apply css rules1))
(spit html-file (make-page section-etalon
                           section1
                           section-etalon
                           section2
                           section-etalon
                           section3))

