# openweatherdb

A sample client of the Open Weather Map API in Clojure.

Fetches weather data about a handful of cities every few minutes, saves it in MongoDB.

## Usage

Create an account on http://openweathermap.org/login and obtain your APPID.

Before running the app, set the OPENWEATHERMAPID environment variable, like this:

```
$ export OPENWEATHERMAPID="your-hexadecimal-key-goes-here"
$ lein run
```

## License

Copyright © 2014 Denis Fuenzalida

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
