# Tarea 1. Cálculo de PI

## Descripición del problema

En esta tarea vamos a desarrollar un programa distribuido, el cual calculará una aproximación de PI utilizando la serie de **[Gregory-Leibniz](https://crypto.stanford.edu/pbc/notes/pi/glseries.html)**. La serie tiene la siguiente forma:  4/1-4/3+4/5-4/7+4/9-4/11+4/13-4/15 ... Notar que los denominadores son los números impares:  1, 3, 5, 7, 9, 11, 13 . 

El programa va a ejecutar en forma distribuida sobre cinco nodos, cada nodo sería una computadora diferente. Por lo pronto, vamos a probar el programa en una sola computadora utilizando cinco ventanas de comandos de Windows o cinco terminales de Linux, en cada ventana representará un nodo (una instancia del programa). Cada nodo (excepto el nodo 0) deberá calcular 10 millones de términos de la serie. Implementaremos la siguiente topología lógica de tipo estrella, cada nodo se ha identificado con un número entero:

![https://s3.us-west-2.amazonaws.com/secure.notion-static.com/e77b251f-d5c8-49fd-b12d-57ec1a8a5a21/descarga.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAT73L2G45O3KS52Y5%2F20210329%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20210329T033636Z&X-Amz-Expires=86400&X-Amz-Signature=8fd722c77a1105caf1b68014e881459eb3c56e11d153a498e1cbc0b38a0b1075&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22descarga.png%22]

El nodo 0 actuará como servidor y los nodos 1, 2, 3 y 4 actuarán como clientes. Se debe desarrollar **un solo programa**, por tanto  será necesario pasar como parámetro al programa el número de nodo actual, de manera que el programa pueda actual como servidor o como cliente, según el número de nodo que pasa como parámetro.


## Formato de entrega

Se deberá subir a la plataforma un archivo en formato ZIP que contenga el código fuente del programa desarrollado y un reporte como documento PDF con la captura de pantalla de la compilación y ejecución del programa. El reporte deberá incluir portada, una descripción de cada captura de pantalla y conclusiones. No se admitirán archivos en formato Word o en formato RAR.

## Puntaje de la tarea

Valor de la tarea: 30% (1.8 punto de la primera evaluación parcial)

## Desarrollo de la prática

Se realizó la implementación de los 3 algoritmos de la siguiente manera

**Algoritmo 1**

```java
try {
	DataInputStream dis = new DataInputStream(connection.getInputStream());	
	double x = 0;
	x = dis.readDouble();
	synchronized(lock) { 
		pi += x;
	} 

dis.close();
connection.close();
} catch (IOException e){
	e.printStackTrace();
}
```

**Algoritmo 2**

```java
ServerSocket server = new ServerSocket(50000);
Worker w[] = new Worker[4];
for(int i=0; i < 4; i++){
	Socket client = server.accept();
	w[i] = new Worker(client);
	w[i].start();
}
for(int i=0; i < 4; i++){
	w[i].join();
}
System.out.println("El valor de PI es: " + pi);
```

**Algoritmo 3**

```java
Socket connection = null;

for(;;){
	try {
		connection = new Socket("localhost",50000);
		break;
	} catch (Exception e) {
		Thread.sleep(100);
	}
}

DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
double sum = 0;
for(int i = 0; i <= 10000000; i++){
	sum += 4.0 / (8 * i + 2 * (nodo-2) + 3);
}
sum = (nodo % 2 == 0) ? -sum : sum;
dos.writeDouble(sum);

dos.close();
connection.close();
```

## Conclusiones

La práctica me pareció muy buena para recordar los conocimientos que había adquirido en la materia de Administración de servicios en Red. El uso de hilos y de herramientas para sincronizar los mismos. Además de darme una introducción a los sistemas distribuidos, de cómo podemos dividir el trabajo entre distintos procesos o hilos para que el tiempo de ejecución sea el mínimo.
