(ns dareka.server
  (:use noir.core)
  (:require [noir.server :as server]
            [noir.response :as resp]
            [noir.statuses :as st :only [set-page!]]
            [dareka.utils :as utils]
            [dareka.models.post :as post]
            [dareka.views.common :as common]
            [dareka.views.show :as v]))

(server/load-views "src/dareka/views/")

(st/set-page! 404 "not found")
(st/set-page! 500 "application error")

(pre-route "/" {}
           (let [today (utils/todays-url)]
             (resp/redirect (str "/v/" today))))

(defpage "/v/:date" {:keys [date]}
  (let [data (post/get-or-new date)]
    (if (nil? data)
      {:status 404 :body "not found"}
      (common/layout (v/show data)))))

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "3000"))]
    (server/start port {:mode mode
                        :ns 'dareka})))

