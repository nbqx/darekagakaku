(ns dareka.views.write
  (:require [dareka.utils :as utils]
            [dareka.views.common :as common])
  (:use [noir.core]
        [noir.response :only [redirect]]
        [dareka.models.post :as post]
        [hiccup.page-helpers :only [link-to]]
        [hiccup.form-helpers]))

(defn post-form [date]
  [:div.content
   [:h1 (utils/jp-date date)]
   [:div#edit.input-wrapper
    (form-to [:post "/w"]
             (text-area "text")
             [:div.right (submit-button "記録")])]])

;; view form
(defpage "/w" []
  (let [today (utils/todays-url)]
    (common/layout
          (post-form today))))

;; update text or redirect if empty
(defpage [:post "/w"] {:keys [text]}
  (if (= text "") (redirect "/")
      (let [id (utils/todays-url) content text]
        (post/create-or-update id content)
        (redirect (str "/v/" id)))))