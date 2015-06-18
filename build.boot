(def project {:name "zoondka-maps" :version "0.1.0"})

(set-env!
  :target-path "target"
  :resource-paths #{"resources"}
  :source-paths #{"src/clj" "src/cljs"}
  :dependencies '[[adzerk/boot-cljs "0.0-3269-1"]
                  [adzerk/boot-cljs-repl "0.1.9"]
                  [adzerk/boot-reload "0.2.6"]
                  [org.clojure/clojure "1.6.0"]
                  [org.clojure/clojurescript "0.0-3297"]
                  [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                  [com.cognitect/transit-cljs "0.8.220"]
                  [org.omcljs/om "0.8.8"]
                  [sablono "0.3.4"]
                  [selmer "0.8.2"]
                  [ring "1.3.2"]
                  [compojure "1.3.0"]
                  [http-kit "2.1.19"]])

(require
  '[boot.core :as c]
  '[clojure.java.io :as io]
  '[adzerk.boot-cljs       :refer :all]
  '[adzerk.boot-reload     :refer :all]
  '[adzerk.boot-cljs-repl  :refer :all]
  '[zoondka-maps.server       :as server]
  '[ring.middleware.reload    :as reload]
  '[ring.middleware.file      :as file]
  '[ring.middleware.file-info :as file-info]
  '[selmer.parser :as selmer])

(def env {:dev  {:classpath (or (System/getProperty "boot.class.path") "")}
          :prod {:classpath (.getCanonicalPath (io/file "target" (str (:name project) ".jar")))}})

(defn render [in-file out-file context-map]
  (doto out-file
    io/make-parents
    (spit (selmer/render (slurp in-file) context-map))))

(deftask selmer-render
  "Render file from selmer template"
  [i in          NAME    str      "The name of the template file"
   o out         NAME    str      "The name of the rendered file"
   c context-map KEY=VAL {kw str} "The context map for rendering"]
  (let [tmp (c/tmp-dir!)]
    (with-pre-wrap fileset
      (c/empty-dir! tmp)
      (let [conf (->> fileset
                      (c/input-files)
                      (c/by-name [in])
                      (first))
            in-file (c/tmp-file conf)
            in-path (c/tmp-path conf)
            out-path (.replaceAll in-path in out)
            out-file (io/file tmp out-path)]
        (render in-file out-file context-map)
        (-> fileset
            (c/add-resource tmp)
            (c/commit!))))))

(deftask dev-cljs
  "Build cljs for development."
  []
  (comp (watch)
        (speak)
        (reload :on-jsload (symbol "zoondka-maps.app/go!"))
        (cljs :source-map true
              :optimizations :none
              :output-to "js/main.js")))

(deftask dev-nginx
  "Configure & restart nginx server for development."
  []
  (selmer-render :in "nginx-template.conf"
                 :out "nginx.conf"
                 :context-map (:dev env)))

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
  (comp (dev-cljs)
        (dev-httpkit)))

(deftask prod
  "Build application uberjar with http-kit main."
  []
  (comp (cljs :unified true
              :source-map true
              :optimizations :none
              :output-to "js/main.js")
        (aot :all true)
        (uber)
        (jar :file (str (:name project) ".jar")
             :main 'zoondka-maps.server)))

(deftask install-local
  "Install to local Maven repository."
  []
  (comp (aot :all true)
        (pom :project (symbol (:name project))
             :version (:version project))
        (jar)
        (install)))
