JFLAGS = -g
JC = javac
J = java
ROBJ = Token
CLASSPATH = ./
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Token.java \
	InterfazProceso.java \
	InterfazLista.java \
	Proceso.java \
	Lista.java \
	Semaforo.java

CLASSES2 = \
	Token.java

default: clean classes

classes: $(CLASSES:.java=.class) \

clean:
	$(RM) *.class

semaforo:
	$(J) Semaforo

rmi:
	rmiregistry

ifeq (process,$(firstword $(MAKECMDGOALS)))
  # use the rest as arguments for "proceso"
  RUN_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  # ...and turn them into do-nothing targets
  $(eval $(RUN_ARGS):;@:)
endif

java: # ...
    # ...

.PHONY: process
process : java
	java Semaforo $(RUN_ARGS) \
