(ns clj-2048.core
  (:require [reagent.core :as reagent :refer [atom]]))

(defonce board-state (atom [[0 2 0 2] [2 0 2 0] [2 2 2 2] [2 0 0 2]]))

(defn move-left
  [board]
  (map
    #(as-> %1 r
           (remove zero? r)
           (partition-by identity r)
           (mapcat (partial partition-all 2 2) r)
           (map (partial apply +) r)
           (concat r (repeat (- 4 (count r)) 0))
           )
    board))

(defn board
  []
  [:div
   {:class ["board"] :autofocus 1 :tabindex 1
    :on-key-down (fn [] (swap! board-state move-left))}
   (map
     (fn [row]
       [:div
        {:class ["row"]}
        (map (fn [d] [:div
                      {:class ["num"]} (if (zero? d) "" d)]) row)])
     @board-state)])

(reagent/render-component [board]
                          (. js/document (getElementById "app")))
