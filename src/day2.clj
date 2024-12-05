(ns day2
  (:require [clojure.string :as str]
            [util :as u]))

@(def parsed-input
  (->>
   "day2/input.txt"
   ;; "day2/example.txt"
   u/input-line-seq
   (map #(str/split % #"\s+"))
   (u/mmap (comp int parse-double))))

(defn deltas [reading]
  (->> reading
       (partition 2 1)
       (map #(apply - %))))

(defn drop-nth [n coll]
  (into (vec (take n coll))
        (drop (inc n) coll)))

(defn explode [reading]
  (map #(drop-nth % reading)
       (range (count reading))))

(defn safe? [reading]
  (and (or (every? pos? reading)
           (every? neg? reading))
       (->> reading
            (map abs)
            (every? #{1 2 3}))))

(comment
  ;; part 1
  (->> parsed-input
       (map deltas)
       (filter #(or (every? pos? %) (every? neg? %)))
       (u/mmap abs)
       (filter #(every? #{1 2 3} %))
       count)
  ;; => 390

  ;; part 2
  (->> parsed-input
       (map explode)
       (u/mmap deltas)
       (keep #(some safe? %))
       count)
  ;; => 439

  ,)
