(defproject ez-plugin "0.1.0-SNAPSHOT"

  :description "Library for creating a plugin system."

  :url "https://github.com/emil0r/ez-plugin/"

  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.10.0"]]

  :profiles {:dev {:dependencies [[ez-plugin-test1 "0.1.0-SNAPSHOT"]
                                  [ez-plugin-test2 "0.1.0-SNAPSHOT"]]}}

  :repl-options {:init-ns ez-plugin.core})
