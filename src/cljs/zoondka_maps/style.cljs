(ns zoondka-maps.style)

(def style
  {:version 7
   :glyphs "mapbox://fontstack/{fontstack}/{range}.pbf"
   :constants {"@sans" "Open Sans Regular, Arial Unicode MS Regular"
               "@water" "#bbbbff" ;"#99aaee"
               "@land" "#f4efe1"
               "@snow" "#f4f8ff"
               "@crop" "#eeeed4"
               "@grass" "#e6e6cc"
               "@scrub" "#dfe5c8"
               "@wood" "#cee2bd"
               "@green" "rgba(0, 233, 0, 0.2)"
               "@admin_l2_width" {"stops" [[2 0.6] [20 6]]}
               "@admin_l3_width" {"stops" [[5 0.6] [20 6]]}}

   :sources {:mb {"type" "vector"
                   "url" "mapbox://mapbox.mapbox-terrain-v2,mapbox.mapbox-streets-v6"}}
   "layers" [{:id "background"
              :type "background"
              :paint {:background-color "@land"}}
             {:id "land"
              :type "fill"
              :source "mb"
              :source-layer "landuse"
              :paint {:fill-color "@green"}}
             {:id "landcover_snow"
              :type "fill"
              :source "mb"
              :source-layer "landcover"
              :filter ["==" "class" "snow"]
              :paint {"fill-color" "@snow"}}
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
             {:id "water"
              :type "fill"
              :source "mb"
              :source-layer "water"
              :paint {:fill-color "@water"}}
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
