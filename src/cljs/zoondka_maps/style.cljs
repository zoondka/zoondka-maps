(ns zoondka-maps.style)

(def style
  {:version 7
   :glyphs "mapbox://fontstack/{fontstack}/{range}.pbf"
   :constants {"@sans" "Open Sans Regular, Arial Unicode MS Regular"
               "@small-label" "#111111"
               "@water" "#88aadd"
               "@land" "#ffffef"
               "@green" "rgba(0, 233, 0, 0.2)"
               "@road" "#333333"
               "@road-width" {:base 1.55
                             :stops [[4, 0.25] [20 30]]}}
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
             {:id "water"
              :source "osm"
              :source-layer "water"
              :type "fill"
              :paint {:fill-color "@water"}}]})
