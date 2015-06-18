(ns zoondka-maps.map
  (:require [cljs.core.async :as async]
            [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [clojure.set :as set]
            [zoondka-maps.util :as u]
            [cognitect.transit :as t])
  (:require-macros [zoondka-maps.macros :as m]))

(def mapbox-key "pk.eyJ1IjoibGVibG93bCIsImEiOiJmMzEzNGMzMDgzOWEyNjg0NDAwMzQzMWQ1OTUzM2FmYSJ9.J-V3-0X4LnoyptGTCGys3g")

(defn read-style []
  (let [r (t/reader :json)]
    (t/read r (m/slurp "resources/json/style.json"))))

(defn init-map [owner]
  (set! (.-accessToken js/mapboxgl) mapbox-key)

  (let [style (read-style)
        map (js/mapboxgl.Map. (clj->js {:container "map"
                                        :style style
                                        :zoom 7
                                        :minZoom 2
                                        :center [0, 0]}))]

    (if navigator.geolocation
      (.getCurrentPosition navigator.geolocation
        (fn [pos]
          (let [initialLoc #js [(.-coords.latitude pos)
                                (.-coords.longitude pos)]]
            (.setCenter map initialLoc))))
      (println "Hey, where'd you go!? Geolocation Disabled."))

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
