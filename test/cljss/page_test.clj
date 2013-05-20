(ns cljss.page-test
  (:require [cljss.grid :as grid])
  (:use cljss.core
        [hiccup.page :only (html5 include-css)]))

(def width (partial grid/width grid/default-grid ))
(def general-width (partial grid/general-width grid/default-grid ))



(def seq1 (for [n (range 25)]
            [n (- 24 n)]))


(defn make-div1 [n]
  [:div {:class (str "col" n)}
        (str (width n))])

(defn make-row1 [[a b]]
  [:div.container_24
   (make-div1 a)
   (make-div1 b)])

(def section-etalon
  [:section
   [:h2 "Grid:"]
   [:div.container_24
    (for [n (range 1 25)]
      [:div.col1 (str n)])]])

(def section1
  [:section#ex1
   [:h2 "The differents sizes: "]
   (map make-row1 seq1)])

(def section2
  [:section#ex2
   [:h2 "Example of push/pull: "]
   [:div.container_24
    [:div.col8 "First in html"]
    [:div.col8 "Second in html"]
    [:div.col8 "Third in html"]]
   [:div.container_24
    [:div.col8.push16 "First in html"]
    [:div.col8 "Second in html"]
    [:div.col8.pull16 "Third in html"]]])

(def section3
  [:section#ex3
   [:h2 "Push range"]
   (for [n (range 1 24)]
     [:div.container_24
      [:div {:class (str "col1 " "push" n)} (str "p" n)]])])


(def section4
  [:section#ex4
   [:h2 "Prefix/suffix example:"]
   [:h3 "Prefix:"]
   (for [n (range 1 24)]
     [:div.container_24
      [:div {:class (str "col1 " "prefix" n)} (str "p" n)]])

   [:h3 "Suffix:"]
   (for [n (range 1 24)]
     [:div.container_24
      [:div {:class (str "col1 " "suffix" n)} (str "s" n)]])])

(defn make-page [& body]
  (html5
   [:head
    (include-css "../css/style.css")
    (include-css "./example-style.css")]
   [:body
    [:div#container body]]))


(defrules example-style
  (css-comment "Example style.")

  [:body
   :background-color :white
   :width (general-width 24)
   :margin-left :auto
   :margin-right :auto]

  [[:section :div :div]
   :margin-bottom :5px
   :background-color :grey
   :text-align :center])



(def output-dir (str (System/getProperty "user.dir")
                     (System/getProperty "file.separator")
                     "example"
                     (System/getProperty "file.separator")))

(def html-file (str output-dir "index.html"))
(def css-file (str output-dir "example-style.css"))


(spit html-file (make-page section-etalon
                           section1
                           section-etalon
                           section2
                           section-etalon
                           section3
                           section-etalon
                           section4))

(spit css-file (css example-style))