(ns zoondka-maps.map
  (:require [cljs.core.async :as async]
            [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [clojure.set :as set]
            [zoondka-maps.util :as u]))

(defn init-map [owner]
  (let [map (js/mapboxgl.Map. (clj->js {:container "map"
                                        :style "style/bright-v8.json"
                                        :zoom 4
                                        :center [0, 0]
                                        :attributionControl false}))]

    (if navigator.geolocation
      (.getCurrentPosition navigator.geolocation
        (fn [pos]
          (let [initialLoc #js [(.-coords.longitude pos)
                                (.-coords.latitude pos)]]
            (.setCenter map initialLoc)))
        (fn [err]
          (.warn js/console "Warning: Geolocation: Unable to get your current position: " err)))
      (.warn js/console "Warning: Geolocation: Disabled"))

    (om/set-state! owner :map map)))

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
      (init-map owner))

    om/IRender
    (render [_]
      (html
        [:div#map]))))
