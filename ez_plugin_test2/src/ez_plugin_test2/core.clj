(ns ez-plugin-test2.core)

(defn hook [context]
  (println context)
  {::hook :loaded})
