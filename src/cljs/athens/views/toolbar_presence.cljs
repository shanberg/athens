(ns athens.views.toolbar-presence
  (:require
   ["@material-ui/core/Popover" :as Popover]
   ["@material-ui/icons/Link" :default Link]
   [athens.style :as style]
   [athens.views.buttons :refer [button]]
   [clojure.string :as str]
   [re-frame.core :refer [subscribe]]
   [reagent.core :as r]
   [stylefy.core :as stylefy :refer [use-style]]))


(def m-popover (r/adapt-react-class (.-default Popover)))


;; Data

(def PALETTE
  ["#DDA74C"
   "#C45042"
   "#611A58"
   "#21A469"
   "#009FB8"
   "#0062BE"])


(def NAMES
  ["Zeus"
   "Poseidon"
   "Hera"
   "Demeter"
   "Athena"
   "Apollo"])
   ;;"Artemis"
   ;;"Ares"
   ;;"Aphrodite"
   ;;"Hephaestus"
   ;;"Hermes"
   ;;"Hestia"
   ;;"Dionysus"
   ;;"Hades"])


(def BLOCK-UIDS
  ["" ;; on page, not block
   "51c3580f5" ;; poseidon
   "ed9f20b26" ;; way down
   "8b66a56f3" ;; different page
   "4135c0ecb" ;; different page on a block
   ""])


(def MEMBERS
  (mapv
   (fn [username color uid]
     {:username username :color color :block/uid uid})
   NAMES PALETTE BLOCK-UIDS))


;; Avatar

(defn avatar-svg
  [props & children]
  [:svg (merge (use-style {:height          "1.5em"
                           :width           "1.5em"
                           :overflow        "hidden"
                           :border-radius   "1000em"
                           ::stylefy/manual [[:text {:font-weight "bold"}]]})
               props)
   children])


(defn avatar-el
  "Takes a member map for the user data.
  Optionally takes some props for things like fill."
  ([member]
   [avatar-el member {:filled true}])
  ([{:keys [username color]} {:keys [filled]}]
   (let [initials (first username)]
     [avatar-svg {:viewBox "0 0 24 24"
                  :vectorEffect "non-scaling-stroke"}
      [:circle {:cx          12
                :cy          12
                :r           12
                :fill        color
                :stroke      color
                :fillOpacity (when-not filled 0.1)
                :strokeWidth (if filled 0 "3px")
                :key "circle"}]
      [:text {:width      24
              :x          12
              :y          "72%"
              :font-size  16
              :fill       (if filled "#fff" color)
              :textAnchor "middle"
              :key "text"}
       initials]])))



(def avatar-stack-style
  {:display "flex"
   ::stylefy/manual [[:svg {:width "1.5rem"
                            :height "1.5rem"}
                      ; In a stack, each sequential item sucks in the spacing
                      ; from the item before it
                      ["&:not(:first-child)" {:margin-left "-0.8rem"}]
                      ; All but the last get a slice masked out for readability
                      ;
                      ; I'm not clear on why 1.55rem / 1.1rem work in this case
                      ; It'd be nice to have a simpler masking method
                      ; or a better-constructed string with some documentation
                      ["&:not(:last-child)" {:mask-image "radial-gradient(1.55rem 1.1rem at 160% 50%, transparent calc(96%), #000 100%)"
                                             :-webkit-mask-image "radial-gradient(1.55rem 1.1rem at 160% 50%, transparent calc(96%), #000 100%)"}]]]})


(defn avatar-stack-el
  [& children]
  [:div (use-style avatar-stack-style)
   children])


;; List

(defn list-el
  [& children]
  [:ul (use-style {:padding        0
                   :margin         0
                   :display        "flex"
                   :flex-direction "column"
                   :list-style     "none"})
   children])


(defn list-header-el
  [& children]
  [:header (use-style {:border-bottom "1px solid #ddd"
                       :padding "0.25rem 0.5rem"
                       :display "flex"
                       :justify-content "space-between"
                       :align-items "center"})
   children])



(defn list-section-header-el
  [& children]
  [:li (use-style {:font-size "12px"
                   :font-weight "bold"
                   :opacity "0.5"
                   :padding "1rem 1rem 0.25rem"})
   children])


(defn list-header-url-el
  [& children]
  [:span (use-style {:font-size     "12px"
                     :font-weight   "700"
                     :display       "inline-block"
                     :opacity       "0.5"
                     :padding       "0.5rem"
                     :user-select   "all"
                     :margin-right  "1em"
                     :flex          "1 1 100%"
                     :white-space   "nowrap"
                     :text-overflow "hidden"})
   children])


(defn list-separator-el
  []
  [:li (use-style {:margin "0.5rem 0 0.5rem 1rem"
                   :border-bottom "1px solid #ddd"})])


(def member-list-item-style
  {:padding "0.375rem 1rem"
   :display "flex"
   :font-size "14px"
   :align-items "center"
   :font-weight "600"
   :color (style/color :body-text-color :opacity-higher)
   :transition "backdrop-filter 0.1s ease"
   :cursor "default"
   ::stylefy/manual [[:svg {:margin-right "0.25rem"}]]})
   ;; turn off interactive button stylings until we implement interactions like "jump" or "follow"
                     ;;[:&:hover {:background (style/color :body-text-color :opacity-lower)}]
                     ;;[:&:active
                     ;; :&:hover:active
                     ;; :&.is-active {:color (style/color :body-text-color)
                     ;;               :background (style/color :body-text-color :opacity-lower)}]
                     ;;[:&:active
                     ;; :&:hover:active
                     ;; :&:active.is-active {:background (style/color :body-text-color :opacity-low)}]
                     ;;[:&:disabled :&:disabled:active {:color (style/color :body-text-color :opacity-low)
                     ;;                                 :background (style/color :body-text-color :opacity-lower)
                     ;;                                 :cursor "default"}]]})



;; event
:presence/ping

;; re-frame db
{:presence/users {"user-id-1" {:username  "Zeus"
                               :block/uid "asd123"
                               :page/uid  "page-1"}}}


(defn member-item-el
  [member filled?]
  [:li (use-style member-list-item-style #_{:on-click #(prn member)})
   [avatar-el member filled?]
   (:username member)])


(defn toolbar-presence
  []
  (r/with-let [ele (r/atom nil)]
    (let [same-page-members (take 3 MEMBERS)
          online-members    (drop 3 MEMBERS)]
      [:<>

                 ;; Preview
       [button {:on-click #(reset! ele (.-currentTarget %))}
        [:<>
         [avatar-stack-el
          (for [member same-page-members]
            [avatar-el member])]]]

                 ;; Dropdown
       [m-popover
        {:open            (boolean (and @ele))
         :anchorEl        @ele
         :onClose         #(reset! ele nil)
         :anchorOrigin    #js{:vertical   "bottom"
                              :horizontal "center"}
         :transformOrigin #js{:vertical   "top"
                              :horizontal "center"}}
        [list-header-el
         [list-header-url-el "ath.ns/34op5fds0a"]
         [button [:> Link]]]

        [list-el
                   ;; On same page
         [list-section-header-el "On This Page"]
         (for [member same-page-members]
           [member-item-el member {:filled true}])

                   ;; Online, different page
         [list-separator-el]
         (for [member online-members]
           [member-item-el member {:filled false}])]]])))
