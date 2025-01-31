(ns shared
  (:require [hiccup.util :refer [escape-html]]))

(defn head [{:keys [title css-file css-directory]
             :or {css-directory ""}} & body]
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
   [:link {:rel "stylesheet" :href (str css-directory "site.css") :type "text/css"}]
   [:link {:rel "stylesheet" :href css-file :type "text/css"}]
   [:title (escape-html title)]
   body])
