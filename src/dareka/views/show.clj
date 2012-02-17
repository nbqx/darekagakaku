(ns dareka.views.show
  (:require [dareka.utils :as utils]
            [dareka.views.common :as common])
  (:use [noir.core :only [defpage]]
        [hiccup.core :only [html]]
        [hiccup.page-helpers :only [link-to]]))

(defn editable? [date]
  (let [now (utils/todays-url)]
    (if (= date now)
      (html " | " (link-to {:id "edit-btn"} "/w" "EditMe")) "")))

(defn show [data]
  (let [date (:date data)
        text (:content data)]
    (common/layout
     [:div.content
      [:h1 (utils/jp-date date)]
      (common/add-p-tags text)
      [:div.right
       (link-to "/a" "About")
       (editable? date)]])))