(ns grid.render
  (:require [quiescent :as q :include-macros true]
            [quiescent.dom :as d]))

(enable-console-print!)

(q/defcomponent Row
  [data header-meta]
  (apply d/tr {}
         (map (fn [header] (d/td {} ((get header :renderer identity)
                                     ((:key header) data))))
              header-meta)))

(defn render-header [header]
  (d/thead {}
           (apply d/tr {}
                  (map (fn [h] (d/th {:className (:className h)
                                      :onClick (:onClick h)} (:title h)))
                       header))))

(defn render-rows [rows header-meta]
  (map (fn [row]
         (Row row header-meta)) rows))

(defn render-body [model]
  (let [header-meta (get-in model [:meta :header])]
    [(apply d/tbody {}
             (render-rows (:data model) header-meta))]))

(defn render-grouped-body [model f]
  (let [grouped (group-by f (:data model))
        header-meta (get-in model [:meta :header])]
    (map (fn [[k rows]]
            (apply d/tbody {}
                   (cons (d/tr {:className "group-header"}
                               (d/td {:colSpan (count header-meta)} (str k " (" (count rows) ")") ))
                         (render-rows rows header-meta)))) grouped)))

(q/defcomponent Grid [model]
  (let [header-meta (get-in model [:meta :header])
        group-by-f (get-in model [:meta :group-by])]
    (d/div {:className "panel panel-default"}
           (d/div {:className "panel-heading"}
                  "SuperAmazing ClojureScript w/React grid")
           (apply d/table {:className "table"}
                    (cons (render-header header-meta)
                          (if group-by-f
                            (render-grouped-body model group-by-f)
                            (render-body model)))))))

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

(defn link-renderer [val]
  (d/a {:className "byline" :href val} val))

(def my-data {:data [{:name "John Doe"
                      :email "user@email.com"
                      :country "USA" }
                     {:name "Frank Bølviken"
                      :email "frank.bolviken@gmail.com"
                      :country "Norway"}
                      {:name "Jazee"
                      :email "jz@gmail.com"
                      :country "Norway"}
                     {:name "SS Assum"
                      :email "sss.bolviken@gmail.com"
                      :country "France"}]
              :meta {:header
                     [{:key :email
                       :title "Email"
                       :className "email"
                       :onClick (create-sorter column-sorter :email)
                       :renderer link-renderer}
                      {:key :name
                       :title "Name"
                       :onClick (create-sorter column-sorter :name)}
                      {:key :country
                       :title "Country"
                       :onClick (create-sorter column-sorter :country)
                       }]
                     :group-by :country}})

(render-grid  my-data)
