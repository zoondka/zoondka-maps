(def project {:name "zoondka-maps" :version "0.1.1"})

(set-env!
  :source-paths   #{"src/clj" "src/cljs"}
  :resource-paths #{"rsc"}
  :target-path    "target"
  :dependencies '[[adzerk/boot-cljs "0.0-3308-0"]
                  [adzerk/boot-cljs-repl "0.1.9"]
                  [adzerk/boot-reload "0.3.1"]
                  [org.clojure/clojure "1.7.0"]
                  [org.clojure/clojurescript "0.0-3308"]
                  [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                  [org.omcljs/om "0.9.0"]
                  [sablono "0.3.4"]
                  [ring "1.4.0"]
                  [compojure "1.4.0"]
                  [http-kit "2.1.19"]])

(require
  '[boot.pod                  :as pod]
  '[adzerk.boot-cljs          :refer :all]
  '[adzerk.boot-reload        :refer :all]
  '[adzerk.boot-cljs-repl     :refer :all]
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
        (cljs :compiler-options {:output-to "js/main.js"})))

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
  (comp (cljs :compiler-options {:output-to "js/main.js"})
        (aot :namespace '#{zoondka-maps.server zoondka-maps.handler})
        (pom :project (symbol (:name project))
             :version (:version project))
        (uber :exclude (conj pod/standard-jar-exclusions  #"(?i).*\.html" #"(?i)clout/.*"))
        (jar :file (str (:name project) ".jar")
             :main 'zoondka-maps.server)))

(deftask prod-install
  "Install to local Maven repository."
  []
  (comp (prod) (install)))
