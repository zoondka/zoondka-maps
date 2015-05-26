(ns zoondka-maps.app
  (:require [cljs.core.async :as async]
            [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [zoondka-maps.core :as core])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(enable-console-print!)

(def tile-url "http://{s}.tile.osm.org/{z}/{x}/{y}.png")
(def tile-attr "&copy; <a href='http://osm.org/copyright'>OpenStreetMap</a> contributors")

(def app-state
  (atom {:model {:settings         {:tile-url     {:id :tile-url
                                                   :value tile-url}
                                    :tile-attr    {:id :tile-attr
                                                   :value tile-attr}}}}))

(def event-bus (async/chan))
(def event-bus-pub (async/pub event-bus first))

(defn app [data owner]
  (om/component
    (om/build core/window data)))

(defn render []
  (om/root app app-state {:target (.getElementById js/document "root")
                          :shared {:event-bus event-bus
                                   :event-bus-pub event-bus-pub}}))

(defn go! []
  (render))

(go!)
