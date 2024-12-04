(ns day1
  (:require [clojure.java.io :as io]
            [clojure.string :as str])

  )

(def input (io/resource "day1/input.txt"))

(def re #"(\d+)\s+(\d+)")

(defn read-numbers
  [numbers-line]
  (->> (str/split numbers-line #"\s+")
       (map parse-double)))

(defn into-accs
  [[acc1 acc2] [n1 n2]]
  [(conj acc1 n1) (conj acc2 n2)])


(comment
  (with-open [rdr (io/reader (io/resource "day1/input.txt"))]
    (->> rdr
         line-seq
         ;; doall ;; not needed after adding reduce
         ;; (take 10)
         (map read-numbers)
         (reduce into-accs [[] []])
         (map sort)
         (apply map -)
         (map abs)
         (reduce +)
         int))
  ;; => 1506483

  ,)
