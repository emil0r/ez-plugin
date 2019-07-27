(ns ez-plugin.core
  (:require [clojure.edn :as edn]
            [clojure.string :as str])
  (:import [java.util.jar JarFile]))


(defn list-jar
  "List the content of a directory for a given JAR file"
  [jar-path inner-dir]
  (if-let [jar          (JarFile. jar-path)]
    (let [inner-dir    (if (and (not= "" inner-dir) (not= "/" (last inner-dir)))
                         (str inner-dir "/")
                         inner-dir)
          entries      (enumeration-seq (.entries jar))
          names        (map (fn [x] (.getName x)) entries)
          snames       (filter (fn [x] (= 0 (.indexOf x inner-dir))) names)
          fsnames      (map #(subs % (count inner-dir)) snames)]
      fsnames)))

(defn read-from-jar
  "Read the content from a JAR file given a file path. Returns a string"
  [jar-path inner-path]
  (if-let [jar   (JarFile. jar-path)]
    (if-let [entry (.getJarEntry jar inner-path)]
      (slurp (.getInputStream jar entry)))))


(defn load-plugin
  "Loads a plugin based on a provided path to hooks to be loaded.
  Requires a context map.
  Can bubble up exceptions, or silently supress them."
  ([jar-path hook-path context]
   (load-plugin jar-path hook-path context))
  ([jar-path hook-path context bubble?]
   (try
     (if-let [file (read-from-jar jar-path hook-path)]
       (let [{:keys [:ez-plugin/hooks]} (edn/read-string file)]
         (reduce (fn [out hook]
                   ;; get the namespace from our hook
                   (let [-ns (namespace hook)]
                     ;; require the namespace. this will load everything in the namespace
                     (require (symbol -ns))
                     ;; call the hook with our context map, collect the result to be returned
                     (conj out ((resolve hook) context))))
                 [] hooks)))
     (catch Exception e
       (if bubble?
         (throw e))))))

(defn load-plugins
  "Loads all plugins given a hook-path"
  ([hook-path context]
   (load-plugins hook-path context false))
  ([hook-path context bubble?]
   (let [jar-paths (as-> (System/getProperty "java.class.path") $
                     (str/split $ #":")
                     (filter #(str/ends-with? % ".jar") $))]
     (->> jar-paths
          (reduce (fn [out jar-path]
                    (conj out (load-plugin jar-path hook-path context bubble?)))
                  [])
          (flatten)
          (remove nil?)))))
