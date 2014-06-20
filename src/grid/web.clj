(ns grid.web
  (:require [org.httpkit.server :refer [run-server]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :as route :refer [not-found resources]]))

(defn index [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello HTTP!"})

(defroutes app
  (resources "/")
  (GET "/" [] index)
  (route/not-found "Route not found" ))

(defn -main [& args]
  (run-server app {:port 8666})
  (println "Server has been started at localhost:8666"))
