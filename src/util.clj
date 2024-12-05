(ns util
  (:require [clojure.java.io :as io]
            hashp.core))

(defn input-line-seq [resource]
  (with-open [rdr (io/reader (io/resource resource))]
    (->> rdr
         line-seq
         doall)))

(comment
  (input-line-seq "day-01/input.txt")
  )

(defn mmap [f coll]
  (map #(map f %) coll))

(defn parse-int [s]
  (-> s parse-double int))
