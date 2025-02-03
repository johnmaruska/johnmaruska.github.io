(ns pages
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as string]
   [hiccup.page :refer [html5]]
   [hiccup.util :refer [escape-html]]
   [markdown.core :as markdown]
   [resume]
   [shared :refer [head navbar *relative-to-root*]]))

(def output-root
  "Directory to dump generated HTML files, relative to project root."
  "docs")

"=============================================================================
=
=
=                  Links Page
=
=
============================================================================="

(defn img
  ([picture]
   (img {} picture))
  ([options picture]
   [:img (assoc options
                :src (:asset picture)
                :alt (escape-html (:alt-text picture)))]))

(def profile-pic
  {:asset "assets/profile_picture_512x521.png"
   ;; TODO better alt text
   :alt-text "profile pic of John. Long hair, full beard, glasses."})

(def links-title "John Maruska - Links")
(def links-html-file "links.html")
(def links-css-file "links.css")
(def links-intro-paragraph "These are all the accounts I associate with my real name. I've been full hermit/lurker mode on most of these but I'm hoping to change that soonish.")
(def links-edn-file "links.edn")

(defn links-page [links-data]
  (html5 {}
    (head {:title links-title
           :css-file links-css-file})
    [:body
     (navbar)
     [:div.column
      [:div#links-profile
       (img {:class "avatar"} profile-pic)
       [:p (escape-html links-intro-paragraph)]]
      [:ul#links
       (for [{:keys [network url icon]} links-data]
         [:li [:a {:href url :target "_blank"}
               [:i {:class icon}] (escape-html network)]])]]]))

"=============================================================================
=
=
=                  Resume Page
=
=
============================================================================="

(def resume-title "John Maruska - Resume")
(def resume-css-file "resume.css")
(def resume-html-file "resume.html")
(defn resume-page [resume-data]
  (html5 {}
    (head {:title resume-title :css-file resume-css-file})
    [:body
     (navbar)
     (resume/contents resume-data)]))


"=============================================================================
=
=
=                  Blog Post Pages
=
=
============================================================================="

(def post-hub-title "Articles")
(def post-hub-html-file "posts.html")
(def post-hub-css-file "posts.css")

(defn displayed-date [date]
  date)

(defn post-hub-page
  "Render HTML for the blogpost hub page.

  Contains a list of all posts. Not yet paginated or truncated in any way."
  [posts-meta]
  (html5 {}
    (head {:title    post-hub-title
           :css-file post-hub-css-file})
    [:body
     (navbar)
     [:h1 "Articles"]
     (for [meta posts-meta]
       (let [title   (-> meta :title first)
             date    (-> meta :date first)
             summary (-> meta :summary first)]
         [:div.post-entry
          [:div.post-header
           [:div.post-title title]
           [:div.post-date date]]
          [:div.post-summary
           summary]]))]))

"=============================================================================
=
=
=                  Blog Post Pages
=
=
============================================================================="

(def posts-directory
  "Directory for individual posts files to live.

  Markdown posts are relative to project root.
  HTML posts are relative to `output-root`"
  "posts")

(defn all-posts []
  (->> (file-seq (io/file "posts/"))
       (map (fn [x] (.toString x)))
       (filter (fn [x] (string/ends-with? x ".md")))))

(defn post-html-file
  "Name of the HTML file that will correspond to the given markdown file."
  [input-file]
  (string/replace input-file #".md$" ".html"))

(defn single-post-page
  "Render HTML for a single blogpost.

  Requires a string path to the markdown file to be rendered."
  [markdown-contents]
  (let [css-file "../blogpost.css"]
    (binding [*relative-to-root* "../"]
      (let [parsed (markdown/md-to-html-string-with-meta
                    markdown-contents
                    :reference-links? true
                    :footnotes? true)]
        (html5 {}
          (head {:title    (first (-> parsed :metadata :title))
                 :css-file css-file})
          [:body
           (navbar)
           (:html parsed)])))))
