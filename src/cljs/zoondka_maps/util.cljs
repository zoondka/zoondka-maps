(ns zoondka-maps.util
  (:require [cljs.core.async :as async])
  (:require-macros [cljs.core.async.macros :refer [go-loop]]))

(defn mmap [f m]
  (into {} (for [[k v] m]
             [k (f v)])))

(defn mfilter [f m]
  (select-keys m (for [[k v] m :when (f v)] k)))

(defn keyset [m]
  (set (keys m)))

(defn sub-go-loop [ch topic fun]
  (let [events (async/sub ch topic (async/chan))]
    (go-loop [e (<! events)]
      (fun e)
      (recur (<! events)))))

(defn get-settings [data]
  (get-in data [:model :settings]))
