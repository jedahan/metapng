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
  "Extract hashmap of metadata from file"
  [^String filename]
  (->> filename
    (File.)
    (PngReader.)
    (.getChunksList)
    (.getChunks)
    (filter #(ChunkHelper/isText %))
    (map #(vector (keyword (.getKey %)) (.getVal %)))
    (into {})))

(defn set-metadata
  "Set a metadata field on a PNG, creating metadata if needed"
  [^IIOImage image [text-keyword text-text]]
  (if (nil? (.getMetadata image))
    (.setMetadata image (PNGMetadata.)))
  (let [metadata (.getMetadata image)]
    (.add (.tEXt_keyword metadata) (name text-keyword))
    (.add (.tEXt_text metadata) text-text))
  image)

(defn write-image
  "Write the image to a file"
  [^String filename ^RenderedImage image]
  (let [writer (.next (ImageIO/getImageWritersBySuffix "png"))]
     (.setOutput writer (FileImageOutputStream. (File. filename)))
     (.write writer nil image nil)))

(defn load-image
  "Load image from file"
  [^String filename]
  (IIOImage. (ImageIO/read (File. filename)) nil nil))

(defmulti bake (fn [image-or-file & _] (class image-or-file)))

(defmethod bake IIOImage [image filename metadata]
  (write-image filename
    (reduce set-metadata image metadata)))

(defmethod bake String [input-filename filename new-metadata]
   (let [image (load-image input-filename)
         metadata (conj new-metadata (get-metadata input-filename))]
    (bake image filename metadata)))
