JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $*.java

CLASSES = \
        InterfaceRMI.java \
        ClaseRMI.java \
        ClienteRMI.java \
		ServidorRMI.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	rm *.class
