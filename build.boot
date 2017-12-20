(def project 'metapng)
(def version "0.1.1")

(set-env! :dependencies '[[adzerk/bootlaces  "0.1.13" :scope "test"]])

(task-options!
  pom {:project project
       :version version
       :description "read and write png metadata"
       :url "https://jedahan.com/metapng"
       :scm {:url "https://github.com/jedahan/meapng" }})

(require '[adzerk.bootlaces :refer :all])

(bootlaces! version)
