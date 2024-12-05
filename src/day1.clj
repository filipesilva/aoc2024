(ns day1
  (:require [clojure.string :as str]
            [util :as u]))

(def re #"(\d+)\s+(\d+)")

(defn read-numbers
  [numbers-line]
  (->> (str/split numbers-line #"\s+")
       (map parse-double)
       (map int)))

(defn into-accs
  [[acc1 acc2] [n1 n2]]
  [(conj acc1 n1) (conj acc2 n2)])

(def parsed-input
  (->> "day1/input.txt"
       u/input-line-seq
       (map read-numbers)
       (reduce into-accs [[] []])))

(comment
  ;; part 1
  (->> parsed-input
       (map sort)
       (apply map -)
       (map abs)
       (reduce +))
  ;; => 1506483


  ;; part 2
  (let [[one two] parsed-input
        freqs     (frequencies two)]
    (->> one
         (map #(* %(get freqs % 0)))
         (reduce +)))
  ;; => 23126924
  ,)
