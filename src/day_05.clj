(ns day-05
  (:require [clojure.string :as str]
            [datascript.core :as d]
            [util :as u]))

(defn parse-input [file]
   (let [[rules _ updates] (->> file u/input-line-seq (partition-by empty?))]
     {:rules   (->> rules
                    (map #(str/split % #"\|"))
                    (u/mmap u/parse-int)
                    (map (fn [[x y]] [x :before y])))
      :updates (->> updates
                    (map #(str/split % #","))
                    (u/mmap u/parse-int))}))

(defn vswap [coll idx1 idx2]
  (let [v (vec coll)]
    (-> v
        (assoc idx1 (get v idx2))
        (assoc idx2 (get v idx1)))))

(defn next-idx [coll idx1 idx2]
  (let [last-idx (dec (count coll))]
    (cond
      (= idx1 (dec last-idx)) nil
      (= idx2 last-idx)       [(inc idx1) (+ idx1 2)]
      :else                   [idx1 (inc idx2)])))

(defn debug
  "Call in rule as [(day-05/debug ?x ?y ?z)]"
  [& args]
  (apply println args)
  true)

(def datascript-rules
  "see datascript.built-ins/query-fns for existing fns"
  '[;; ordered
    [(ordered ?coll)
     [(empty? ?coll)]]
    [(ordered ?coll)
     (head-and-tail ?coll ?head ?tail)
     (before-all ?head ?tail)
     (ordered ?tail)]

    ;; before-all
    [(before-all ?_ ?ys)
     [(empty? ?ys)]]
    [(before-all ?x ?ys)
     (head-and-tail ?ys ?head ?tail)
     (not [?head :before ?x])
     (before-all ?x ?tail)]

    ;; middle-page
    [(middle-page ?coll ?m)
     [(count ?coll) ?c]
     [(odd? ?c)]
     [(quot ?c 2) ?n]
     [(clojure.core/nth ?coll ?n) ?m]]

    ;; reorder, for part 2
    [(reorder ?coll ?reordered-coll)
     (ordered ?coll)
     [(ground ?coll) ?reordered-coll]]
    [(reorder ?coll ?reordered-coll)
     (not (ordered ?coll))
     (swap-next-wrong ?coll 0 1 ?swapped)
     (reorder ?swapped ?reordered-coll)]

    ;; swap-next-wrong, for part 2
    [(swap-next-wrong ?coll ?idx1 ?idx2 ?swapped)
     (get-idxs ?coll ?idx1 ?v1 ?idx2 ?v2)
     [?v2 :before ?v1]
     [(day-05/vswap ?coll ?idx1 ?idx2) ?swapped]]
    [(swap-next-wrong ?coll ?idx1 ?idx2 ?swapped)
     (get-idxs ?coll ?idx1 ?v1 ?idx2 ?v2)
     (not [?v2 :before ?v1])
     (next-idxs ?coll ?idx1 ?next-idx1 ?idx2 ?next-idx2)
     (swap-next-wrong ?coll ?next-idx1 ?next-idx2 ?swapped)]

    ;; helpers
    [(head-and-tail ?coll ?head ?tail)
     [(clojure.core/first ?coll) ?head]
     [(clojure.core/rest ?coll) ?tail]]
    [(get-idxs ?coll ?idx1 ?v1 ?idx2 ?v2)
     [(clojure.core/vec ?coll) ?vcoll]
     [(get ?vcoll ?idx1) ?v1]
     [(get ?vcoll ?idx2) ?v2]]
    [(next-idxs ?coll ?idx1 ?next-idx1 ?idx2 ?next-idx2)
     [(day-05/next-idx ?coll ?idx1 ?idx2) [?next-idx1 ?next-idx2]]]])

(defn q1 [file]
  (let [parsed-input (parse-input file)]
    (d/q '[:find (sum ?m) .
           :with ?update
           :in   $ % [?update ...]
           :where
           (ordered ?update)
           (middle-page ?update ?m)]
         (:rules parsed-input)
         datascript-rules
         (:updates parsed-input))))

(defn q2 [file]
  (let [parsed-input (parse-input file)]
    (d/q '[:find  (sum ?m) .
           :with  ?update
           :in    $ % [?update ...]
           :where
           (not (ordered ?update))
           (reorder ?update ?reordered-update)
           (middle-page ?reordered-update ?m)]
         (:rules parsed-input)
         datascript-rules
         (:updates parsed-input))))

(comment
  ;; dev
  (d/q '[:find  ?update ?m
         :in    $ % [?update ...]
         :where
         (ordered ?update)
         (middle-page ?update ?m)]
       [[1 :before 2]
        [2 :before 3]
        [3 :before 4]]
       datascript-rules
       [ ;; good
        [1 2 3]
        [1]
        [1 3 4]
        [4 1 1]
        ;; bad
        [3 2 1]
        [3 2 2]])
  ;; => #{[[4 1 1] 1] [[1 3 4] 3] [[1 2 3] 2] [[1] 1]}

  ;; part 1 example
  (q1 "day-05/example.txt")
  ;; => 143

  ;; part 1
  (q1 "day-05/input.txt")
  ;; => 4569

  ;; part 2 example
  (q2 "day-05/example.txt")
  ;; => 123

  ;; part 2 example
  (q2 "day-05/input.txt")
  ;; too slow

  ,)
