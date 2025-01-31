(ns resume
  (:require
   [shared :refer [head]]
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as string]
   [hiccup.page :refer [html5]]
   [hiccup.util :refer [escape-html]]))

(def title "John Maruska - Resume")
(def css-file "resume.css")

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
  [:div.education
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

(defn sidebar [{:keys [basics] :as resume-data}]
  [:div.sidebar
   [:div.sidebar-content
    (introduction resume-data)
    (links resume-data)
    [:hr.divider]
    (summary resume-data)]])

;; dates in EDN are given in [yyyy "Month" dd] format
(defn date [[year month day]]
  (str month " " year))
(defn interval [{:keys [start end]}]
  (if (nil? end)
    (date start)
    (str (date start) " to " (date end))))

(defn experience [{:keys [professional-experience]}]
  [:div.experience
   [:h2 "Professional Experience"]
   [:ul.experience
    (for [entry professional-experience]
      [:li.company
       [:a.name {:href (:website entry)} (escape-html (:company entry))]
       [:div.position
        [:p.position (escape-html (:position entry))]
        [:p.interval (escape-html (interval entry))]]
       [:p.keywords (escape-html (string/join ", " (map name (:keywords entry))))]
       [:ul.details (for [bullet (:bullets entry)]
                      [:li (escape-html bullet)])]])]])

(defn publications [{:keys [publications]}]
  [:div.publications
   [:h2 "Publications"]
   [:ul.publications
    (for [entry publications]
      [:li (escape-html entry)])]])

(defn proficiencies [{:keys [proficiencies]}]
  [:div.proficiencies
   [:h2 "Prof​iciencies"]
   [:ul.proficiencies
    (for [[category values] proficiencies]
      [:li
       [:b category ": "]
       (escape-html (string/join ", " values))])]])

(defn render [resume-data]
  (html5 {}
    (head {:title title :css-file css-file})
    [:body
     (sidebar resume-data)
     [:div.main
      [:div.main-content
       (experience resume-data)
       (education resume-data)
       (publications resume-data)
       (proficiencies resume-data)]]
     (updated)]))

(defn fetch-data []
  (->> (edn/read-string (slurp (io/resource "links.edn")))
       (filter :resume?)
       (assoc-in (edn/read-string (slurp (io/resource "experience.edn")))
                 [:basics :contact-links])))

(defn page-html []
  (render (fetch-data)))
