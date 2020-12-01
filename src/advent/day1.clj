(ns advent.day1
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def expenses
  (->> (io/resource "day1/input.txt")
       (slurp)
       (str/split-lines)
       (map #(Integer/parseInt %))))

(def expense-pairs
  (for [x expenses
        y expenses
        :when (< x y)]
    [x y]))

(def expense-triples
  (for [x expenses
        y expenses
        z expenses
        :when (< x y z)]
    [x y z]))

(defn find-matching-expense-tuple
  [tuples]
  (->> tuples
       (filter #(= 2020 (apply + %)))
       (first)))

(defn result
  [expense-tuples]
  (->> expense-tuples
       (find-matching-expense-tuple)
       (apply *)))

(def result-for-pairs (result expense-pairs))
(def result-for-triples (result expense-triples))

(defn -main []
  (println result-for-pairs)
  (println result-for-triples))
