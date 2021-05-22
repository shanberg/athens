(ns athens.devcards.buttons
  (:require
    ["@material-ui/icons/ChevronRight" :default ChevronRight]
    ["@material-ui/icons/Face" :default Face]
    [athens.views.buttons :refer [button]]
    [devcards.core :refer-macros [defcard-rg]]
    [cljs-styled-components.reagent :refer [defstyled]]
))


(defstyled demo-container :div
  {:display "grid"
  :grid-auto-flow "column"
  :justify-content "flex-start"
  :grid-gap "0.5rem"})


(defcard-rg Default-button
  [demo-container
   [button "Button"]
   [button [:> Face]]
   [button [:<>
            [:> Face]
            [:span "Button"]]]
   [button [:<>
            [:span "Button"]
            [:> ChevronRight]]]
   [button {:disabled true} "Button"]
   [button {:disabled true} [:> Face]]
   [button {:disabled true} [:<>
                             [:> Face]
                             [:span "Button"]]]
   [button {:disabled true} [:<>
                             [:span "Button"]
                             [:> ChevronRight]]]])


(defcard-rg Primary-Button
  [demo-container
   [button {:className "is-primary"} "Button"]
   [button {:className "is-primary"} [:> Face]]
   [button {:className "is-primary"} [:<>
                            [:> Face]
                            [:span "Button"]]]
   [button {:className "is-primary"} [:<>
                            [:span "Button"]
                            [:> ChevronRight]]]
   [button {:className "is-primary" :disabled true} "Button"]
   [button {:className "is-primary" :disabled true} [:> Face]]
   [button {:className "is-primary" :disabled true} [:<>
                                           [:> Face]
                                           [:span "Button"]]]
   [button {:className "is-primary" :disabled true} [:<>
                                           [:span "Button"]
                                           [:> ChevronRight]]]])
