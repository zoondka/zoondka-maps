(ns zoondka-maps.style)

(def sans "Open Sans Regular, Arial Unicode MS Regular")
(def land "#f4efe1")
(def water "#ccdddd")
(def motorway "#cda0a0")
(def main "#ddc0b9")
(def casing "#ffffff")
(def motorway_width        {:base 1.25
                            :stops [[8 0.5] [12 1] [16 3]]})
(def motorway_casing_width {:stops [[8 0.5] [12 1] [16 2]]})
(def main_width            {:base 1.3
                            :stops [[10 0.5] [12 0.75] [16 2.5]]})
(def main_casing_width     {:stops [[11 1] [15 1.5]]})
(def country_boundary_width {:stop [[0 0.2] [20 4]]})
(def state_boundary_width   {:stops [[0 0] [20 4]]})

(def style
  {:version 8
   :sources {:tile-srv {:type "vector"
                        :tiles ["https://maps.zoondka.com/tile-srv/all/{z}/{x}/{y}.mvt"]}}
   :layers [{:id "background"
             :type "background"
             :paint {:background-color land}}
            {:id "land"
             :type "fill"
             :source "tile-srv"
             :source-layer "earth"
             :paint {:fill-color land}}
            {:id "water"
             :type "fill"
             :source "tile-srv"
             :source-layer "water"
             :paint {:fill-color water}}
            {:id "country-boundary"
             :type "line"
             :source "tile-srv"
             :source-layer "boundaries"
             :filter ["==" "type" "country"]
             :layout {:line-cap "round"
                      :line-join "round"}
             :paint {:line-color "#666"}
             :line-width country_boundary_width}
            {:id "state-boundary"
             :type "line"
             :source "tile-srv"
             :source-layer "boundaries"
             :filter ["==" "type" "state"]
             :layout {:line-cap "round"
                      :line-join "round"}
             :paint {:line-color "#999"
                     :line-dasharray [1, 2]
                     :line-width state_boundary_width}}
            {:id "motorway_casing"
             :type "line"
             :source "tile-srv"
             :source-layer "roads"
             :filter ["==" "type" "motorway"]
             :layout {:line-join "round"
                      :line-cap "round"}
             :paint {:line-color casing
                     :line-width motorway_casing_width
                     :line-gap-width motorway_width
                     :line-opacity {:stops [[8.5 0] [9 1]]}}}
            {:id "motorway"
             :ref "motorway_casing"
             :paint {:line-color motorway
                     :line-width motorway_width}}
            {:id "major_roads"
             :type "line"
             :source "tile-srv"
             :source-layer "roads"
             :filter ["in" "type" "trunk" "primary" "secondary" "tertiary"]
             :layout {:line-cap "round"
                      :line-join "round"}
             :paint {:line-color main
                     :line-width main_width}}]})
