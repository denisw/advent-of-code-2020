(ns advent.day6
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def answer-groups
  (->> (io/resource "day6/input.txt")
       (slurp)
       (str/split-lines)
       (partition-by empty?)
       (filter #(not (empty? (first %))))
       (map #(map set %))))

(def result-1
  (->> answer-groups
     (map #(apply clojure.set/union %))
     (map count)
     (reduce +)))

(def result-2
  (->> answer-groups
     (map #(apply clojure.set/intersection %))
     (map count)
     (reduce +)))

(defn -main []
  (println result-1)
  (println result-2))
