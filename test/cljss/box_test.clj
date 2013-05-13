(ns cljss.box-test
  (:use cljss.box
        midje.sweet
        [cljss.units.length :only (px)]))



(def property #'cljss.box/property)

(fact "The property function take a name and a property and return a css property"
  (property :border :left) => :border-left
  (property :padding :bottom) => :padding-bottom)

(facts "We can manipulate css box's size with differents selectors"

  (fact "we can resize border"
    (resize default-box :border :all (px 3))
    => {:border-bottom (px 3)
        :border-left   (px 3)
        :border-right  (px 3)
        :border-top    (px 3)})


  (fact "we can resize padding"
    (resize default-box :padding :tb (px 50))
    => {:padding-bottom (px 50)
        :padding-top    (px 50)}

    (resize default-box :padding :lr (px 20))
    => {:padding-left  (px 20)
        :padding-right (px 20)})


  (fact "we can resize margins"
    (resize default-box :margin :left (px 50))
    => {:margin-left (px 50)}

    (resize default-box :margin [:left :top] (px 20))
    => {:margin-left (px 20)
        :margin-top  (px 20)})


  (fact "we can change width and height"
    (resize default-box :width (px 50))
    => {:width (px 50)}

    (resize default-box :height (px 20))
    => {:height (px 20)}))

(fact "When we try to resize with an unknown property we got an exception"
  (resize default-box :a (px 10)) => (throws clojure.lang.ExceptionInfo)
  (resize default-box :border :b (px 10)) => (throws clojure.lang.ExceptionInfo)
  (resize default-box :a :b (px 10)) => (throws clojure.lang.ExceptionInfo))
