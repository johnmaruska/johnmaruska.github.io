(ns post
  (:require
   [shared :refer [head *relative-to-root*]]
   [hiccup.page :refer [html5]]
   [markdown.core :as markdown]
   [clojure.java.io :as io]
   [clojure.string :as string]))


(def css-directory "../")
(def css-file "../blogpost.css")
(def input-directory "posts/")
(def output-directory "docs/posts/")

(defn all-posts []
  (->> (file-seq (io/file "posts/"))
       (filter (fn [x] (string/ends-with? (.toString x) ".md")))))

(defn output-file [input-file]
  (->  input-file
       (string/replace (re-pattern (str "^" input-directory)) output-directory)
       (string/replace #".md$" ".html")))

(defn render [contents]
  (binding [*relative-to-root* "../"]
    (let [{:keys [html metadata]} (markdown/md-to-html-string-with-meta
                                   contents
                                   :reference-links? true
                                   :footnotes? true)
          {:keys [author date title]} metadata]
      (html5 {}
        (head {:title (first title)
               :css-file css-file
               :css-directory css-directory})
        [:body html]))))

(defn page-html [input-file]
  (render (slurp input-file)))
