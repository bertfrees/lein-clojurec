# {{name}}

TODO

## Building

Prerequisites:

* glib-2.0
  - Mac: `brew install glib`
  - Linux: `aptitude install libglib2.0-dev`
* bdw-gc
  - Mac: `brew install bdw-gc`
  - Linux: `aptitude install libgc-dev`
* pcre
  - Linux: `aptitude install libpcre3-dev`

First time only:

```sh
lein git-deps
( cd .lein-git-deps/clojurec && git submodule init && git submodule update )
chmod +x tools/*
```

To build executable:

```sh
make
```

## License

Copyright © {{year}} TODO

Distributed under the Eclipse Public License, the same as Clojure.
