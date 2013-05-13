(defproject jeremys/cljss-grid "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [jeremys/cljss-core  "0.2.1"]
                 [jeremys/cljss-units "0.2.1"]]

  :profiles {:dev
             {:dependencies [[org.clojure/tools.trace "0.7.5"]
                             [midje "1.5.1"]
                             [hiccup "1.0.3"]]}})
