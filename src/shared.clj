(ns shared
  (:require
   [hiccup.page :refer [html5]]
   [hiccup.util :refer [escape-html]]))

(def ^:dynamic *relative-to-root* "")
(defn head [{:keys [title css-file]}]
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
   [:link {:rel "stylesheet"
           :href (str *relative-to-root* "site.css")
           :type "text/css"}]
   [:title (escape-html title)]])

(defn header-bar []
  [:div#header-bar
   [:a#site-title {:href "/"}
    "John Maruska"]
   [:nav#navbar
    [:ul
     [:li [:a {:href "links" :title "Links"}
           "Links"]]
     [:li [:a {:href "resume" :title "Resume"}
           "Resume"]]
     [:li [:a {:href "posts" :title "posts"}
           "Posts"]]]]])
