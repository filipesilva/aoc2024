(ns day-04
  (:require [util :as u]))

(def parsed-input
  (->>
   "day-04/input.txt"
   #_"day-04/example.txt"
   u/input-line-seq
   vec ;; needed for get-in over vec
   ))

(defn c
  "c is for coord"
  [row col row+ col+]
  [(+ row row+) (+ col col+)])

(defn xmas-explode
  ; explode to all directions, length xmas
  [[row col]]
  (let [c (partial c row col)]
    [[(c 0 0) (c -1 0)  (c -2 0)  (c -3 0) ] ; up
     [(c 0 0) (c 1 0)   (c 2 0)   (c 3 0)  ] ; down
     [(c 0 0) (c 0 -1)  (c 0 -2)  (c 0 -3) ] ; left
     [(c 0 0) (c 0 1)   (c 0 2)   (c 0 3)  ] ; right
     [(c 0 0) (c -1 -1) (c -2 -2) (c -3 -3)] ; up-left
     [(c 0 0) (c -1 1)  (c -2 2)  (c -3 3) ] ; up-right
     [(c 0 0) (c 1 -1)  (c 2 -2)  (c 3 -3) ] ; down-left
     [(c 0 0) (c 1 1)   (c 2 2)   (c 3 3)  ] ; down-right
     ]))

(defn x-string
  [[row col]]
  (let [c (partial c row col)]
    [(c -1 -1) (c 0 0)  (c 1 1)  ; down-left
     (c -1 1)  (c 0 0)  (c 1 -1) ; down-right
     ]))

(defn vlookup
  [v]
  (->> v
       (map (partial get-in parsed-input))
       (apply str)))

(defn to-coords
  [coll]
  (map-indexed
        (fn [row str]
          (map-indexed (fn [col _]
                         [row col])
                       str))
        coll))

(comment
  ;; part 1
  (->> parsed-input
       to-coords
       (apply concat)
       (mapcat xmas-explode)
       (map vlookup)
       (filter #(= % "XMAS"))
       count)
  ;; => 2599

  ;; part 2
  (->> parsed-input
       to-coords
       (apply concat)
       (map x-string) ; not mapcat!
       (map vlookup)
       (filter #{"MASMAS" "MASSAM" "SAMSAM" "SAMMAS"})
       count)
  ;; => 1948

  ,)
