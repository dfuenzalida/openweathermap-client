(ns openweatherdb.db
  (:require [monger.core :as mg]
            [monger.collection :as mc])
  (:import [org.bson.types ObjectId]))

(def cities-coll "cities")
(def weather-coll "weather")
(def mongo-host (or (System/getenv "MONGOHOST") "127.0.0.1"))

(def sample-cities [{:name "Santiago" :country "Chile"}
                    {:name "Sydney" :country "Australia"}
                    {:name "London" :country "UK"}])

(defn connect-mongo
  ;; with no db name connects to default db-name
  ([] (connect-mongo "openweatherdb"))

  ;; optional db-name
  ([dbname]
     (let [^MongoOptions opts (mg/mongo-options :threads-allowed-to-block-for-connection-multiplier 300
                                                :auto-connect-retry true)
           ^ServerAddress sa  (mg/server-address mongo-host 27017)]
       (mg/connect! sa opts)
       (mg/set-db! (mg/get-db dbname))

       ;; Crete cities collection if found empty
       (when (empty? (mc/find-maps cities-coll))
         (mc/insert-batch cities-coll sample-cities)))))

(defn cities []
  (mc/find-maps cities-coll))

(defn save [weather-entry]
  (mc/insert-and-return weather-coll weather-entry))
