(ns zoondka-maps.style)

(def style
  {:version 7
   :glyphs "mapbox://fontstack/{fontstack}/{range}.pbf"
   :constants {"@sans" "Open Sans Regular, Arial Unicode MS Regular"
               "@small-label" "#111111"
               "@water" "#88aadd"
               "@land" "#f7f7f7"
               "@green" "rgba(0, 233, 0, 0.2)"
               "@admin_l2_width" {"stops" [[2 0.6] [20 6]]}
               "@admin_l3_width" {"stops" [[5 0.6] [20 6]]}}

   :sources {:osm {"type" "vector"
                   "tiles" ["https://vector.mapzen.com/osm/all/{z}/{x}/{y}.mvt?api_key=vector-tiles-Vg2j_l4"]}
             :mbs {"type" "vector"
                   "url" "mapbox://mapbox.mapbox-streets-v6"}}
   "layers" [{:id "earth"
              :type "fill"
              :source "osm"
              :source-layer "earth"
              :paint {:fill-color "@land"}}
             {:id "land"
              :type "fill"
              :source "mbs"
              :source-layer "landuse"
              :paint {:fill-color "@green"}}
             {:id "admin_l2"
              :type "line"
              :source "mbs"
              :source-layer "admin"
              :filter ["==" "admin_level" 2]
              :layout {:line-join "round"
                       :line-cap "round"}
              :paint {:line-color "#999999"
                      :line-width "@admin_l2_width"}}
             {:id "admin_l3"
              :type "line"
              :source "mbs"
              :source-layer "admin"
              :filter ["in" "admin_level" 3 4 5]
              :layout {:line-join "round"}
              :paint {:line-color "#999999"
                      :line-dasharray [2 1]
                      :line-opacity {:stops [[0 0] [4 1]]}
                      :line-width "@admin_l3_width"}}
             {:id "water"
              :source "osm"
              :source-layer "water"
              :type "fill"
              :paint {:fill-color "@water"}}]})
