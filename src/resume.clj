(ns resume
  (:require
   [clojure.string :as string]
   [hiccup.page :refer [html5]]
   [clojure.edn :as edn]
   [clojure.java.io :as io]))

(defn head [{:keys [basics]}]
  [:head
   [:meta {:name "description" :content (str (:name basics) " - " (:label basics))}]
   [:meta {:name "keywords" :content (map name (:meta-keywords basics))}]
   [:meta {:name "viewport" :content "initial-scale=1,width=device-width"}]
   [:meta {:content "no-cache" :http-equiv "cache-control"}]
   [:link {:rel "stylesheet" :href "styles.css" :type "text/css"}]
   [:link {:rel "stylesheet" :href "https://fonts.googleapis.com/css?family=Space+Mono|Muli"}]
   [:link {:rel "stylesheet" :href "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"}]
   [:title (str (:name basics) ". " (:label basics))]])

(defn introduction [{:keys [basics]}]
  (list
   [:h1.name (:name basics)]
   [:p.role (:label basics)]
   [:p.location (:location basics)]))

(def pdf-link
  [:a.pdf-link {:href "resume.pdf"} [:i {:class "fa fa-file-pdf-o"}]])

(defn links [{:keys [basics]}]
  (for [profile (:contact-links basics)]
    [:a {:href (:url profile)}
     [:i {:class (str "fa fa-" (:icon profile))}]]))

(defn summary [{:keys [basics]}]
  (list
   [:h2 "Summary"]
   [:p.summary (:content (:summary basics))]))

(defn education [{:keys [education]}]
  (list
   [:h2 (:title education)]
   [:ul.education
    (for [entry (:content education)]
      [:li
       [:p
        (str (:study-type entry) " in " (:area entry))
        [:br] [:a {:href (:website entry)} (:institution entry)]]])]))

(defn today [] (.format (java.text.SimpleDateFormat. "MMMM d, YYYY") (java.util.Date.)))
(defn updated []
  [:div.updated [:p (str "updated:" (today))]])

(defn sidebar [{:keys [basics] :as resume-data}]
  [:div.sidebar
   [:div.sidebar-content
    (introduction resume-data)
    (links resume-data)
    pdf-link
    [:hr.divider]
    (summary resume-data)
    (education resume-data)]
   (updated)])

;; dates in EDN are given in [yyyy "Month" dd] format
(defn date [[year month day]]
  (str month " " year))
(defn interval [{:keys [start end]}]
  (if (nil? end)
    (date start)
    (str (date start) " to " (date end))))

(defn experience [{:keys [fulltime-experience]}]
  (list
   [:h2 (:title fulltime-experience)]
   [:ul.experience
    (for [entry (:content fulltime-experience)]
      [:li.company
       [:a.name {:href (:website entry)} (:company entry)]
       [:div.position
        [:p.position (:position entry)] [:p.interval (interval entry)]]
       [:p.keywords (string/join ", " (map name (:keywords entry)))]
       #_ [:div.details
           [:p (or (:highlight entry) "TODO")]
           [:ul.details (for [bullet (:bullets entry)]
                          [:li bullet])]]
       [:ul.details (for [bullet (:bullets entry)]
                      [:li bullet])]])]))

(defn render [resume-data]
  (html5
   (head resume-data)
   [:body
    (sidebar resume-data)
    [:div.main
     [:div.main-content
      (experience resume-data)]]]))

(defn page []
  (render (edn/read-string (slurp (io/resource "experience.edn")))))
