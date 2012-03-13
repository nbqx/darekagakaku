(ns dareka.models.post
  (:require [dareka.utils :as utils]
            [somnium.congomongo :as mongo])
  (:use [somnium.congomongo.config :only [*mongo-config*]]))

(def dev-db "mydb")
(def dev-host "127.0.0.1")
(def dev-port 27017)

;; from http://thecomputersarewinning.com/post/clojure-heroku-noir-mongo
(defn split-mongo-url [url]
  "Parses mongodb url from heroku, eg. mongodb://user:pass@localhost:1234/db"
  (let [matcher (re-matcher #"^.*://(.*?):(.*?)@(.*?):(\d+)/(.*)$" url)] ;; Setup the regex.
    (when (.find matcher) ;; Check if it matches.
      (zipmap [:match :user :pass :host :port :db] (re-groups matcher))))) ;; Construct an options map.

(defn conn []
  (when (not (mongo/connection? *mongo-config*))
    (let [mongo-url (get (System/getenv) "MONGOHQ_URL")]
      (if (nil? mongo-url)
        (mongo/mongo! :db dev-db :host dev-host :port dev-port)
        (let [config (split-mongo-url mongo-url)]
          (mongo/mongo! :db (:db config) :host (:host config) :port (Integer. (:port config)))
          (mongo/authenticate (:user config) (:pass config)))))))

;;sanitize text
(defn my-sanitize [st]
  (-> st str (.replace "&" "&amp;") (.replace "<" "&lt;") (.replace ">" "&gt;")))

;;get
(defn get-or-new [id]
  (conn)
  (let [n (mongo/fetch-count :posts :where {:date id})
        today (utils/todays-url)]
    (if (and (= n 0) (= id today))
      (mongo/insert! :posts {:date id :content "" :updated_at (.toString (utils/jp-now))})
      (mongo/fetch-one :posts :where {:date id}))))
  
;;insert
(defn create-or-update [id cont]
  (conn)
  (let [n (mongo/fetch-count :posts :where {:date id})
        txt (my-sanitize cont)]
    (if (= n 0)
      (mongo/insert! :posts {:date id :content txt :updated_at (.toString (utils/jp-now))})
      (let [itm (mongo/fetch-one :posts :where {:date id})]
        (mongo/update! :posts itm (merge itm {:content txt :updated_at (.toString (utils/jp-now))}))))))

;;get recent
(defn get-recent [n]
  (conn)
  (mongo/fetch :posts :limit n :sort {:date -1}))