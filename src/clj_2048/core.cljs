(ns clj-2048.core
    (:require [reagent.core :as reagent :refer [atom]]))

(defonce board-state (atom [[0 2 0 2] [2 0 2 0] [2 2 2 2] [2 0 0 2]]))

(defn move-left
  [board]
  (loop
   [b (remove #{0} board) res []]
    (cond
      (empty? b)
       (concat res (repeat (- 4 (count res)) 0))
      (= (first b) (second b))
       (recur (nnext b) (conj res (+ (first b) (second b))))
      :else
        (recur (next b) (conj res (first b))))))

(defn board
  []
   [:table
    {:class ["board"] :autofocus 1 :tabindex 1
     :on-key-down (fn [] (swap! board-state #(map move-left @board-state)))}
    [:tbody
     (map
       (fn [r]
         [:tr
          (map (fn [d]
                 [:td d]) r)])
       @board-state)]])

(reagent/render-component [board]
                          (. js/document (getElementById "app")))
