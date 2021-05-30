(ns athens.views.blocks.user-presence-indicator
  (:require
    [athens.style :as style :refer [OPACITIES]]
    [stylefy.core :as stylefy]))


(stylefy/keyframes "drop-area-appear"
                   [:from
                    {:opacity "0"}]
                   [:to
                    {:opacity "1"}])


(stylefy/keyframes "drop-area-color-pulse"
                   [:from
                    {:opacity (:opacity-lower style/OPACITIES)}]
                   [:to
                    {:opacity (:opacity-med style/OPACITIES)}])


(def user-presence-indicator-style
{:border-radius "0.25rem"
 :position "absolute"
 :right "100%"
 :background "var(--link-color)"
 :color "#fff"
 :padding "2px"
 ::stylefy/manual [["&:after" {:position "absolute"}]]})


(defn user-presence-indicator
  ([{:keys [username]}]
   [:span (stylefy/use-style
          (merge user-presence-indicator-style))
    username]))
