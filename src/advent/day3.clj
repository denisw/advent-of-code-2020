(ns advent.day3
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def tree-grid
  (->> (io/resource "day3/input.txt")
       (slurp)
       (str/split-lines)
       (map (partial into []))
       (into [])))

(def num-rows (count tree-grid))
(def last-row (- num-rows 1))
(def num-cols-per-cycle (count (first tree-grid)))

(defn tree? [[row col]]
  (let [col-in-cycle (mod col num-cols-per-cycle)]
    (= \# (get-in tree-grid [row col-in-cycle]))))

(defn arrived? [[row col]]
  (> row last-row))

(defn slope [right down]
  (fn [[row col]] [(+ row down) (+ col right)]))

(defn num-trees-for-slope [right down]
  (->> (iterate (slope right down) [0 0])
       (take-while (complement arrived?))
       (filter tree?)
       (count)))

(def result-1
  (num-trees-for-slope 3 1))

(def result-2
  (* (num-trees-for-slope 1 1)
     (num-trees-for-slope 3 1)
     (num-trees-for-slope 5 1)
     (num-trees-for-slope 7 1)
     (num-trees-for-slope 1 2)))

(defn -main []
  (println result-1)
  (println result-2))
