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

   :sources {:mb {:type "vector"
                  :url "mapbox://mapbox.mapbox-terrain-v2,mapbox.mapbox-streets-v6"}
             :osm {:type "vector"
                   :url "http://tile.openstreetmap.us/vectiles-highroad/{z}/{x}/{y}.mvt"}}
   :layers [{:id "background"
             :type "background"
             :paint {:background-color "@land"}}
            {:id "landcover_snow"
             :type "fill"
             :source "mb"
             :source-layer "landcover"
             :filter ["==" "class" "snow"]
             :paint {:fill-color "@snow"}}
            {:id "landcover_crop"
             :type "fill"
             :source "mb"
             :source-layer "landcover"
             :filter ["==" "class" "crop"]
             :paint {:fill-color "@crop"}}
            {:id "landcover_grass"
             :type "fill"
             :source "mb"
             :source-layer "landcover"
             :filter ["==" "class" "grass"]
             :paint {:fill-color "@grass"
                     :fill-opacity {:stops [[12 1] [16 0.2]]}}}
            {:id "landcover_scrub"
             :type "fill"
             :source "mb"
             :source-layer "landcover"
             :filter ["==" "class" "scrub"]
             :paint {:fill-color "@scrub"
                     :fill-opacity {:stops [[12 1] [16 0.2]]}}}
            {:id "landcover_wood"
             :type "fill"
             :source "mb"
             :source-layer "landcover"
             :filter ["==" "class" "wood"]
             :paint {:fill-color "@wood"
                     :fill-opacity {:stops [[12 1] [16 0.2]]}}}
            {:id "landuse_wood"
             :type "fill"
             :source "mb"
             :source-layer "landuse"
             :filter ["==" "class" "wood"]
             :paint {:fill-color "rgba(0, 182, 0, 0.1)"
                     :fill-outline-color "rgba(0, 182, 0, 0.2)"}}
            {:id "landuse_rock"
             :type "fill"
             :source "mb"
             :source-layer "landuse"
             :filter ["==" "class" "rock"]
             :paint {:fill-color "rgba(77, 77, 77, 0.1)"
                     :fill-outline-color "rgba(77, 77, 77, 0.2)"}}
            {:id "landuse_agriculture"
             :type "fill"
             :source "mb"
             :source-layer "landuse"
             :filter ["==" "class" "agriculture"]
             :paint {:fill-color "rgba(177, 177, 0, 0.1)"
                     :fill-outline-color "rgba(177, 177, 0, 0.1)"}}
            {:id "road_main_casing"
             :type "line"
             :source "mb"
             :source-layer "road"
             :filter ["==" "class" "main"]
             :layout {:line-join "round"
                      :line-cap "round"}
             :paint {:line-color "#fff"
                     :line-width "@main_casing_width"
                     :line-gap-width "@main_width"
                     :line-opacity {:stops [[8 0] [9 1]]}}}
            {:id "road_motorway_casing"
             :type "line"
             :source "osm"
             :layout {:line-join "round"
                      :line-cap "round"}
             :paint {:line-color "#ff00ff"
                     :line-width "@motorway_casing_width"
                     :line-gap-width "@motorway_width"}}
            {:id "road_main"
             :ref "road_main_casing"
             :paint {:line-color "#000"
                     :line-width "@main_width"
                     :line-opacity {:stops [[3 0] [4 1]]}}}
            {:id "road_motorway"
             :ref "road_motorway_casing"
             :paint {:line-color "#777"
                     :line-width "@motorway_width"
                     :line-opacity {:stops [[3 0] [4 1]]}}}
            {:id "water"
             :type "fill"
             :source "mb"
             :source-layer "water"
             :paint {:fill-color "@water"
                     :fill-outline-color "#a2bdc0"}}
            {:id "contour_line"
             :type "line"
             :source "mb"
             :source-layer "contour"
             :filter ["!=" "index" 5]
             :paint {:line-color "#000"
                     :line-width 1.2
                     :line-opacity {:stops [[11 0.05] [12 0.1]]}}}
            {:id "contour_line_loud"
             :type "line"
             :source "mb"
             :source-layer "contour"
             :filter ["==" "index" 5]
             :paint {:line-color "#000"
                     :line-width 1.2
                     :line-opacity {:stops [[11 0.1] [12 0.2]]}}}
            {:id "admin_l2"
             :type "line"
             :source "mb"
             :source-layer "admin"
             :filter ["==" "admin_level" 2]
             :layout {:line-join "round"
                      :line-cap "round"}
             :paint {:line-color "#999999"
                     :line-width "@admin_l2_width"}}
            {:id "admin_l3"
             :type "line"
             :source "mb"
             :source-layer "admin"
             :filter ["in" "admin_level" 3 4 5]
             :layout {:line-join "round"}
             :paint {:line-color "#999999"
                     :line-dasharray [2 1]
                     :line-opacity {:stops [[0 0] [4 1]]}
                     :line-width "@admin_l3_width"}}]})
