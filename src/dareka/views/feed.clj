(ns dareka.views.feed
  (:require [dareka.utils :as utils])
  (:use [noir.core :only [defpage]]
        [dareka.views.common :only [add-p-tags] :as common]
        [dareka.models.post :as post]
        [hiccup.core]
        [hiccup.page-helpers]))

(defn make-entry [en]
  (let [date (:date en)
        title (utils/jp-date (:date en))
        content (common/add-p-tags (:content en))]
    [:entry
     [:id (str "http://darekagakaku.herokuapp.com/v/" date)]
     [:published (utils/get-utc date)]
     [:updated (utils/get-utc date)]
     [:link {:rel "alternate" :type "text/html" :href (str "http://darekagakaku.herokuapp.com/v/" date)}]
     [:title title]
     [:content {:type "html"} content]
     [:author
      [:name "anyone"]]]))

(defpage "/feed" {}
  (let [entries (post/get-recent 10)
        most-recent (:date (first entries))]
    (html
     (xml-declaration "UTF-8")
     [:feed {"xml:lang" "ja-JP" :xmlns "http://www.w3.org/2005/Atom"}
      [:id "http://darekagakaku.herokuapp.com/"]
      [:link {:rel "alternate" :type "text/html" :href "http://darekagakaku.herokuapp.com/"}]
      [:link {:rel "self" :type "application/atom+xml" :href "http://darekagakaku.herokuapp.com/feed"}]
      [:title "自分が書かなければおそらく誰かが書く日記"]
      [:subtitle "diary"]
      [:updated (utils/get-utc most-recent)]
      [:author
       [:name "anyone"]]
      (map make-entry entries)])))