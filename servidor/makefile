JFLAGS = -g
JC = javac
J = java
MAIN = servidor
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	servidor.java \

default: clean classes run

classes: $(CLASSES:.java=.class) \

run: classes 
	$(J) $(MAIN)

clean:
	$(RM) *.class


