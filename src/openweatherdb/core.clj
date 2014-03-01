(ns openweatherdb.core
  (:require [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.triggers :as t]
            [clojurewerkz.quartzite.jobs :as j]
            [clojurewerkz.quartzite.jobs :refer [defjob]]
            [clojurewerkz.quartzite.schedule.simple
             :refer [schedule with-repeat-count
                     repeat-forever
                     with-interval-in-milliseconds
                     with-interval-in-minutes]]
            [openweatherdb.db :as db]
            [openweatherdb.client :as client]))

;; A sample job which collects the cities and saves current weather data for each one
(defjob update-weather-data
  [ctx]
  (doall
   (map
    (fn [city]
      (let [name (:name city) country (:country city)]
        (db/save (client/weather name country))))
    (db/cities))))

(defn -main
  [& m]
  (db/connect-mongo)
  (qs/initialize)
  (qs/start)
  (let [job (j/build
             (j/of-type update-weather-data)
             (j/with-identity (j/key "jobs.weather.1")))
        trigger (t/build
                 (t/with-identity (t/key "triggers.1"))
                 (t/start-now)
                 (t/with-schedule (schedule
                                   (repeat-forever)
                                   (with-interval-in-minutes 5))))]
    (qs/schedule job trigger)))
