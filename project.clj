(defproject sheet-helper "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[cider/cider-nrepl "0.8.1"]]
  :main sheet-helper.core
  :dependencies [
                 [org.clojure/data.json "0.2.6"]
                 [javax.servlet/servlet-api "2.5"]
                 [org.clojure/clojure "1.6.0"]
                 [ring/ring-core "1.3.2"]
                 [compojure "1.3.4"]
                 [http-kit "2.1.18"]
                 ])
