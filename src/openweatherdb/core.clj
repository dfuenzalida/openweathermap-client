(ns openweatherdb.core
  (:require [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.triggers :as t]
            [clojurewerkz.quartzite.jobs :as j]
            [clojurewerkz.quartzite.jobs :refer [defjob]]
            [clojurewerkz.quartzite.schedule.simple
             :refer [schedule with-repeat-count with-interval-in-milliseconds]]))

(defjob NoOpJob
  [ctx]
  (do
    (println ctx)
    (println (java.util.Date.))))

(defn -main
  [& m]
  (qs/initialize)
  (qs/start)
  (let [job (j/build
             (j/of-type NoOpJob)
             (j/with-identity (j/key "jobs.noop.1")))
        trigger (t/build
                 (t/with-identity (t/key "triggers.1"))
                 (t/start-now)
                 (t/with-schedule (schedule
                                   (with-repeat-count 10)
                                   (with-interval-in-milliseconds 2000))))]
    (qs/schedule job trigger)))
