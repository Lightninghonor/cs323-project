ANTLR_JAR ?= libs/antlr-4.13.2-complete.jar
JAVA_FLAGS := -Xms16m -Xmx256m -XX:+UseSerialGC
JAVAC_FLAGS := $(addprefix -J,$(JAVA_FLAGS))
CLASSPATH := build/classes:$(ANTLR_JAR)

.PHONY: all generate build run run-listener test clean
all: build

$(ANTLR_JAR):
	mkdir -p $(dir $(ANTLR_JAR))
	curl --fail --location --output $@.tmp https://www.antlr.org/download/antlr-4.13.2-complete.jar
	mv $@.tmp $@

generate: build/generated/Calc1Parser.java

build/generated/Calc1Parser.java: Calc1.g4 $(ANTLR_JAR)
	mkdir -p build/generated
	java $(JAVA_FLAGS) -jar $(ANTLR_JAR) -visitor -listener \
		-package generated.Calc1 -o build/generated Calc1.g4

build: generate
	mkdir -p build/classes
	javac $(JAVAC_FLAGS) -cp $(ANTLR_JAR) -d build/classes \
		build/generated/*.java src/main/java/*.java

run: build
	java $(JAVA_FLAGS) -cp $(CLASSPATH) Main

run-listener: build
	java $(JAVA_FLAGS) -cp $(CLASSPATH) ListenerMain

test: build
	mkdir -p build/test-classes
	javac $(JAVAC_FLAGS) -cp $(CLASSPATH) -d build/test-classes src/test/java/*.java
	java $(JAVA_FLAGS) -cp build/test-classes:$(CLASSPATH) CalcLab5Test
	java $(JAVA_FLAGS) -cp $(CLASSPATH) Main > build/visitor.out
	diff -u tests/expected.out build/visitor.out
	java $(JAVA_FLAGS) -cp $(CLASSPATH) ListenerMain > build/listener.out
	diff -u tests/expected.out build/listener.out
	@echo "[PASS] Both sample outputs exactly match tests/expected.out"

clean:
	rm -rf build
