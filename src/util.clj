(ns util
  (:require [clojure.java.io :as io]))

(defn input-line-seq [resource]
  (with-open [rdr (io/reader (io/resource resource))]
    (->> rdr
         line-seq
         doall)))

(comment
  (input-line-seq "day1/input.txt")
  )
