(ns metapng.tests
  (:require [metapng.core :as metapng]
            [clojure.test :as t]))

(defn get-metadata []
  (let [metadata (metapng/get-metadata "meta.png")]
    (t/is (= "metapod" (:name metadata)))
    (t/is (= "11" (:id metadata)))))
