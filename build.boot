(def project {:name "zoondka-maps" :version "0.2.0"})

(set-env!
  :source-paths   #{"src/clj" "src/cljs"}
  :resource-paths #{"rsc"}
  :target-path "target"
  :dependencies '[[adzerk/boot-cljs "1.7.228-1"]
                  [adzerk/boot-reload "0.4.5"]
                  [org.clojure/clojure "1.7.0"]
                  [org.clojure/clojurescript "1.7.228"]
                  [org.clojure/core.async "0.2.374"]
                  [cljsjs/react "0.14.3-0"]
                  [cljsjs/react-dom "0.14.3-1"]
                  [cljsjs/react-dom-server "0.14.3-0"]
                  [org.omcljs/om "1.0.0-alpha30" :exclusions [cljsjs/react]]
                  [sablono "0.5.3"]
                  [ring "1.4.0"]
                  [compojure "1.4.0"]
                  [http-kit "2.1.19"]])

(require
  '[boot.pod                  :as pod]
  '[adzerk.boot-cljs          :refer :all]
  '[adzerk.boot-reload        :refer :all]
  '[zoondka-maps.server       :as server]
  '[ring.middleware.reload    :as reload]
  '[ring.middleware.file      :as file]
  '[ring.middleware.file-info :as file-info])

(deftask dev-cljs
  "Build cljs for development."
  []
  (comp (watch)
        (speak)
        (reload :on-jsload (symbol "zoondka-maps.app/go!"))
        (cljs :source-map true
              :optimizations :none)
        (target :dir #{"target"})))

(defn dev-handler []
  (-> server/handler (reload/wrap-reload)
                     (file/wrap-file "target")
                     (file-info/wrap-file-info)))

(deftask dev-httpkit
  "Start internal httpkit server for development."
  []
  (with-post-wrap fileset (server/run (dev-handler))))

(deftask dev
  "Build cljs and start server for development."
  []
  (comp (dev-cljs) (dev-httpkit)))

(deftask prod
  "Build application uberjar with http-kit main."
  []
  (comp (cljs)
        (aot :namespace '#{zoondka-maps.server zoondka-maps.handler})
        (pom :project (symbol (:name project))
             :version (:version project))
        (uber :exclude (conj pod/standard-jar-exclusions  #"(?i).*\.html" #"(?i)clout/.*"))
        (jar :file (str (:name project) ".jar")
             :main 'zoondka-maps.server)))

(deftask prod-install
  "Install uberjar to local Maven repository for deployment."
  []
  (comp (prod) (install :pom "zoondka-maps/zoondka-maps")))
