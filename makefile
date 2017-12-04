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
	TheToken.java \
	IntObjeto.java \
	InterfazProceso.java \
	InterfazLista.java \
	InterfazToken.java \
	Proceso.java \
	Lista.java \
	Token.java \
	OToken.java \
	Servidor.java \
	Semaforo.java

CLASSES2 = \
	InterfazToken.java \
	Token.java 

default: clean classes classes2 

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


