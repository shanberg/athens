(ns athens.views.wait-message
  (:require
   ["@material-ui/core/Button" :default Button]
   ["@material-ui/core/Dialog" :default Dialog]
   ["@material-ui/core/DialogContent" :default DialogContent]
   ["@material-ui/core/CircularProgress" :default CircularProgress]
   ["@material-ui/core/Typography" :default Typography]
   [athens.views.spinner :refer [spinner-component]]
   [athens.style :as style]
   [reagent.core :as r]
   [stylefy.core :as stylefy]))


(defn wait-message []
(let [open (r/atom true)
      close #(swap! open not)]
    (fn []
      [:> Dialog {:open @open
                  :onClose close}
       [:> DialogContent {:classes {:root "outline"}
                          :style {:padding "1rem 2.5rem" :display "flex" :alignItems "center" :gap "1rem" :flexDirection "column"}}
        [:> CircularProgress]
        [:> Typography {:variant "caption" :color "textSecondary"} "Saving..."]]])))
