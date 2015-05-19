(ns sheet-helper.core
  (:import org.bson.types.ObjectId)
  (:require 
   (monger [core :as mg]
           [gridfs :as fs]
           [collection :as mc])
   (clojure.java [io :as io])
   (clojure.data [json :as json :refer [write-str]])
   (ring.util [response :as rsp :refer [response status]])
   (ring.middleware [params :refer [wrap-params]]
                    [keyword-params :refer [wrap-keyword-params]]
                    [multipart-params :refer [wrap-multipart-params]])
   (compojure  [core :as cpj :refer [defroutes GET POST PUT]])   
   (org.httpkit [server :as httpkit :refer [run-server]])))


(def mongo (mg/connect))
(def files (atom {}))




(defroutes all-routes 
  (GET "/"  req (response "ping"))
  (GET "/sheet/:id" {params :params} (let [id (:id params)
                                           gridfs (mg/get-gridfs mongo "somefiles")]
                                      (response 
                                       (fs/find-one-as-map gridfs {:filename id}))))

  (GET "/sheets" req (write-str (into () (keys @files))))
  (GET "/somedata/:id" {params :params} (let [db (mg/get-db mongo "somedb")]
                                          (response (mc/find-one-as-map db "somedata" {:somefield (:id params)}))))

  (PUT "/somedata/:id" {params :params} (let [db (mg/get-db mongo "somedb")]
                                          (response
                                           (.toString (mc/insert db "somedata" {:_id (ObjectId.) :somefield (:id params)})))))
  (POST "/upload" {params :params}
        (let [file (:file params)
              filename (:filename file)
              tmppath (:tempfile file)
              gridfs (mg/get-gridfs mongo "somefiles")]
          (response (fs/store-file 
                     (fs/make-input-file gridfs tmppath)
                     (fs/filename filename))))))

(def app
  (-> all-routes
      wrap-keyword-params
      wrap-params
      wrap-multipart-params))


(defn -main
  [& args]
  (run-server app {:port 8080}))


