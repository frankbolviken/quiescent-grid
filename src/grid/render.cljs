(ns grid.render
  (:require [quiescent :as q :include-macros true]
            [quiescent.dom :as d]))


(q/defcomponent User
  [user]
  (d/tr {}
        (d/td {} (:name user))
         (d/td {} (:email user))
         (d/td {} (:country user))))

(q/defcomponent Grid
  [data]
  (d/div {:className "panel panel-default"}
         (d/div {:className "panel-heading"} (str "SuperAmazing ClojureScript w/React grid"))
         (d/table {:className "table"}
                  (d/thead {}
                           (d/tr {}
                                 (d/th {} (str "Name"))
                                 (d/th {} (str "Email"))
                                 (d/th {} (str "Country"))))
                  (apply d/tbody {}
                         (map User (:users data))))))


(def my-data {:users [{:name "John Doe"
                          :email "user@email.com"
                          :country "USA" }
                         {:name "Frank Bølviken"
                          :email "frank.bolviken@gmail.com"
                          :country "Norway"}]})


(q/render (Grid my-data)
          (.getElementById js/document "grid"))
