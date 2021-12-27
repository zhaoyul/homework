(defproject homework1 "homework"
  :description "实现一个TCP通讯的小游戏"
  :jvm-opts ["-Dapple.awt.UIElement=false" "-Djdk.attach.allowAttachSelf"
             "--illegal-access=permit"
             #_"-XX:+TraceClassLoading"]
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [aleph "0.4.6"]
                 [gloss "0.2.6"]
                 [cljfx "1.7.17"]]
  :repl-options {:init-ns homework1.core})
