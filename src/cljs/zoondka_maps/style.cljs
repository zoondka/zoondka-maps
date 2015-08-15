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
                                  :stops [[0 0] [4 1] [20 30]]}
               "@motorway_casing_width" {:stops [[9 0.9] [11 1] [14 1.5]]}
               "@main_width" {:base 1.3
                              :stops [[3 0] [4 1] [20 28]]}
               "@main_casing_width" {:stops [[11 1] [15 1.5]]}
               "@admin_l2_width" {:stops [[2 0.6] [20 6]]}
               "@admin_l3_width" {:stops [[5 0.6] [20 6]]}}

   :sources {:mapbox   {:type "vector"
                        :url "mapbox://mapbox.mapbox-streets-v5"}
             :tile-gen {:type "vector"
                        :tiles ["https://maps.zoondka.com/tile-srv/all/{z}/{x}/{y}.mvt"]}}
   :layers [{:id "background"
             :type "background"
             :paint {:background-color "@land"}}
            {:id "water"
             :type "fill"
             :source "tile-gen"
             :source-layer "water"
             :paint {:fill-color "@water"}}
            {:id "earth"
             :type "fill"
             :source "tile-gen"
             :source-layer "earth"
             :paint {:fill-color "@land"}}]})
