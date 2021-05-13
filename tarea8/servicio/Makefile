JC = javac
JVM= java 

.SUFFIXES: .java .class

.java.class:
	$(JC) -cp ./lib/javax.ws.rs-api-2.0.1.jar:./lib/gson-2.3.1.jar:. -d ./WEB-INF/classes $*.java

CLASSES = ./negocio/Servicio.java

default: classes

classes: $(CLASSES:.java=.class)

deploy: ./WEB-INF/classes/negocio/Servicio.class
	jar cvf Servicio.war WEB-INF META-INF

clean:
	$(RM) ./WEB-INF/classes/negocio/*.class && $(RM) Servicio.war
