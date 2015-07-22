(ns zoondka-maps.server
  (:require [org.httpkit.server :refer [run-server]]
            [zoondka-maps.handler :as h])
  (:gen-class))

(defn run [handler & [port]]
  (defonce ^:private server
    (let [port (Integer. (or port 8090))]
      (println "Starting web server on port" port "...")
      (run-server handler {:port port})))
  server)

(def handler #'h/handler)

(defn -main [& [port]]
  (run handler port))
