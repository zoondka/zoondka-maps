(ns zoondka-maps.map
  (:require [cljs.core.async :as async]
            [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [clojure.set :as set]
            [zoondka-maps.util :as u]))

(defn init-map [tile-url tile-attr owner]
  (when-let [l-map (om/get-state owner :map)]
    (.remove l-map))
  (let [l-map (-> js/L
                (.map "map"
                  (clj->js {:zoomControl false}))
                (.setView (om/get-state owner :center) 9))]

    (-> js/L
      (.tileLayer (:value tile-url) #js {:attribution (:value tile-attr)})
      (.addTo l-map))

    (.on l-map "contextmenu" #())

    (if navigator.geolocation
      (.getCurrentPosition navigator.geolocation
        (fn [pos]
          (let [initialLoc #js [(.-coords.latitude pos)
                                (.-coords.longitude pos)]]
            (.setView l-map initialLoc 9))))
      (println "Hey, where'd you go!? Geolocation Disabled."))

    (om/set-state! owner :map l-map)))

(defn l-map [{:keys [tile-url tile-attr]} owner]
  (reify
    om/IInitState
    (init-state [_]
      {:center #js [0 0]
       :evt-timeout nil
       :units {}
       :map nil})

    om/IDidMount
    (did-mount [_]
      (init-map tile-url tile-attr owner))

    om/IRender
    (render [_]
      (html
        [:div#map]))))
