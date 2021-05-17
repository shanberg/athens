(ns athens.views.help
  (:require
   [athens.style :refer [color ZINDICES DEPTH-SHADOWS]]
   [re-frame.core :refer [reg-sub subscribe]]
   [reagent.core :as r]
   [stylefy.core :refer [use-style]]))

;; styles

(def popup-style
  {:position "absolute"
   :background (color :background-color)
   :border-radius "1rem"
   :box-shadow (:64 DEPTH-SHADOWS)
   :z-index (:zindex-modal ZINDICES)
   :bottom "10px"
   :right "10px"})


;; subscriptions

(reg-sub
 :show-help?
 (fn [db _]
   (:show-help? db)))

;; return

(defn help
  []
  (let [show-help? (subscribe [:show-help?])
        el (.. js/document (querySelector "body"))]
    (fn []
      (if @show-help?

        (js/ReactDOM.createPortal
         (r/as-element [:div (use-style popup-style)
                        [:h1 "Help!"]])
         el)
        [:<>]))))