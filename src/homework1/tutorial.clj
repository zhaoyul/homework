(ns homework1.tutorial
  (:require [cljfx.api :as fx]))


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


(defn root-view [{{:keys [gravity friction]} :state}]
  {:fx/type :stage
   :height (+ 30 length)
   :width length
   :min-height (+ 30 length)
   :min-width length
   :showing true
   :scene {:fx/type :scene
           :root {:fx/type :pane
                  :children (gen-lines length line-count 10)}}})

(def renderer
  (fx/create-renderer
   :middleware (fx/wrap-map-desc (fn [state]
                                   {:fx/type root-view
                                    :state state}))
   :opts {:fx.opt/map-event-handler event-handler}))

(fx/mount-renderer *state renderer)
