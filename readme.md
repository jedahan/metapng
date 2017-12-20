metapng hides something wonderful inside pngs

<img src="meta.png" alt="metapng" title="metapng" align="right" width=200 />

we can see what it knows...

```clojure
    (:require [metapng.core :as metapng]))
    (:name (metapng/get-metadata "meta.png"))
```

or perhaps write our own genesis when playing with quil...

```clojure
    (metapng/bake "boring.png" "meta.png" {:code (slurp *file*)})
```
