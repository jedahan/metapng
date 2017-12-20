metapng hides something wonderful inside pngs

<img src="meta.png" alt="metapng" title="metapng" align="right" width=400 />

we can see what it knows...

    (:require [metapng.core :as metapng]))
    (:name (metapng/get-metadata "meta.png"))

or perhaps write our own genesis when playing with quil...

    (metapng/bake "boring.png" "meta.png" {:code (slurp *file*)})
