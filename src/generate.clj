(ns generate
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]
   [markdown.core :as markdown]
   [pages]
   [resume])
  (:gen-class))

(defn write! [output-file contents]
  (let [dest (str pages/output-root "/" output-file)]
    (println "Generating" dest)
    (spit dest contents)))

(defn -main []
  (write! pages/home-html-file (pages/home-page))
  ;; Generate resume page
  (let [resume-data (->> (edn/read-string (slurp (io/resource "links.edn")))
                         (filter :resume?)
                         (assoc-in (edn/read-string (slurp (io/resource "experience.edn")))
                                   [:basics :contact-links]))]
    (write! pages/resume-html-file (pages/resume-page resume-data)))

  ;; Generate links page
  (let [links-data (edn/read-string (slurp (io/resource pages/links-edn-file)))]
    (write! pages/links-html-file (pages/links-page links-data)))

  ;; Generate post hub
  (write! pages/post-hub-html-file (pages/post-hub-page (pages/all-posts)))

  ;; Generate single post
  (doseq [post-md-file (pages/all-posts)]
    (write! (pages/post-html-file post-md-file)
            (pages/single-post-page (slurp post-md-file)))))
