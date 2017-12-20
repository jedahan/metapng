(ns metapng.core
  "Utility for writing metadata into pngs"
  (:require [clojure.java.io :as io])
  (:import (java.io File)
           (ar.com.hjg.pngj PngReader)
           (ar.com.hjg.pngj.chunks ChunkHelper)
           (com.sun.imageio.plugins.png PNGMetadata)
           (java.awt.image RenderedImage)
           (javax.imageio ImageIO IIOImage)
           (javax.imageio.metadata IIOMetadata)
           (javax.imageio.stream FileImageOutputStream)))

(defn get-metadata
  "Extract hashmap of metadata from filename"
  [filename]
  (->> filename
    (File.)
    (PngReader.)
    (.getChunksList)
    (.getChunks)
    (filter #(ChunkHelper/isText %))
    (map (fn [ch] [(keyword (.getKey ch)) (.getVal ch)]))
    (flatten)
    (apply hash-map)))

(defn set-metadata
  "Set a metadata field on a PNG, creating metadata if needed."
  [^IIOImage image [text-keyword text-text]]
  (if (nil? (.getMetadata image))
    (.setMetadata image (PNGMetadata.)))
  (let [metadata (.getMetadata image)]
    (.add (.tEXt_keyword metadata) (name text-keyword))
    (.add (.tEXt_text metadata) text-text))
  image)

(defn write-image
  "Write the image to a file"
  [output-file-name ^RenderedImage image]
  (let [writer (.next (ImageIO/getImageWritersBySuffix "png"))]
     (.setOutput writer (FileImageOutputStream. (File. output-file-name)))
     (.write writer nil image nil)))

(defn bake
  "Bake keywords into an image

  (bake \"boring.png\" \"meta.png\" {:atk 20})
  "
  [input-filename output-filename new-metadata]
  (let [image (IIOImage. (ImageIO/read (File. input-filename)) nil nil)
        metadata (conj (get-metadata input-filename) new-metadata)]
    (write-image output-filename
                (reduce set-metadata image (vec metadata)))))