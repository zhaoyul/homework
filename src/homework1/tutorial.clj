(ns homework1.tutorial
  (:require [cljfx.api :as fx]))

(defmulti event-handler :event/type)



(def length 500)

(def line-count 20)

(def *state (atom (into {}
                        (for [x (range line-count)
                              y (range line-count)]
                          [[x y] 0]))))

(defn move [[x y] color]
  (swap! *state assoc [x y] color))

(defn gen-lines-circles [length line-count margin]
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
           lst)
     (mapv (fn [[x y] [idx-x idx-y]]
             {:fx/type :circle
              :radius 10
              :layout-x x
              :layout-y y
              :opacity (if (zero? (get @*state [idx-x idx-y])) 0.0 1.0)
              :fill (nth ["white" "red" "black"] (get @*state [idx-x idx-y]))
              :on-mouse-clicked (fn [e]
                                  (prn "x:" x "y:" y " get clicked")
                                  (when (zero? (get @*state [idx-x idx-y]))
                                    (swap! *state assoc [idx-x idx-y] 0)))})
           (for [x lst
                 y lst]
             [x y])

           (for [x-idx (range line-count)
                 y-idx (range line-count)]
             [x-idx y-idx])))))


(defmethod event-handler ::touched [e]
  (prn e))

(defmethod event-handler :default [e]
  (prn "unhandled event:" e))


(defn root-view [state]
  {:fx/type :stage
   :height (+ 30 length)
   :width length
   :min-height (+ 30 length)
   :min-width length
   :showing true
   :scene {:fx/type :scene
           :root {:fx/type :pane
                  :children (gen-lines-circles length line-count 10)
                  }}})

(def renderer
  (fx/create-renderer
   :middleware (fx/wrap-map-desc (fn [state]
                                   {:fx/type root-view
                                    :state state}))
   ))

(fx/mount-renderer *state renderer)
