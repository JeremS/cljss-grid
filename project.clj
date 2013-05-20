(defproject jeremys/cljss-grid "0.1.0-SNAPSHOT"
  :description "Little grid framework on top of cljss."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [jeremys/cljss-core  "0.3.0-SNAPSHOT"]
                 [jeremys/cljss-units "0.2.2-SNAPSHOT"]]

  :plugins [[codox "0.6.4"]]

  :profiles {:dev
             {:dependencies [[org.clojure/tools.trace "0.7.5"]
                             [midje "1.5.1"]
                             [hiccup "1.0.3"]]}})
