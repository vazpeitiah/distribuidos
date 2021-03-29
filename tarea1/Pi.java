import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

public class Pi{
	static Object lock = new Object();
	static double pi = 0;

	static class Worker extends Thread {
		Socket connection;
		Worker(Socket connection){
			this.connection = connection;
		}
		public void run(){
			//Algoritmo 1
			try {
    			DataInputStream dis = new DataInputStream(connection.getInputStream());	
				double x = 0;
				x = dis.readDouble();
				synchronized(lock) 
				{ 
					pi += x;
				} 

				dis.close();
				connection.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}

	} 

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage: java Pi <nodo>");
			System.exit(0);
		}
		int nodo = Integer.valueOf(args[0]);
		if (nodo == 0) {
			// Algoritmo 2
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
		} else {
			// Algoritmo 3
			Socket connection = null;

			for(;;)
			try {
				connection = new Socket("localhost",50000);
				break;
			} catch (Exception e) {
				Thread.sleep(100);
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
		}
	}
}