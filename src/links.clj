(ns links
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [hiccup.page :refer [html5]]))

(defn render [links-data]
  (html5
   [:head
    ;; Shared
    [:meta {:name "viewport"
            :content "initial-scale=1,width=device-width"}]
    [:meta {:content "no-cache"
            :http-equiv "cache-control"}]
    [:link {:rel "stylesheet"
            :href "https://fonts.googleapis.com/css?family=Space+Mono|Muli"}]
    [:script {:src "https://kit.fontawesome.com/729e48c2e5.js"
              :crossorigin "anonymous"}]
    ;; Unique
    [:link {:rel "stylesheet" :href "links.css" :type "text/css"}]
    [:title "John Maruska - Links"]]
   [:body
    [:div.column
     [:div#links-profile
      [:img#avatar {:src "profile_picture_512x521.png"
                    :alt "profile pic of John. Long hair, full beard, glasses."}]
      [:p "These are all the accounts I associate with my real name. I've been full hermit/lurker mode on most of these but I'm hoping to change that soonish."]]
     [:ul#links
      (for [{:keys [network url icon]} links-data]
        [:li [:a {:href url :target "_blank"}
              [:i {:class icon}] network]])]]]))

(defn page []
  (render (edn/read-string (slurp (io/resource "links.edn")))))
