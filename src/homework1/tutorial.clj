(ns homework1.tutorial
  (:require [cljfx.api :as fx]))

(Thread/setDefaultUncaughtExceptionHandler
 (reify Thread$UncaughtExceptionHandler
   (uncaughtException [_ thread ex]
     (println {:what :uncaught-exception
               :exception ex
               :where (str "Uncaught exception on" (.getName thread))}))))


;; I want to build an interactive chart that shows how bouncing object falls
;; on the ground. I want to be able to edit gravity and friction to see how
;; it affects object's behavior, so I will put it into state:

(def *state
  (atom {:gravity 10
         :friction 0.4}))

(defmulti event-handler :event/type)



(def length 500)

(def line-count 20)

(def s (atom (into {}
                   (for [x (range line-count)
                         y (range line-count)]
                     [[x y] 0]))))

(defn move [[x y] color]
  (swap! s assoc [x y] color))

(defn gen-lines [length line-count margin]
  (let [space (- length (* 2 margin))
        interval (/ space (dec line-count))
        lst    (mapv (fn [idx] (+ margin (* idx interval)))
                     (range line-count))]
    (concat
     (mapv (fn [pos]
             {:fx/type :line
              :start-x margin
              :start-y (int pos)
              :end-x (+ margin space)
              :end-y (int pos)})
           lst)
     (mapv (fn [pos]
             {:fx/type :line
              :start-x (int pos)
              :start-y margin
              :end-x (int pos)
              :end-y (+ margin space)})
           lst))))


(defmethod event-handler ::touched [e]
  (prn e))

(defmethod event-handler :default [e]
  (prn "unhandled event:" e))


(defn root-view [{{:keys [gravity friction]} :state}]
  {:fx/type :stage
   :height (+ 30 length)
   :width length
   :min-height (+ 30 length)
   :min-width length
   :showing true
   :scene {:fx/type :scene
           :root {:fx/type :pane
                  :children (concat
                             (gen-lines length line-count 10)
                             [{:fx/type :circle
                               :radius 30
                               :layout-x 100
                               :layout-y 100
                               :fill "red"
                               :on-mouse-clicked (fn [e] (prn "clicked"))}])
                  }}})

(def renderer
  (fx/create-renderer
   :middleware (fx/wrap-map-desc (fn [state]
                                   {:fx/type root-view
                                    :state state}))
   ))

(fx/mount-renderer *state renderer)
