(ns metapng.tests
  (:require [metapng.core :as metapng]
            [clojure.test :as t]))

(defn get-metadata []
  (t/is (= (metapng/get-metadata "meta.png")
         {:test "beautiful"})))
