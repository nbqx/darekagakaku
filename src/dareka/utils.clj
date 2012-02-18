(ns dareka.utils
  (:require [clj-time.core :as ct]
            [clj-time.format :as fmt]))

(def url-format (fmt/formatter (ct/time-zone-for-id "Asia/Tokyo") "yyyy-MM-dd" "yyyyMMdd"))
(def jp-format (fmt/formatter (ct/time-zone-for-id "Asia/Tokyo") "yyyy年MM月dd日" "yyyy-MM-dd"))

(defn jp-now []
  (let [now (ct/now)]
    (ct/to-time-zone now (ct/time-zone-for-id "Asia/Tokyo"))))

(defn jp-date [date]
  (let [dt (fmt/parse url-format date)]
    (fmt/unparse jp-format dt)))

(defn get-utc [date]
  (fmt/parse url-format date))

(defn todays-url []
  (let [now (jp-now)]
    (fmt/unparse url-format now)))

