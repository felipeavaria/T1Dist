JFLAGS = -g
JC = javac
J = java
RMIC = rmic
MAIN = Servidor
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

classes2: $(CLASSES2:.java=.class) \

rmic: 
	rmic Token

rmic2:
	rmic Lista

rmic3:
	rmic Proceso

run: rmic 
	$(J) $(MAIN)

clean:
	$(RM) *.class

semaforo: 
	$(J) Semaforo

servidor: 
	$(J) Servidor

rmi:
	rmiregistry

# If the first argument is "run"...
ifeq (proceso,$(firstword $(MAKECMDGOALS)))
  # use the rest as arguments for "proceso"
  RUN_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  # ...and turn them into do-nothing targets
  $(eval $(RUN_ARGS):;@:)
endif

java: # ...
    # ...

.PHONY: proceso
proceso : java
	java Semaforo $(RUN_ARGS)

