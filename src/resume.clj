(ns resume
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as string]
   [hiccup.page :refer [html5]]
   [hiccup.util :refer [escape-html]]))

(defn introduction [{:keys [basics]}]
  [:div.introduction
   [:h1.name (escape-html (:name basics))]
   [:p.role (escape-html (:label basics))]
   [:p.location (escape-html (:location basics))]])

(defn links [{:keys [basics]}]
  [:div.links
   (for [{:keys [url icon]} (:contact-links basics)]
     [:a {:href url}
      [:i {:class icon}]])])

(defn summary [{:keys [basics]}]
  [:div.summary
   [:h2 "Professional Profile"]
   [:p.summary (escape-html (:content (:summary basics)))]])

(defn education [{:keys [education]}]
  [:div.education.section
   [:h2 "Education"]
   [:ul.education
    (for [entry education]
      [:li
       [:a {:href (:website entry)} (escape-html (:institution entry))]
       (escape-html (str
                     ", " (:study-type entry) " in " (:area entry)
                     ", " (:term entry)))])]])

(defn today []
  (.format (java.text.SimpleDateFormat. "MMMM d, YYYY")
           (java.util.Date.)))
(defn updated []
  [:div.updated [:p (str "updated:" (today))]])

;; dates in EDN are given in [yyyy "Month" dd] format
(defn date [[year month day]]
  (str month " " year))

(defn interval [{:keys [start end]}]
  (if (nil? end)
    (date start)
    (str (date start) " to " (date end))))

(defn experience [{:keys [professional-experience]}]
  [:div.experience.section
   [:h2 "Professional Experience"]
   [:ul.experience
    (for [entry professional-experience]
      [:li.company
       [:div
        [:a.name.h4 {:href (:website entry)}
         (escape-html (:company entry))]
        [:h4.interval (escape-html (interval entry))]
        [:h4.position (escape-html (:position entry))]]
       [:p.keywords.label1 (escape-html (->> (:keywords entry)
                                             (map name)
                                             (string/join ", ")))]
       [:ul.details (for [bullet (:bullets entry)]
                      [:li (escape-html bullet)])]])]])

(defn publications [{:keys [publications]}]
  [:div.publications.section
   [:h2 "Publications"]
   [:ul.publications
    (for [entry publications]
      [:li (escape-html entry)])]])

(defn proficiencies [{:keys [proficiencies]}]
  [:div.proficiencies.section
   [:h2 "Prof​iciencies"]
   [:ul.proficiencies
    (for [[category values] proficiencies]
      [:li
       [:b category ": "]
       (escape-html (string/join ", " values))])]])
