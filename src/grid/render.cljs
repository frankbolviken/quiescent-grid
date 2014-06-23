(ns grid.render
  (:require [quiescent :as q :include-macros true]
            [quiescent.dom :as d]))

(q/defcomponent User
  [data header-meta]
  (apply d/tr {}
         (map (fn [key] (d/td {} (get data key))) (map :key header-meta))))

(defn render-header [header]
  (d/thead {}
           (apply d/tr {}
                  (map (fn [h] (d/th {:className (:className h) 
                                      :onClick (:onClick h)} (:title h)))
                       header))))

(q/defcomponent Grid
  [model]
  (let [header-meta (get-in model [:meta :header])]
    (d/div {:className "panel panel-default"}
           (d/div {:className "panel-heading"}
                  "SuperAmazing ClojureScript w/React grid")
           (d/table {:className "table"}
                    (render-header header-meta)
                    (apply d/tbody {}
                           (map (fn [d]
                                  (User d header-meta)) (:data model)))))))

(defn render-grid [grid-data]
  (q/render (Grid grid-data)
            (.getElementById js/document "grid")))

(def sort-order (atom reverse))

(defn reverse-sort [f]
  (if (= identity f)
     reverse
     identity))
  
(defn column-sorter [column]
  (swap! sort-order reverse-sort)
  (update-in my-data [:data] (fn [data] (@sort-order  (sort-by column data)))))

(defn create-sorter [f column-key]
  (fn [event reactid]
    (render-grid (f column-key))))

(def my-data {:data [{:name "John Doe"
                      :email "user@email.com"
                      :country "USA" }
                     {:name "Frank Bølviken"
                      :email "frank.bolviken@gmail.com"
                      :country "Norway"}
                     {:name "SS Assum"
                      :email "sss.bolviken@gmail.com"
                      :country "France"}]
              :meta {:header
                     [{:key :email :title "Email" :className "email" :onClick
                       (create-sorter column-sorter :email)}
                      {:key :name :title "Name"
                       :onClick
                       (create-sorter column-sorter :name)}
                      {:key :country :title "Country"
                       :onClick (create-sorter column-sorter :country)
                       }]}})

(render-grid  my-data)
