(ns zoondka-maps.style)

(def style
  {:version 7
   :glyphs "mapbox://fontstack/{fontstack}/{range}.pbf"
   :constants {"@sans" "Open Sans Regular, Arial Unicode MS Regular"
               "@land" "#f4efe1"
               "@snow" "#f4f8ff"
               "@crop" "#eeeed4"
               "@grass" "#e6e6cc"
               "@scrub" "#dfe5c8"
               "@wood" "#cee2bd"
               "@water" "#bbbbff" ;"#99aaee"
               "@green" "rgba(0, 233, 0, 0.2)"
               "@motorway_width" {:base 1.25
                                  :stops [[8 0.5] [12 1] [16 3]]}
               "@motorway_casing_width" {:stops [[8 0.5] [11 1] [14 1.5]]}
               "@main_width" {:base 1.3
                              :stops [[3 0] [4 1] [20 28]]}
               "@main_casing_width" {:stops [[11 1] [15 1.5]]}
               "@boundary_width" {:stops [[0 0.2] [20 6]]}}
   :sources {:tile-srv {:type "vector"
                        :tiles ["https://maps.zoondka.com/tile-srv/all/{z}/{x}/{y}.mvt"]}}
   :layers [{:id "background"
             :type "background"
             :paint {:background-color "@land"}}
            {:id "earth"
             :type "fill"
             :source "tile-srv"
             :source-layer "earth"
             :paint {:fill-color "@land"}}
            {:id "water"
             :type "fill"
             :source "tile-srv"
             :source-layer "water"
             :paint {:fill-color "@water"}}
            {:id "country-boundary"
             :type "line"
             :source "tile-srv"
             :source-layer "boundaries"
             :filter ["==" "type" "country"]
             :layout {:line-cap "round"
                      :line-join "round"}
             :paint {:line-color "#666"}
             :line-width "@boundary_width"}
            {:id "state-boundary"
             :type "line"
             :source "tile-srv"
             :source-layer "boundaries"
             :filter ["==" "type" "state"]
             :layout {:line-cap "round"
                      :line-join "round"}
             :paint {:line-color "#999"
                     :line-dasharray [1, 2]
                     :line-width "@boundary_width"}}
            {:id "road"
             :type "line"
             :source "tile-srv"
             :source-layer "roads"
             :layout {:line-cap "round"
                      :line-join "round"}
             :paint {:line-color "#000"
                     :line-width "@motorway_width"}}]})
