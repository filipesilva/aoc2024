(ns day-03
  (:require [util :as u]))

@(def parsed-input
  (->>
   "day-03/input.txt"
   ;; "day-03/example.txt"
   ;; "day-03/example2.txt"
   u/input-line-seq))

(def re #"mul\((\d{1,3}),(\d{1,3})\)")

@(def re2 (re-pattern (str re #"|do\(\)|don't\(\)")))
#"mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)"

(defn op [s]
  (case (#{"do(" "don"} (subs (first s) 0 3))
    "do(" :do
    "don" :don't
    nil   :mul))

(defn eat-ops [m item]
  (condp = (op (first item))
    :do (assoc m :do? true)
    :don't (assoc m :do? false)
    :mul  (cond-> m
             (:do? m) (update :muls into item))))

(comment
  ;; part 1
  (->> parsed-input
       (mapcat #(re-seq re %))
       (map #(drop 1 %))
       (u/mmap u/parse-int)
       (map (partial apply *))
       (reduce +))
  ;; => 171183089

  ;; part 2
  (->> parsed-input
       (mapcat #(re-seq re2 %))
       (partition-by op)
       (reduce eat-ops {:do? true, :muls []})
       :muls
       (map #(drop 1 %))
       (u/mmap u/parse-int)
       (map (partial apply *))
       (reduce +)
       ,)
  ;; => 63866497

  ,)
