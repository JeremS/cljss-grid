(ns cljss.page-test
  (:use cljss.core
        cljss.grid
        hiccup.core
        [hiccup.page :only (html5 include-css)]

        [clojure.pprint :only (pprint)]
        clojure.repl))

;; css generation

(def w (partial width default-grid ))
(def gw (partial general-width default-grid ))
(def col (partial column default-grid))

(def column-classes
  (for [n (range 0 25)]
    (str ".col" n)))


(defrules rules1
  (css-comment "general")
  [:#container
   :width (gw 24)]

  (css-comment "page specific")
  [:section
   :height :15px
   :margin-bottom :10px

   [[& :> :div] ;container-mixin

    [:div :text-align :center] ; divs inside each row

    [[ & (-> :div (nth-child :even))]
     :background-color :red]

    [[& (-> :div (nth-child :odd))]
     :background-color :blue]]]

  (css-comment "generated grid classes")
  [(set column-classes) column-mixin]
  (map #(vector %1 (col %2) )
       column-classes
       (range))

  [:.col0 :display :none])



;; html generation

(def seq1 (for [n (range 25)]
            [n (- 24 n)]))


(defn make-div1 [n]
  [:div {:class (str "col" n)}
        (str (w n))])

(defn make-row1 [[a b]]
  [:section
   [:div (make-div1 a)
         (make-div1 b)]])

(defn make-section1 []
  (html (map make-row1 seq1)))

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
(spit html-file (make-page (make-section1)))

