(ns {{ns-name}}.core
  (:require
   [reagent.core :as reagent]
   [petrol.core :as petrol]{{#re-frisk?}}
   [re-frisk.core :as rf]{{/re-frisk?}}
   ))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Model

(def initial-state
  {:counter 0})

(defonce app-state
  (reagent/atom initial-state))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Update

(defrecord Decrement []
  petrol/Message
  (process-message [_ app]
    (update app :counter dec)))

(defrecord Increment []
  petrol/Message
  (process-message [_ app]
    (update app :counter inc)))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; View

(defn page [ui-channel app]
  [:div
   [:button {:on-click (petrol/send! ui-channel (->Decrement))}
    "Decrement"]
   [:button {:on-click (petrol/send! ui-channel (->Increment))}
    "Increment"]
   [:p "Count: " (:counter app)]])



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Initialize App

(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    (println "dev mode"){{#re-frisk?}}
    (rf/enable-frisk!)
    (rf/add-data :app-state app-state){{/re-frisk?}}
    ))

(defn reload []
  (swap! app-state identity))

(defn render-fn [ui-channel app]
  (reagent/render [page ui-channel app]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (petrol/start-message-loop! app-state render-fn))
