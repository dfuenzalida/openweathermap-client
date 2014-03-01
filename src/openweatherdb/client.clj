(ns openweatherdb.client
  (:require [clj-http.client :as client]))

(def appid (System/getenv "OPENWEATHERMAPID"))
(def api-endpoint "http://api.openweathermap.org/data/2.5/weather")
(def default-params {:mode :json :APPID appid :units :metric})
(def default-options {:as :json :content-type :json :coerce :always :throw-exceptions false})

(defn weather [city country]

  (println (str "Retrieving data for " city ", " country "..."))

  (let [query-params (into default-params {:q (str city "," country)})]
    (:body (client/get api-endpoint
                       (into default-options {:query-params query-params})))))

(defn kelvin->celsius [k]
  (- k 273.15))
