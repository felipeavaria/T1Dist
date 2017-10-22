JFLAGS = -g
JC = javac
J = java
MAIN = serv_distrito
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	serv_distrito.java \

default: clean classes run

classes: $(CLASSES:.java=.class) \

run: classes 
	$(J) $(MAIN)

clean:
	$(RM) *.class


