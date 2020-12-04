(ns advent.day4
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

;;; https://adventofcode.com/2020/day/4

(defn parse-record
  "Converts each passport record into a map."
  [input]
  (->> (str/split input #" ")
       (map #(str/split % #":"))
       (into {})))

(def records
  (->> (io/resource "day4/input.txt")
       (slurp)
       (str/split-lines)
       ;; Split into sub-sequences of empty lines (separators)
       ;; and sub-sequences of non-empty lines (the actual records)
       (partition-by empty?)
       ;; Filter out empty-line sub-sequences
       (filter #(not (empty? (first %))))
       ;; Join together lines belonging to the same record
       (map #(parse-record (str/join " " %)))))

(defn record-field-valid?
  "Does a field in the record exist and matches the value predicate?"
  [record field predicate]
  (and (contains? record field)
       (predicate (get record field))))

(defn record-valid?
  "Does a record match all field validations?"
  [record validations]
  (every?
    (fn [[field predicate]] (record-field-valid? record field predicate))
    validations))

;; Part 1 - No validations other than field existence
(def field-validations-1
  {"byr" any?
   "iyr" any?
   "eyr" any?
   "hgt" any?
   "hcl" any?
   "ecl" any?
   "pid" any?})

(defn digit?
  [c]
  (Character/isDigit c))

(defn digits?
  "Is the string a number with n digits?"
  [n string]
  (and (re-matches #"\d+" string)
       (= n (count string))))

(defn number-between?
  "Is the string a number between a and b?"
  [a b string]
  (<= a (Integer/parseInt string) b))

;; Part 2 - Extra validations for each field
(def field-validations-2
  {"byr" #(and (digits? 4 %) (number-between? 1920 2002 %))
   "iyr" #(and (digits? 4 %) (number-between? 2010 2020 %))
   "eyr" #(and (digits? 4 %) (number-between? 2020 2030 %))
   "hgt" #(let [[_ digits unit] (re-find #"(\d+)(cm|in)" %)]
            (case unit
              "cm" (number-between? 150 193 digits)
              "in" (number-between? 59 76 digits)
              false))
   "hcl" #(re-matches #"#[0-9a-f]{6}" %)
   "ecl" #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"}
   "pid" #(re-matches #"[0-9]{9}" %)})


(defn result [validations]
  (->> records
       (filter #(record-valid? % validations))
       (count)))

(def result-1 (result field-validations-1))
(def result-2 (result field-validations-2))

(defn -main []
  (println result-1)
  (println result-2))
