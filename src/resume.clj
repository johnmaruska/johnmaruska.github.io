(ns resume
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as string]
   [hiccup.page :refer [html5]]))

(defn head [{:keys [basics]}]
  [:head
   ;; Shared
   [:meta {:name "viewport" :content "initial-scale=1,width=device-width"}]
   [:meta {:content "no-cache" :http-equiv "cache-control"}]
   [:link {:rel "stylesheet" :href "https://fonts.googleapis.com/css?family=Space+Mono|Muli"}]
   [:link {:rel "stylesheet" :href "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"}]
   ;; Unique
   [:link {:rel "stylesheet" :href "resume.css" :type "text/css"}]
   [:title "John Maruska - Resume"]])

(defn introduction [{:keys [basics]}]
  [:div.introduction
   [:h1.name (:name basics)]
   [:p.role (:label basics)]
   [:p.location (:location basics)]])

(def pdf-link
  [:a.pdf-link {:href "resume.pdf"} [:i {:class "fa fa-file-pdf-o"}]])

(defn links [{:keys [basics]}]
  [:div.links
   (for [{:keys [url icon]} (:contact-links basics)]
     [:a {:href url}
      [:i {:class icon}]])
   ;; pdf-link  ; uncomment when pdf TODO is done.
   ])

(defn summary [{:keys [basics]}]
  [:div.summary
   [:h2 "Summary"]
   [:p.summary (:content (:summary basics))]])

(defn education [{:keys [education]}]
  [:div.education
   [:h2 "Education"]
   [:ul.education
    (for [entry education]
      [:li
       [:p
        (str (:study-type entry) " in " (:area entry)) [:br]
        [:a {:href (:website entry)} (:institution entry)]]])]])

(defn today [] (.format (java.text.SimpleDateFormat. "MMMM d, YYYY") (java.util.Date.)))
(defn updated []
  [:div.updated [:p (str "updated:" (today))]])

(defn sidebar [{:keys [basics] :as resume-data}]
  [:div.sidebar
   [:div.sidebar-content
    (introduction resume-data)
    (links resume-data)
    [:hr.divider]
    (summary resume-data)
    (education resume-data)]])

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
       [:a.name {:href (:website entry)} (:company entry)]
       [:div.position
        [:p.position (:position entry)] [:p.interval (interval entry)]]
       [:p.keywords (string/join ", " (map name (:keywords entry)))]
       #_ [:div.details
           [:p (or (:highlight entry) "TODO")]
           [:ul.details (for [bullet (:bullets entry)]
                          [:li bullet])]]
       [:ul.details (for [bullet (:bullets entry)]
                      [:li bullet])]])]])

(defn publications [{:keys [publications]}]
  [:div.publications
   [:h2 "Publications"]
   [:ul.publications
    (for [entry publications]
      [:li entry])]])

(defn render [resume-data]
  (html5
   (head resume-data)
   [:body
    (sidebar resume-data)
    [:div.main
     [:div.main-content
      (experience resume-data)
      (publications resume-data)]]
    (updated)]))

(defn resume-links []
  (filter :resume? (edn/read-string (slurp (io/resource "links.edn")))))

(defn resume-data []
  (assoc-in (edn/read-string (slurp (io/resource "experience.edn")))
            [:basics :contact-links] (resume-links)))

(defn page []
  (render (resume-data)))
