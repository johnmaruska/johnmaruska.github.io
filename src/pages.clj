(ns pages
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [clojure.string :as string]
   [hiccup.page :refer [html5]]
   [hiccup.util :refer [escape-html]]
   [markdown.core :as markdown]
   [resume]
   [shared :refer [head header-bar *relative-to-root*]]))

(def output-root
  "Directory to dump generated HTML files, relative to project root."
  "docs")

"=============================================================================
=
=
=                  Home Page
=
=
============================================================================="

(def home-title "John Maruska - Home")
(def home-html-file "index.html")
(defn home-page []
  (html5 {}
    (head {:title home-title})
    [:body#homepage
     (header-bar)
     [:div.contents
      [:h2 "Hello!"]
      [:p "My name is John Maruska. Professionally I'm a software
    engineer, most of my experience has been on backend data
    ingestion systems and my favorite language Clojure. Personally,
    I'm interested in heavy metal, cheesy monster movies, VTubers,
    learning bass guitar, and playing co-operative games with
    friends."]
      [:p "This website is in the first pass at what I'm hoping will be
    a long-lived personal blogging endeavor. I enjoy reading and
    learning about software development, and I'm hoping this
    website can act as a means to push myself into doing that more
    actively and get better retention out of my efforts."]
      [:p "I don't have any posts yet, so this area mostly serves as a
    placeholder until that happens. Hopefully I'll have something
    to show you soon."]]]))

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
  {:asset    "assets/profile_picture_512x521.png"
   ;; TODO better alt text
   :alt-text "profile pic of John. Long hair, full beard, glasses."})

(def links-title "John Maruska - Links")
(def links-html-file "links.html")
(def links-intro-paragraph "These are all the accounts I associate with my real name. I've been full hermit/lurker mode on most of these but I'm hoping to change that soonish.")
(def links-edn-file "links.edn")

(defn links-page [links-data]
  (html5 {}
    (head {:title links-title})
    [:body#links
     (header-bar)
     [:div.column
      [:div.links-profile
       (img {:class "avatar"} profile-pic)
       [:p (escape-html links-intro-paragraph)]]
      [:ul.links
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
(def resume-html-file "resume.html")
(defn resume-page [resume-data]
  (html5 {}
    (head {:title resume-title})
    [:body#resume
     (header-bar)
     [:div.sidebar
      [:div.sidebar-content
       (resume/introduction resume-data)
       (resume/links resume-data)
       [:hr.divider]
       (resume/summary resume-data)]]
     [:div.main
      [:div.main-content
       (resume/experience resume-data)
       (resume/education resume-data)
       (resume/publications resume-data)
       (resume/proficiencies resume-data)]]
     (resume/updated)]))


"=============================================================================
=
=
=                  Blog Post Pages
=
=
============================================================================="

(def post-hub-title "Articles")
(def post-hub-html-file "posts.html")

(defn displayed-date [date]
  date)

(defn post-hub-page
  "Render HTML for the blogpost hub page.

  Contains a list of all posts. Not yet paginated or truncated in any way."
  [posts-meta]
  (html5 {}
    (head {:title post-hub-title})
    [:body#post-hub
     (header-bar)
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
  (binding [*relative-to-root* "../"]
    (let [parsed (markdown/md-to-html-string-with-meta
                  markdown-contents
                  :reference-links? true
                  :footnotes? true)]
      (html5 {}
        (head {:title (first (-> parsed :metadata :title))})
        [:body.blogpost
         (header-bar)
         (:html parsed)]))))
