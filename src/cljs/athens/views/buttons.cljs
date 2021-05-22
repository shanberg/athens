(ns athens.views.buttons
  (:require
   [athens.db]
   [athens.style :refer [color]]
   [cljs-styled-components.reagent :refer [defstyled]]
   [cljsjs.react]
   [cljsjs.react.dom]
   [garden.selectors :as selectors]
   [stylefy.core :as stylefy]))


;;; Styles


(def button-icons-style
  {:margin-block-start "-0.0835em"
   :margin-block-end "-0.0835em"})


(def button-icons-not-last-child-style {:margin-inline-end "0.251em"})

(def button-icons-not-first-child-style {:margin-inline-style "0.251em"})


(def button-icons-only-child-style
  {:margin-inline-start "-0.25rem"
   :margin-inline-end "-0.25rem"})


(def buttons-style
  {:cursor           "pointer"
   :padding          "0.375rem 0.625rem"
   :margin           "0"
   :font-family      "inherit"
   :font-size        "inherit"
   :border-radius    "0.25rem"
   :font-weight      "500"
   :border           "none"
   :display          "inline-flex"
   :align-items      "center"
   :color            (color :body-text-color)
   :background-color "transparent"
   :transition       "all 0.075s ease"
   ::stylefy/manual [[:&:hover {:background (color :body-text-color :opacity-lower)}]
                     [:&:active
                      :&:hover:active
                      :&.is-active {:color (color :body-text-color)
                                    :background (color :body-text-color :opacity-lower)}]
                     [:&:active
                      :&:hover:active
                      :&:active.is-active {:background (color :body-text-color :opacity-low)}]
                     [:&:disabled :&:disabled:active {:color (color :body-text-color :opacity-low)
                                                      :background (color :body-text-color :opacity-lower)
                                                      :cursor "default"}]
                     [:span {:flex "1 0 auto"
                             :text-align "left"}]
                     [:kbd {:margin-inline-start "1rem"
                            :font-size "85%"}]
                     [:svg button-icons-style
                      [(selectors/& (selectors/not (selectors/last-child))) button-icons-not-last-child-style]
                      [(selectors/& (selectors/not (selectors/first-child))) button-icons-not-first-child-style]
                      [(selectors/& ((selectors/first-child (selectors/last-child)))) button-icons-only-child-style]]
                     [:&.is-primary {:color (color :link-color)
                                     :background (color :link-color :opacity-lower)}
                      [:&:hover {:background (color :link-color :opacity-low)}]
                      [:&:active
                       :&:hover:active
                       :&.is-active {:color "white"
                                     :background (color :link-color)}]
                      [:&:disabled :&:disabled:active {:color (color :body-text-color :opacity-low)
                                                       :background (color :body-text-color :opacity-lower)
                                                       :cursor "default"}]]]})

(defstyled button
  :button {:cursor           "pointer"
           :padding          "0.375rem 0.625rem"
           :margin           "0"
           :font-family      "inherit"
           :font-size        "inherit"
           :border-radius    "0.25rem"
           :font-weight      "500"
           :border           "none"
           :display          "inline-flex"
           :align-items      "center"
           :color            (color :body-text-color)
           :background-color "transparent"
           :transition       "all 0.075s ease"
           "&:hover, &[aria-pressed='true']" {:color (color :body-text-color)
                                              :background (color :body-text-color :opacity-lower)}
           "&:active, &:active:hover" {:background (color :body-text-color :opacity-low)}
           "&:disabled, &:disabled:active" {:color (color :body-text-color :opacity-low)
                                            :background (color :body-text-color :opacity-lower)
                                            :cursor "default"}
           "> svg" {:margin-block-start "-0.0835em"
                    :margin-block-end "-0.0835em"
                    "&:not(:last-child)" {:margin-inline-end "0.251em"}
                    "&:not(:first-child)" {:margin-inline-start "0.251em"}
                    "&:first-child:last-child" {:margin-inline-start "-0.25rem"
                                                :margin-inline-end "-0.25rem"}}
           "span" {:flex "1 0 auto"
                   :text-align "left"}

           "kbd" {:margin-inline-start "1rem"
                  :font-size "85%"}
           "&.is-primary" {:color (color :link-color)
                           :background (color :link-color :opacity-lower)
                           "&:hover" {:background (color :link-color :opacity-low)}
                           "&:active, :&:hover:active, &[aria-pressed]" {:color "white"
                                                                         :background (color :link-color)}
                           "&:disabled, &:disabled:active" {:color (color :body-text-color :opacity-low)
                                                            :background (color :body-text-color :opacity-lower)
                                                            :cursor "default"}}})


;;; Components

(stylefy/class "button" buttons-style)
