(ns homework1.socket
  (:require [aleph.tcp :as tcp]
            [gloss.core :as g]
            [gloss.io :as gio]
            [manifold.stream :as s]))

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

(g/defcodec msg-codec [[:byte :byte] :byte])

(defn broadcast [all-streams msg]
  (run! (fn [s] (s/try-put! s msg 1000))
        all-streams))

(def all-server-streams (atom #{}))

(defn close-server [server]
  (.close server))

(defn server []
  (tcp/start-server
   (fn [server-stream _info]
     (println "server get a new client...")
     (swap! all-server-streams conj server-stream)
     (s/consume
      #(do
         (println "收到数据:" %)
         (broadcast @all-server-streams %))
      server-stream))
   {:port 3333}))

(defn client []
  (wrap-duplex-stream
   msg-codec
   @(tcp/client {:host "localhost"
                 :port 3333})))

(comment
  (def s (server))

  (.close s)

  (def c1 (client))

  (s/consume (partial println "c1:") c1)

  (def c2 (client))
  (s/consume (partial println "c2:") c2)

  (s/try-put! c1 [[2 2] 2] 1000)


  )
