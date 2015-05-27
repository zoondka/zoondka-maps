(set-env!
        :target-path "target"
        :resource-paths #{"resources"}
        :source-paths #{"src/clj" "src/cljs" "env"}
        :dependencies '[[adzerk/boot-cljs "0.0-3269-1"]
                        [adzerk/boot-cljs-repl "0.1.9"]
                        [adzerk/boot-reload "0.2.6"]
                        [boot-syu "0.1.0"]
                        [org.clojure/clojure "1.7.0-RC1"]
                        [org.clojure/clojurescript "0.0-3297"]
                        [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                        [org.omcljs/om "0.8.8"]
                        [sablono "0.3.4"]
                        [ring "1.4.0-RC1"]
                        [compojure "1.3.4"]
                        [http-kit "2.1.19"]
                        [selmer "0.8.2"]])

(require
  '[boot.core :as c]
  '[clojure.java.io :as io]
  '[adzerk.boot-cljs       :refer :all]
  '[adzerk.boot-reload     :refer :all]
  '[adzerk.boot-cljs-repl  :refer :all]
  '[boot-syu.task          :refer :all]
  '[zoondka-maps.server             :as server]
  '[ring.middleware.reload    :as reload]
  '[ring.middleware.file      :as file]
  '[ring.middleware.file-info :as file-info]
  '[selmer.parser :as selmer])

(defn render [in-file out-file k v]
  (doto out-file
    io/make-parents
    (spit (selmer/render (slurp in-file) {k v}))))

(deftask render-conf
  "Render configuration files from templates"
  []
  (let [tmp (c/tmp-dir!)]
    (with-pre-wrap fileset
      (c/empty-dir! tmp)
      (let [conf (->> fileset
                   (c/input-files)
                   (c/by-name ["nginx-template.conf"])
                   (first))
            in-file (c/tmp-file conf)
            in-path (c/tmp-path conf)
            out-path (.replaceAll in-path "nginx-template.conf" "nginx.conf")
            out-file (io/file tmp out-path)]
        (render in-file out-file :classpath "YO!")
        (-> fileset
            (c/add-resource tmp)
            (c/commit!))))))

(defn dev-handler []
  (-> server/handler (reload/wrap-reload)
                     (file/wrap-file "target")
                     (file-info/wrap-file-info)))

(deftask dev-cljs
  "Build cljs for development."
[]
(comp (watch)
      (speak)
      (reload :on-jsload (symbol "zoondka-maps.app/go!"))
      (cljs :source-map true
            :optimizations :none
            :output-to "js/main.js")))

(deftask dev-serve
  "Start server for development."
[]
(with-post-wrap fileset (server/run (dev-handler))))

(deftask dev
  "Build cljs and start server for development."
[]
(comp
      (dev-cljs)
      (dev-serve)))

(deftask prod
  "Build application uberjar with http-kit main."
[]
(comp (cljs :unified true
            :source-map true
            :optimizations :none
            :output-to "js/main.js")
      (aot :all true)
      (uber)
      (jar :file "zoondka-maps.jar" :main 'zoondka-maps.server)))

(deftask install-local
  "Install to local Maven repository."
  []
  (comp (aot :all true)
        (pom :project 'zoondka-maps :version "0.1.0")
        (jar)
        (install)))
