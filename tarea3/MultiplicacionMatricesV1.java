import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

public class MultiplicacionMatrices{
	static Object lock = new Object();
	static long checksum = 0;
    static int N = 4;
    static int[][] A = new int[N][N];
    static int[][] B = new int[N][N];
    static int[][] C = new int[N][N];

	static class Worker extends Thread {
		Socket connection;
        int nodo;
		Worker(Socket connection){
			this.connection = connection;
		}
		public void run(){
			//Algoritmo 1
			try {
    			DataInputStream entrada = new DataInputStream(connection.getInputStream());
                DataOutputStream salida = new DataOutputStream(connection.getOutputStream());
				int ai[][] = new int[N/2][N];
				int bi[][] = new int[N/2][N];
				
				int nodo = entrada.readInt(); //Leemos el numero de nodo

                if(nodo == 1){
                    for (int i = 0; i < N/2; i++) {
                        for (int j = 0; j < N; j++) {
                            ai[i][j] = A[i][j];
                            bi[i][j] = B[i][j];
                        }
                    }
                }else if(nodo == 2){
                    for (int i = 0; i < N/2; i++) {
                        for (int j = 0; j < N; j++) {
                            ai[i][j] = A[i][j];
                            bi[i][j] = B[i + N / 2][j];
                        }
                    }
                } else if(nodo == 3){
                    for (int i = 0; i < N/2; i++) {
                        for (int j = 0; j < N; j++) {
                            ai[i][j] = A[i + N / 2][j];
                            bi[i][j] = B[i][j];
                        }
                    }
                } else if(nodo == 4){
                    for (int i = 0; i < N/2; i++) {
                        for (int j = 0; j < N; j++) {
                            ai[i][j] = A[i + N / 2][j];
                            bi[i][j] = B[i + N / 2][j];
                        }
                    }
                }

                sendMatrix(ai, N/2, N, salida);
				sendMatrix(bi, N/2, N, salida);

				int ci[][] = recvMatrix(N/2, N/2, entrada);

				if(nodo == 1){
					for (int i = 0; i < N/2; i++) {
						for (int j = 0; j < N/2; j++) {
							C[i][j] = ci[i][j];
						}
					}
				}else if(nodo == 2){
					for (int i = 0; i < N/2; i++) {
						for (int j = 0; j < N/2; j++) {
							C[i][j +  N / 2] = ci[i][j];
						}
					}
				} else if(nodo == 3){
					for (int i = 0; i < N/2; i++) {
						for (int j = 0; j < N/2; j++) {
							C[i + N / 2][j] = ci[i][j];
						}
					}
				} else if(nodo == 4){
					for (int i = 0; i < N/2; i++) {
						for (int j = 0; j < N/2; j++) {
							C[i + N / 2][j +  N / 2] = ci[i][j];
						}
					}
				}
				entrada.close();
                salida.close();
				connection.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}

	}
	static void read(DataInputStream f,byte[] b,int posicion,int longitud) throws Exception {
		while (longitud > 0) {
			int n = f.read(b,posicion,longitud);
			posicion += n;
			longitud -= n;
		}
	}

	static void printMatrix(int matrix[][], int rows, int cols){
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				System.out.printf("%4d", matrix[i][j]);
			}
			System.out.println("");
		}
	}

	static void sendMatrix(int matrix[][], int rows, int cols, DataOutputStream salida){
		for (int i = 0; i < rows; i++) {
			ByteBuffer bf = ByteBuffer.allocate(cols*4);
			for (int j = 0; j < cols; j++) {
				bf.putInt(matrix[i][j]);
			}
			byte[] bytes = bf.array();
			try {
				salida.write(bytes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	static int[][] recvMatrix(int rows, int cols, DataInputStream entrada)	{
		int matrix[][] = new int[rows][cols];
		for (int i = 0; i < rows; i++) {
			byte[] bytes = new byte[cols * 4];
			try{
				read(entrada, bytes, 0, cols * 4);
				ByteBuffer bf = ByteBuffer.wrap(bytes);
				for (int j = 0; j < cols; j++)
					matrix[i][j] = bf.getInt();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		return matrix;
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage: java Pi <nodo>");
			System.exit(0);
		}
		int nodo = Integer.valueOf(args[0]);
		if (nodo == 0) {
			ServerSocket server = new ServerSocket(50000);
			Worker w[] = new Worker[4];

            // inicializa las matrices A y B
            for (int i = 0; i < N; i++){
                for (int j = 0; j < N; j++)
                {
                    A[i][j] = 2 * i + 3 * j;
                    B[i][j] = 2 * i - 3 * j;
                    C[i][j] = 0;
                }
            }

			System.out.println("Matriz A:");
			printMatrix(A, N, N);
			System.out.println("Matriz B:");
			printMatrix(B, N, N);
            // transpone la matriz B, la matriz traspuesta queda en B
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < i; j++) {
                    int x = B[i][j];
                    B[i][j] = B[j][i];
                    B[j][i] = x;
                }
            }

			System.out.println("Matriz B^T:");
			printMatrix(B, N, N);
			
			for(int i=0; i < 4; i++){
				Socket client = server.accept();
				w[i] = new Worker(client);
				w[i].start();
			}
			for(int i=0; i < 4; i++){
				w[i].join();
			}

			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					checksum += C[i][j];
				}
			}
			System.out.println("check sum = " + checksum);

			/* System.out.println("Matriz C:");
			printMatrix(C, N, N); */

			server.close();
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
			
			DataInputStream entrada = new DataInputStream(connection.getInputStream());
            DataOutputStream salida = new DataOutputStream(connection.getOutputStream());

			salida.writeInt(nodo);

			int ai[][] = recvMatrix(N/2, N, entrada);
			int bi[][] = recvMatrix(N/2, N, entrada);
			int ci[][] = new int[N/2][N/2];

			for (int i = 0; i < N/2; i++)
				for (int j = 0; j < N/2; j++)
					for (int k = 0; k < N; k++)
						ci[i][j] += ai[i][k] * bi[j][k];

			sendMatrix(ci, N/2, N/2, salida);

			entrada.close();
			salida.close();
			connection.close();
		}
	}
}