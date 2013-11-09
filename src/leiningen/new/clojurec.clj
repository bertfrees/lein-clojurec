(ns leiningen.new.clojurec
  (:use [leiningen.new.templates]))

(defn clojurec
  "Create a new ClojureC project"
  [name]
  (let [render (renderer "clojurec")
        project-name (project-name name)
        main-ns (multi-segment (sanitize-ns name))
        data {:raw-name name
              :name project-name
              :main-ns main-ns
              :nested-dirs (name-to-path main-ns)
              :year (year)}]
    (println (str "Generating a new ClojureC project named " name "..."))
    (->files data
             [".gitignore"                       (render "gitignore")]
             ["README.md"                        (render "README.md" data)]
             ["project.clj"                      (render "project.clj" data)]
             ["Makefile"                         (render "Makefile" data)]
             ["tools/cljc-compile"               (render "tools/cljc-compile")]
             ["tools/cljc-depsort"               (render "tools/cljc-depsort")]
             ["tools/cljc-gen-main"              (render "tools/cljc-gen-main")]
             ["src/cljc/{{nested-dirs}}.cljc"    (render "src/cljc/foo.cljc" data)]
             "src/c")
    (println (str "\n"
                  "Done! Next steps:\n"
                  "----------------------------------------------------------------------------\n"
                  "cd " project-name "\n"
                  "lein git-deps\n"
                  "( cd .lein-git-deps/clojurec && git submodule init && git submodule update )\n"
                  "chmod +x tools/*\n"
                  "----------------------------------------------------------------------------"))))
