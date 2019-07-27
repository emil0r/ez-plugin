(ns ez-plugin.core-test
  (:require [clojure.test :refer :all]
            [ez-plugin.core :refer :all]))

(deftest a-test
  (testing "Load plugin.edn"
    (is (= [#:ez-plugin-test1.core{:hook :loaded} #:ez-plugin-test2.core{:hook :loaded}]
           (load-plugins "plugin.edn" {})))))
