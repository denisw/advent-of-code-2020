(ns advent.day2
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def lines
  (->> (io/resource "day2/input.txt")
       (slurp)
       (str/split-lines)))

(defn split-policy-spec [spec]
  (let [[_ n1 n2 letter] (re-matches #"(\d+)-(\d+) (\w)" spec)]
    [(Integer/parseInt n1)
     (Integer/parseInt n2)
     (first letter)]))

(defn old-policy-parser [spec]
  (let [[n1 n2 letter] (split-policy-spec spec)]
    (fn [password]
      (let [letter-frequencies (frequencies password)]
        (<= n1 (get letter-frequencies letter 0) n2)))))

(defn new-policy-parser [spec]
  (let [[n1 n2 letter] (split-policy-spec spec)]
    (fn [password]
      (let [c1 (get password (- n1 1))
            c2 (get password (- n2 1))]
        (or (and (= c1 letter) (not= c2 letter))
            (and (= c2 letter) (not= c1 letter)))))))

(defn result [policy-parser]
  (->> lines
       (map #(str/split % #": " 2))
       (map #(update % 0 policy-parser))
       (map (fn [[policy password]] (policy password)))
       (filter true?)
       (count)))

(def result-1 (result old-policy-parser))
(def result-2 (result new-policy-parser))

(defn -main []
  (println result-1)
  (println result-2))
