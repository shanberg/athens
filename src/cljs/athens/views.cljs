(ns athens.views
  (:require
    ["@material-ui/core/Snackbar" :as Snackbar]
    [athens.config]
    [athens.style :refer [color DEPTH-SHADOWS]]
    [athens.subs]
    [athens.views.app-toolbar :as app-toolbar]
    [athens.views.athena :refer [athena-component]]
    [athens.views.devtool :refer [devtool-component]]
    [athens.views.filesystem :as filesystem]
    [athens.views.left-sidebar :as left-sidebar]
    [athens.views.pages.core :as pages]
    [athens.views.right-sidebar :as right-sidebar]
    [athens.views.spinner :refer [initial-spinner-component]]
    [re-frame.core :as rf]
    [reagent.core :as r]
    [stylefy.core :as stylefy]))


;;; Styles


(def app-wrapper-style
  {:display "grid"
   :grid-template-areas
   "'app-header app-header app-header'
    'left-sidebar main-content secondary-content'
   'devtool devtool devtool'"
   :grid-template-columns "auto 1fr auto"
   :grid-template-rows "auto 1fr auto"
   :height "100vh"})


(def alert-message-style
  {:color (color :background-plus-2)
   :background (color :body-text-color)
   :padding "0.25rem 1rem"
   :border-radius "1.25rem"
   :box-shadow (:16 DEPTH-SHADOWS)
   ::stylefy/manual [[:&.success {:background (color :confirmation-bg-color)
                                  :color "#fff"}]
                     [:&.warning {:background (color :warning-color)
                                  :color "#fff"}]]})


;;; Components


(defn alert
  []
  (let [alert- (rf/subscribe [:alert])]
    (when-not (nil? @alert-)
      (js/alert (str @alert-))
      (rf/dispatch [:alert/unset]))))


;; Snackbar

(def m-snackbar (r/adapt-react-class (.-default Snackbar)))


(rf/reg-sub
  :db/snack-msg
  (fn [db]
    (:db/snack-msg db)))


(rf/reg-event-db
  :show-snack-msg
  (fn [db [_ msg-opts]]
    (js/setTimeout #(rf/dispatch [:show-snack-msg {}]) 4000)
    (assoc db :db/snack-msg msg-opts)))


(defn main
  []
  (let [loading    (rf/subscribe [:loading?])
        modal      (rf/subscribe [:modal])]
    (fn []
      [:<>
       [alert]
       (let [{:keys [msg type]} @(rf/subscribe [:db/snack-msg])]
         [m-snackbar
          {:message msg
           :open true}
          [:span
           (stylefy/use-style alert-message-style
                              {:class "success"})
           "You have reached enlightenment"]])
       [athena-component]
       (cond
         (and @loading @modal) [filesystem/window]

         @loading [initial-spinner-component]

         :else [:<>
                (when @modal [filesystem/window])
                [:div (stylefy/use-style app-wrapper-style)
                 [app-toolbar/app-toolbar]
                 [left-sidebar/left-sidebar]
                 [pages/view]
                 [right-sidebar/right-sidebar]
                 [devtool-component]]])])))
