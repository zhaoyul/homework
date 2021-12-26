(ns homework1.core
  (:require [aleph.tcp :as tcp]
            [manifold.stream :as s]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(comment

  (def server (tcp/start-server (fn [server-stream info]
                                  (s/consume
                                   #(do
                                      (println "收到数据:" %)
                                      (s/put! server-stream "收到消息了"))
                                   server-stream))
                                {:port 3333}))
  (.close server)

  (def client (tcp/client {:host "localhost"
                           :port 3333}))


  (type @client)

  (s/put! @client "hello world")

  )
