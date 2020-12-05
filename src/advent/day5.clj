(ns advent.day5
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def num-rows 128)
(def num-cols 8)
(def highest-row (- num-rows 1))
(def highest-col (- num-cols 1))

(defn average
  [x y]
  (/ (+ x y) 2))

(defn floor
  [x]
  (int (Math/floor (float x))))

(defn ceil
  [x]
  (int (Math/ceil (float x))))

(defn lower-half
  [[lower upper]]
  (if (= 1 (- upper lower))
    lower
    (let [new-upper (floor (average lower upper))]
      [lower new-upper])))

(defn upper-half
  [[lower upper]]
  (if (= 1 (- upper lower))
    upper
    (let [new-lower (ceil (average lower upper))]
      [new-lower upper])))

(defn seat-char-reducer
  [[row-range col-range] c]
  (case c
    \F [(lower-half row-range) col-range]
    \B [(upper-half row-range) col-range]
    \L [row-range (lower-half col-range)]
    \R [row-range (upper-half col-range)]))

(defn parse-seat
  [string]
  (reduce seat-char-reducer
          [[0 highest-row] [0 highest-col]]
          string))

(defn seat-id
  [[row col]]
  (+ (* row 8) col))

(def boarding-pass-seats
  (->> (io/resource "day5/input.txt")
       (slurp)
       (str/split-lines)
       (map parse-seat)))

(def all-seats
  (for [row (range 0 num-rows)
        col (range 0 num-cols)]
    [row col]))

(def free-seats
  (clojure.set/difference (set all-seats)
                          (set boarding-pass-seats)))

(def boarding-pass-seat-ids
  (->> boarding-pass-seats
       (map seat-id)
       (set)))

(def result-1
  (->> boarding-pass-seats
       (map seat-id)
       (apply max)))

(def result-2
  (->> free-seats
       (map seat-id)
       (filter #(and (contains? boarding-pass-seat-ids (+ % 1))
                     (contains? boarding-pass-seat-ids (- % 1))))
       (first)))

(defn -main []
  (println result-1)
  (println result-2))
