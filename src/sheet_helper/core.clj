(ns sheet-helper.core
  (:require 
   (clojure.java [io :as io])
   (clojure.data [json :as json :refer [write-str]])
   (ring.middleware [params :refer [wrap-params]]
                    [keyword-params :refer [wrap-keyword-params]]
                    [multipart-params :refer [wrap-multipart-params]])
   (compojure  [core :as cpj :refer [defroutes GET POST]])   
   (org.httpkit [server :as httpkit :refer [run-server]])))

(def files (atom {}))


(defn simple-logging-middleware [app]
  (fn [req]
    (println req)
    (app req)))

(defroutes all-routes 
  (GET "/"  req (str req))
  (GET "/sheet/:id" {params :params} (let [sheet (:id params)]
                                    (println sheet)
                                    (if (nil? sheet)
                                      {:status 404}
                                      {:status 200 :body (str sheet 12312313)})))
  (GET "/sheets" [req] (write-str (into () (keys @files))))
  (POST "/upload" {params :params}
        (let [file (:file params)
              filename (:filename file)
              tmppath (:tempfile file)]
         (if (contains? @files filename)
                       {:status 412}
                       (do 
                         (swap! files assoc filename tmppath)
                         "added")))))

(def app
  (-> all-routes
      wrap-keyword-params
      wrap-params
      wrap-multipart-params))


(defn -main
  [& args]
  (run-server app {:port 8080}))


