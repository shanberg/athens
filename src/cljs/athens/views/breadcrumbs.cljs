(ns athens.views.breadcrumbs
  (:require
    ["@material-ui/core/Breadcrumbs" :default Breadcrumbs]
    ["@material-ui/core/Link" :default Link]
    [athens.db]
    [athens.style :refer [color OPACITIES]]
    [cljsjs.react]
    [cljsjs.react.dom]
    [stylefy.core :as stylefy :refer [use-style]]))


;; Styles


(def breadcrumbs-list-style
  {::stylefy/manual [["&.root"
                      {:font-size "85%"
                       :color (color :body-text-color :opacity-high)}]
                     ["&.muted" {:color (color :body-text-color :opacity-med)}]
                     [".MuiBreadcrumbs-separator" {:opacity (:opacity-low OPACITIES)
                                                   :margin-left "0.25em"
                                                   :margin-right "0.25em"}]]})


(def breadcrumb-style
  {::stylefy/manual [["&.MuiLink-button" {:color "inherit"}]
                     ["*" {:pointer-events "none"}]
                     ["li:last-child button" {:color (color :body-text-color)}]
                     ["button:hover" {:color (color :link-color)}]]})


;; Components


(defn breadcrumbs-list
  ([children] [breadcrumbs-list {} children])
  ([{:keys [style separator className] :as props} children]
   (let [props- (dissoc props :style)]
     [:> Breadcrumbs (use-style (merge breadcrumbs-list-style style)
                                (merge {:classes {:root "root"}
                                        :separator (or separator "/")} props-))
      children])))


(defn breadcrumb
  ([children] [breadcrumb {} children])
  ([{:keys [style] :as props} children]
   (let [props- (dissoc props :style)]
     [:> Link (use-style (merge breadcrumb-style style)
                         (merge props- {:component "button"}))
      children])))
