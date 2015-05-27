(ns zoondka-maps.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :as route]
            [ring.util.response :as resp]))

(defroutes handler
  (GET "/" [] (resp/resource-response "index.html"))
  (route/resources "/" {:root ""})
  (route/not-found "Page not found."))
