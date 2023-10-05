(ns generate
  (:require
   [links]
   [resume])
  (:gen-class))


(defn generate! [location f]
  (println "Generating" location)
  (spit location (f)))

(defn -main []
  ;; Generate resume. Going to index html until I'm doing more with
  ;; the website and figure out routing.
  (doseq [[location f] [["docs/resume.html" resume/page]
                        ["docs/links.html" links/page]]]
    (generate! location f)))
