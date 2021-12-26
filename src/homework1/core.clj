(ns homework1.core
  (:require [aleph.tcp :as tcp]
            [gloss.core :as g]
            [gloss.io :as gio]
            [manifold.stream :as s]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))


(defn wrap-duplex-stream
  "s is the duplex stream: both client & server"
  [protocol s]
  (let [out (s/stream)] ;; device-sensor.socket.??
    (s/connect
     (s/map #(gio/encode protocol %) out) ;; source
     s)                                   ;; sink
    (s/splice                             ;; put! --> sink --- source -> take!
     out                                  ;; sink
     (gio/decode-stream s protocol))))



(comment
  ;; [[xpos ypos] color]  1:black 2:white
  (g/defcodec msg-codec [[:byte :byte] :byte])

  (defn broadcast [all-streams msg]
    (run! (fn [s] (s/try-put! s msg 1000))
          all-streams))

  (def all-server-streams (atom #{}))
  (def server (tcp/start-server
               (fn [server-stream info]
                 (swap! all-server-streams conj server-stream)
                 (s/consume
                  #(do
                     (println "收到数据:" %)
                     (broadcast @all-server-streams %))
                  server-stream))
               {:port 3333}))


  (.close server)

  (def client1
    (wrap-duplex-stream
     msg-codec
     @(tcp/client {:host "localhost"
                   :port 3333})))


  (s/consume println client1)

  (s/put! client1 [[3 4] 1])

  


  )
