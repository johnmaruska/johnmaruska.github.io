(ns links
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [hiccup.page :refer [html5]]))

(defn render [links-data]
  (html5
   [:head
    ;; Shared
    [:meta {:name "viewport" :content "initial-scale=1,width=device-width"}]
    [:meta {:content "no-cache" :http-equiv "cache-control"}]
    [:link {:rel "stylesheet" :href "https://fonts.googleapis.com/css?family=Space+Mono|Muli"}]
    [:link {:rel "stylesheet" :href "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"}]
    ;; Unique
    [:link {:rel "stylesheet" :href "links.css" :type "text/css"}]
    [:title "John Maruska - Links"]]
   [:body
    [:ul
     (for [{:keys [network url icon]} links-data]
       [:li [:a {:href url :target "_blank"}
             [:i {:class icon}] network]])]]))

(defn page []
  (render (edn/read-string (slurp (io/resource "links.edn")))))
