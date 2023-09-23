(ns generate
  (:require [resume])
  (:gen-class))

(defn -main []
  ;; Generate resume. Going to index html until I'm doing more with
  ;; the website and figure out routing.
  (let [location "docs/resume.html"]
    (println "Generating" location)
    (spit location (resume/page))))
