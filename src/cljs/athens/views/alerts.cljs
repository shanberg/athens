(ns athens.views.alerts
  (:require
   ["@material-ui/icons/Announcement" :default Announcement]
   ["@material-ui/icons/Check" :default Check]
   ["@material-ui/icons/Close" :default Close]
   [athens.style :refer [color]]
   [athens.views.buttons :refer [button]]
   [cljs-styled-components.reagent :refer [defstyled]]
   [cljsjs.react]
   [cljsjs.react.dom]
   [reagent.core :as r]))


;;; Styles

(defstyled alert-container :div
  {:background-color (color :highlight-color :opacity-low)
   :display          "flex"
   :align-items      "center"
   :justify-content  "center"
   :max-width        "500px"
   :min-width        "300px"
   :padding          "10px 5px"
   :color            (color :body-text-color)
   :border-radius "5px"})


;;; Components


(defn alert-component
  "A pop-up, only used for merging pages right now. Can abstract to generic alerts and messages as needed."
  [message confirm-fn close-fn]
  [alert-container
   [button {:style {:color (color :highlight-color)}}
    [:> Announcement]]
   [:span message]
   [button {:onClick confirm-fn :style {:color (color :header-text-color)}}
    [:> Check]]
   [button {:onClick close-fn :style {:color (color :header-text-color)}}
    [:> Close]]])
