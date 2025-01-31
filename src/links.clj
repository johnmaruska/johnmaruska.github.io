(ns links
  (:require
   [shared :refer [head]]
   [hiccup.page :refer [html5]]
   [hiccup.util :refer [escape-html]]
   [clojure.edn :as edn]
   [clojure.java.io :as io]))

(def title "John Maruska - Links")
(def css-file "links.css")
(def profile-pic-asset "assets/profile_picture_512x521.png")
(def profile-pic-alt-text "profile pic of John. Long hair, full beard, glasses.")
(def intro-paragraph "These are all the accounts I associate with my real name. I've been full hermit/lurker mode on most of these but I'm hoping to change that soonish.")
(def data-file "links.edn")

(defn render [links-data]
  (html5 {}
    (head {:title title
           :css-file css-file})
    [:body
     [:div.column
      [:div#links-profile
       [:img#avatar {:src profile-pic-asset
                     :alt (escape-html profile-pic-alt-text)}]
       [:p (escape-html intro-paragraph)]]
      [:ul#links
       (for [{:keys [network url icon]} links-data]
         [:li [:a {:href url :target "_blank"}
               [:i {:class icon}] (escape-html network)]])]]]))

(defn fetch-data []
  (edn/read-string (slurp (io/resource data-file))))

(defn page-html []
  (render (fetch-data)))
