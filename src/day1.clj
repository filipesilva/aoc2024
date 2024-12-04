(ns day1
  (:require [clojure.java.io :as io]
            [clojure.string :as str])

  )

(def input (io/resource "day1/input.txt"))

(def re #"(\d+)\s+(\d+)")

(defn read-numbers
  [numbers-line]
  (->> (str/split numbers-line #"\s+")
       (map parse-double)
       (map int)))

(defn into-accs
  [[acc1 acc2] [n1 n2]]
  [(conj acc1 n1) (conj acc2 n2)])

(defn read-all [rdr]
  (->> rdr
       line-seq
       doall ;; not needed after adding reduce
       ;; (take 100)
       (map read-numbers)
       (reduce into-accs [[] []])))

(comment
  ;; part 1
  (with-open [rdr (io/reader (io/resource "day1/input.txt"))]
    (->> rdr
         read-all
         (map sort)
         (apply map -)
         (map abs)
         (reduce +)))
  ;; => 1506483


  ;; part 2
  (with-open [rdr (io/reader (io/resource "day1/input.txt"))]
    (let [[one two] (read-all rdr)
          freqs     (frequencies two)]
      (->> one
           (map #(* %(get freqs % 0)))
           (reduce +))))
  ;; => 23126924
  ,)
