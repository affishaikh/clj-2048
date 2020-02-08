(ns clj-2048.core
  (:require [reagent.core :as reagent :refer [atom]]))

(defonce board-state (atom [[0 2 0 2] [2 0 2 0] [2 2 2 2] [2 0 0 2]]))

(defn move-left
  [board]
  (mapv
    #(as-> %1 r
           (remove zero? r)
           (partition-by identity r)
           (mapcat (partial partition-all 2 2) r)
           (map (partial apply +) r)
           (concat r (repeat (- 4 (count r)) 0))
           (vec r)
           )
    board))

(defn transpose
  [board]
  (apply mapv vector board))

(defn move-up
  [board]
  (-> board
      (transpose)
      (move-left)
      (transpose)))

(defn move-right
  [board]
  (->> board
       (mapv (comp vec reverse))
       (move-left)
       (mapv (comp vec reverse))
       ))

(defn move-down
  [board]
  (-> board
      (transpose)
      (move-right)
      (transpose)))

(defn insert-2or4
  [board]
  (assoc-in
    board
    (rand-nth
      (for [x (range (count board))
            y (range (count board))
            :when (= (get-in board [x y]) 0)]
        [x y])) (rand-nth [2 4])))

(defn comp-insert-2
  [f]
  (comp insert-2or4 f))

(defn board
  []
  [:div
   {:class       ["board"] :autofocus 1 :tabindex 1
    :on-key-down (fn [kc] (case
                            (.-which kc)
                            37 (swap! board-state (comp-insert-2 move-left))
                            38 (swap! board-state (comp-insert-2 move-up))
                            39 (swap! board-state (comp-insert-2 move-right))
                            40 (swap! board-state (comp-insert-2 move-down))))}
   (map
     (fn [row]
       [:div
        {:class ["row"]}
        (map (fn [d] [:div
                      {:class ["num"]} (if (zero? d) "" d)]) row)])
     @board-state)])

(reagent/render-component [board]
                          (. js/document (getElementById "app")))