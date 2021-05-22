(ns athens.views.app-toolbar
  (:require
    ["@material-ui/icons/BubbleChart" :default BubbleChart]
    ["@material-ui/icons/CheckCircle" :default CheckCircle]
    ["@material-ui/icons/ChevronLeft" :default ChevronLeft]
    ["@material-ui/icons/ChevronRight" :default ChevronRight]
    ["@material-ui/icons/Error" :default Error]
    ["@material-ui/icons/FileCopy" :default FileCopy]
    ["@material-ui/icons/Menu" :default Menu]
    ["@material-ui/icons/MergeType" :default MergeType]
    ["@material-ui/icons/Replay" :default Replay]
    ["@material-ui/icons/Search" :default Search]
    ["@material-ui/icons/Settings" :default Settings]
    ["@material-ui/icons/Storage" :default Storage]
    ["@material-ui/icons/Sync" :default Sync]
    ["@material-ui/icons/Today" :default Today]
    ["@material-ui/icons/ToggleOff" :default ToggleOff]
    ["@material-ui/icons/ToggleOn" :default ToggleOn]
    ["@material-ui/icons/VerticalSplit" :default VerticalSplit]
    [athens.router :as router]
    [athens.style :refer [color]]
    [athens.subs]
    [athens.util :as util]
    [athens.views.buttons :refer [button]]
    [athens.views.filesystem :as filesystem]
    [athens.views.presence :as presence]
    [athens.ws-client :as ws]
    [cljs-styled-components.reagent :refer [defstyled]]
    [re-frame.core :refer [subscribe dispatch]]
    [reagent.core :as r]))


;; Styles


(defstyled app-header :header
  {:grid-area "app-header"
   :justify-content "flex-start"
   :background-clip "padding-box"
   :align-items "center"
   :display "grid"
   :position "absolute"
   :top "0"
   :border-bottom [["1px solid" (color :border-color)]]
   :backdrop-filter "blur(0.375rem)"
   :background (color :background-color :opacity-high)
   :right 0
   :left 0
   :grid-template-columns "auto 1fr auto"
   :z-index "1070"
   :grid-auto-flow "column"
   :padding "0 0.75rem"
   "svg" {:font-size "20px"}
   "button" {:justify-self "flex-start"}})


(defstyled app-header-control-section :div
  {:display "grid"
   :grid-auto-flow "column"
   :padding "0.25rem"
   :grid-gap "0.25rem"})


(defstyled app-header-secondary-controls app-header-control-section
  {:color (color :body-text-color :opacity-med)
   :justify-self "flex-end"
   :margin-left "auto"
   "button" {:color "inherit"}})


(defstyled separator :hr
  {:border "0"
   :background (color :background-minus-2 :opacity-high)
   :margin-inline "20%"
   :margin-block "0"
   :inline-size "1px"
   :block-size "auto"})


(defstyled sync-icon-style :div
  {:background (color :background-minus-2)
   :border-radius "100%"
   :padding 0
   :margin 0
   :margin-left "-10px"
   :align-self "flex-end"
   :height "12px !important"
   :width "12px !important"})


;; Components


(defn app-toolbar
  []
  (let [left-open?        (subscribe [:left-sidebar/open])
        right-open?       (subscribe [:right-sidebar/open])
        route-name        (subscribe [:current-route/name])
        electron?         (util/electron?)
        theme-dark        (subscribe [:theme/dark])
        remote-graph-conf (subscribe [:db/remote-graph-conf])
        socket-status     (subscribe [:socket-status])
        merge-open?       (reagent.core/atom false)]
    (fn []
      [:<>
       (when @merge-open?
         [filesystem/merge-modal merge-open?])
       [app-header
        [app-header-control-section
         [button {:aria-pressed @left-open?
                  :title "Toggle Navigation Sidebar"
                  :onClick #(dispatch [:left-sidebar/toggle])}
          [:> Menu]]
         [separator]
         ;; TODO: refactor to effects
         (when electron?
           [:<>
            [button {:onClick #(.back js/window.history)} [:> ChevronLeft]]
            [button {:onClick #(.forward js/window.history)} [:> ChevronRight]]
            [separator]])
         [button {:onClick router/nav-daily-notes
                  :title "Open Today's Daily Note"
                  :aria-pressed (= @route-name :home)} [:> Today]]
         [button {:onClick #(router/navigate :pages)
                  :title "Open All Pages"
                  :aria-pressed (= @route-name :pages)} [:> FileCopy]]
         [button {:onClick #(router/navigate :graph)
                  :title "Open Graph"
                  :aria-pressed (= @route-name :graph)} [:> BubbleChart]]
         ;; below is used for testing error tracking
         #_[button {:onClick #(throw (js/Error "error"))
                    :style {:border "1px solid red"}} [:> Warning]]
         [button {:onClick #(dispatch [:athena/toggle])
                  :style    {:width "14rem" :marginLeft "1rem" :background (color :background-minus-1)}
                  :aria-pressed @(subscribe [:athena/open])}
          [:<> [:> Search] [:span "Find or Create a Page"]]]]

        [app-header-secondary-controls
         (if electron?
           [:<>
            [presence/presence-popover-info]
            (when (= @socket-status :closed)
              [button
               {:onClick #(ws/start-socket!
                            (assoc @remote-graph-conf
                                   :reload-on-init? true))}
               [:<>
                [:> Replay]
                [:span "Re-connect with remote"]]])
            [button {:onClick #(swap! merge-open? not)
                     :title "Merge Roam Database"}
             [:> MergeType]]
            [button {:onClick #(router/navigate :settings)
                     :title "Open Settings"
                     :aria-pressed (= @route-name :settings)}
             [:> Settings]]

            [button {:onClick #(dispatch [:modal/toggle])
                     :title "Choose Database"}
             [:div {:style {:display "flex"}}
              [:> Storage {:style {:align-self "center"}}]
              [sync-icon-style
               (cond
                 (= @socket-status :closed)
                 [:> Error
                  {:style {:color (color :error-color)}
                   :title "Disconnected"}]
                 (or (and (:default? @remote-graph-conf)
                          (= @socket-status :running))
                     @(subscribe [:db/synced]))
                 [:> CheckCircle
                  {:style {:color (color :confirmation-color)}
                   :title "Synced"}]
                 :else [:> Sync
                        {:style {:color (color :highlight-color)}
                         :title "Synchronizing..."}])]]]

            [separator]]
           [button {:style {:min-width "max-content"}
                    :onClick #(dispatch [:get-db/init])
                    :className "is-primary"} "Load Test DB"])
         [button {:onClick #(dispatch [:theme/toggle])
                  :title "Toggle Color Scheme"}
          (if @theme-dark
            [:> ToggleOff]
            [:> ToggleOn])]
         [separator]
         [button {:aria-pressed @right-open?
                  :title "Toggle Sidebar"
                  :onClick #(dispatch [:right-sidebar/toggle])}
          [:> VerticalSplit {:style {:transform "scaleX(-1)"}}]]]]])))

