(ns zoondka-maps.core
  (:require [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [zoondka-maps.util :as u]
            [zoondka-maps.map :as map]))

(defn window
  [data owner]
  (om/component
    (html [:div#app
           (om/build map/l-map (u/get-settings data))])))
