(ns ez-plugin-test1.core)

(defn hook [context]
  (println context)
  {::hook :loaded})

