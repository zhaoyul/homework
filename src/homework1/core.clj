(ns homework1.core
  (:require [aleph.tcp :as tcp]
            [manifold.stream :as s]
            [homework1.socket :as socket]
            [homework1.ui :as ui])
  (:gen-class))


(defn -main [& args]
  (cond (= "-server" (first args))
        (do (println "I am server")
            (socket/server))

        (= "-black" (first args))
        (do (println "I am using black....")
            (ui/init-client 2))

        (= "-red" (first args))
        (ui/init-client 1)

        :else (println "get args:" args)))
