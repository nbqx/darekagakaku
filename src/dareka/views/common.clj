(ns dareka.views.common
  (:require clojure.string)
  (:use [noir.core :only [defpartial]]
        [hiccup.core :only [html]]
        [hiccup.page-helpers :only [include-css include-js link-to html5]]))

(defn add-p-tags [text]
  (let [lines (clojure.string/split-lines text)]
    (clojure.string/join (map (fn [x] (html [:p x])) lines))))

(defpartial layout [& content]
            (html5
              [:head
               "<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />"
               "<meta content='user-scalable=no,width=480,maximum-scale=1.0' name='viewport' />"
               [:title "自分が書かなければおそらく誰かが書く日記"]
               (include-css "/css/app.css")]
              [:body
               [:div#main content]]))
