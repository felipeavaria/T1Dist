JFLAGS = -g
JC = javac
J = java
RMIC = rmic
MAIN = Servidor
ROBJ = ObjetoRemoto
CLASSPATH = ./
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	IRemota.java \
	ObjetoRemoto.java \
	Servidor.java

CLASSES2 = \
	IRemota.java \
	ObjetoRemoto.java \
	Cliente.java

default: clean classes classes2 rmic

classes: $(CLASSES:.java=.class) \

classes2: $(CLASSES2:.java=.class) \

rmic: 
	rmic ObjetoRemoto

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


