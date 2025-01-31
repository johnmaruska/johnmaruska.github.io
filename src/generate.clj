(ns generate
  (:require
   [links]
   [post]
   [resume])
  (:gen-class))

(defn -main []
  ;; Generate resume. Going to index html until I'm doing more with
  ;; the website and figure out routing.
  (doseq [[location f] [["docs/resume.html" resume/page-html]
                        ["docs/links.html" links/page-html]]]
    (println "Generating" location)
    (spit location (f)))
  (doseq [post (post/all-posts)]
    (let [input-file  (.toString post)
          output-file (post/output-file input-file)]
      (println "Generating" output-file)
      (spit output-file (post/page-html input-file)))))
