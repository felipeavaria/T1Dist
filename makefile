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
	InterfazToken.java \
	Token.java \
	Servidor.java

CLASSES2 = \
	InterfazToken.java \
	Token.java \
	Semaforo.java

default: clean classes classes2 rmic

classes: $(CLASSES:.java=.class) \

classes2: $(CLASSES2:.java=.class) \

rmic: 
	rmic Token

run: rmic 
	$(J) $(MAIN)

clean:
	$(RM) *.class

cliente: 
	$(J) Cliente

servidor: 
	$(J) Servidor

rmi:
	rmiregistry


