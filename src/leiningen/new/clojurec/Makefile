#!/usr/bin/make -f

SHELL := /bin/bash
CC    := gcc -std=gnu99

SRC_C_DIR          := src/c
SRC_CLJC_DIR       := src/cljc
TARGET_O_DIR       := target/o
TARGET_C_DIR       := target/c
CLOJUREC_HOME      := .lein-git-deps/clojurec
EXEC               := target/bin/{{name}}
CLJC_SRC           := $(shell ./tools/cljc-depsort `find $(SRC_CLJC_DIR) -name '*.cljc' ! -name '.\#*'`)
CLJC_SRC_MAIN      := $(SRC_CLJC_DIR)/{{nested-dirs}}.cljc
C_SRC              := $(shell find $(SRC_C_DIR) -name '*.c' ! -name '.\#*')
C_FROM_CLJC_SRC    := $(patsubst $(SRC_CLJC_DIR)/%.cljc,$(TARGET_C_DIR)/%.c,$(CLJC_SRC))
O_FROM_C_SRC       := $(patsubst $(SRC_C_DIR)/%.c,$(TARGET_O_DIR)/%.o,$(C_SRC))
O_FROM_CLJC_SRC    := $(patsubst $(TARGET_C_DIR)/%.c,$(TARGET_O_DIR)/%.o, $(C_FROM_CLJC_SRC))
O                  := $(O_FROM_C_SRC) $(O_FROM_CLJC_SRC)

CFLAGS := -Wno-unused-variable -Wno-unused-value -Wno-unused-function -g -O0 \
          `pcre-config --cflags` `pkg-config --cflags bdw-gc glib-2.0` \
          -I$(SRC_C_DIR) -I$(CLOJUREC_HOME)/src/c -I$(CLOJUREC_HOME)/run/thirdparty/klib
LIBS := -lm -lpthread `pcre-config --libs` `pkg-config --libs bdw-gc glib-2.0`

all : exec

exec : $(EXEC)

clean :
	rm -rf target

$(EXEC) : $(TARGET_O_DIR)/_runtime.o $(TARGET_O_DIR)/cljc/core.o $(O) $(TARGET_O_DIR)/_main.o
	mkdir -p $(dir $@)
	$(CC) $(LIBS) $^ -o $@

$(O_FROM_C_SRC) : $(TARGET_O_DIR)/%.o : $(SRC_C_DIR)/%.c
	mkdir -p $(dir $@)
	$(CC) $(CFLAGS) -c $< -o $@

$(O_FROM_CLJC_SRC) $(TARGET_O_DIR)/cljc/core.o $(TARGET_O_DIR)/_main.o : $(TARGET_O_DIR)/%.o : $(TARGET_C_DIR)/%.c
	mkdir -p $(dir $@)
	$(CC) $(CFLAGS) -c $< -o $@

$(TARGET_O_DIR)/_runtime.o : $(CLOJUREC_HOME)/src/c/runtime.c
	mkdir -p $(dir $@)
	$(CC) $(CFLAGS) -c $< -o $@

$(C_FROM_CLJC_SRC) : $(TARGET_C_DIR)/%.c : $(SRC_CLJC_DIR)/%.cljc
	./tools/cljc-compile $(subst /,.,$(patsubst $(TARGET_C_DIR)/%.c,%,$@)) $(SRC_CLJC_DIR) $(TARGET_C_DIR)

$(TARGET_C_DIR)/cljc/core.c : $(CLOJUREC_HOME)/src/cljc/cljc/core.cljc
	./tools/cljc-compile cljc.core $(CLOJUREC_HOME)/src/cljc $(TARGET_C_DIR)

$(TARGET_C_DIR)/_main.c : $(CLJC_SRC_MAIN)
	./tools/cljc-gen-main $@ {{main-ns}}/-main

.PHONY: exec clean
