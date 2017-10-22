JFLAGS = -g
JC = javac
J = java
MAIN = cliente
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	cliente.java \

default: clean classes run

classes: $(CLASSES:.java=.class) \

run: classes 
	$(J) $(MAIN)

clean:
	$(RM) *.class


