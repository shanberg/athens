(ns athens.devcards.style-guide
  (:require
   [athens.db]
   [athens.style :refer [color THEME-LIGHT THEME-DARK OPACITIES]]
   [cljs-styled-components.reagent :refer [defstyled]]
   [cljsjs.react]
   [cljsjs.react.dom]
   [devcards.core :refer-macros [defcard-rg]]))


;;; Styles


(defstyled color-group :div
  {:display "grid"
   :padding "1rem"
   :grid-template-columns "repeat( auto-fit, minmax(9rem, 1fr))"
   :grid-gap "3rem 1rem"
   :text-align "center"
   :align-items "center"})


(defstyled color-item :span
  {:display "grid"
   :grid-gap "0.25rem"
   ::stylefy/manual [[:div {:border-radius "1000px"
                            :background (color :link-color)
                            :height "4rem"
                            :margin "auto"
                            :width "4rem"}]]})


(defstyled text-item :span
  {:display "flex"
   :justify-content "space-between"})


;;; Components


(def types [:h1 :h2 :h3 :h4 :h5 :span :span.block-ref])


(def fonts
  [["IBM Plex Serif" "serif"]
   ["IBM Plex Sans" "sans-serif"]
   ["IBM Plex Mono" "monospace"]])


;;; Devcards


(defcard-rg Light-Theme
  [color-group
   {:style {:background (color :body-text-color :opacity-low)}}
   (doall
    (for [c (keys THEME-LIGHT)]
      ^{:key c}
      [color-item
       [:div {:style {:background (color c) :box-shadow "0 0 0 1px rgba(0,0,0,0.15)"}}]
       [:span c]
       [:span {:style {:color (color c)}} (color c)]]))]
  {}
  {:padding false})


(defcard-rg Dark-Theme
  [color-group
   {:style {:background (color :body-text-color :opacity-low)}}
   (doall
    (for [c (keys THEME-DARK)]
      ^{:key c}
      [color-item
       [:div {:style {:background (color c) :box-shadow "0 0 0 1px rgba(0,0,0,0.15)"}}]
       [:span c]
       [:span {:style {:color (color c)}} (color c)]]))]
  {}
  {:padding false})


(defcard-rg Opacities
  [color-group
   (doall
    (for [o (keys OPACITIES)]
      ^{:key o}
      [color-item
       [:div {:style {:opacity (o OPACITIES)}}]
       [:span o]]))])


(defcard-rg Sans-Types
  [:div
   (doall
     (for [t types]
       ^{:key t}
       [text-item
        [:span t]
        [t {:style {:font-family (second fonts)}} "Welcome to Athens"]]))])


(defcard-rg Serif-Types
  [:div
   (doall
     (for [t types]
       ^{:key t}
       [text-item
        [:span t]
        [t {:style {:font-family (first fonts)}} "Welcome to Athens"]]))])


(defcard-rg Monospace-Types
  [:div
   (doall
     (for [t types]
       ^{:key t}
       [text-item
        [:span t]
        [t {:style {:font-family (last fonts)}} "Welcome to Athens"]]))])
