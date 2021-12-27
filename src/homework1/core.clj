(ns homework1.core
  (:require [aleph.tcp :as tcp]
            [gloss.core :as g]
            [gloss.io :as gio]
            [manifold.stream :as s]
            [homework1.socket :as socket]
            [homework1.ui :as ui]))




(defn -main [args]
  (cond (= "-server" args)
        (do (println "I am server")
            (socket/server))

        (= "-black" args)
        (do (println "I am using black....")
            (ui/init-client 2))

        (= "-red" args)
        (ui/init-client 1)))

(comment
  ;; [[xpos ypos] color]  1:black 2:white



  (.close server)

  (def client1
    (wrap-duplex-stream
     msg-codec
     @(tcp/client {:host "localhost"
                   :port 3333})))


  (s/consume println client1)

  (s/put! client1 [[3 4] 1])

  


  )
