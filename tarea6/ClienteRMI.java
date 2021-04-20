import java.rmi.Naming;

public class ClienteRMI {

  private static final int N = ClaseRMI.N;

  static int[][] separa_matriz(int[][] A, int inicio) {
    int[][] M = new int[N / 2][N];
    for (int i = 0; i < N / 2; i++)
      for (int j = 0; j < N; j++)
        M[i][j] = A[i + inicio][j];
    return M;
  }

  static void acomoda_matriz(int[][] C, int[][] A, int renglon, int columna) {
    for (int i = 0; i < N / 2; i++)
      for (int j = 0; j < N / 2; j++)
        C[i + renglon][j + columna] = A[i][j];
  }

  static void printMatrix(int matrix[][], int rows, int cols) {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        System.out.printf("%4d", matrix[i][j]);
      }
      System.out.println("");
    }
  }

  public static void main(String args[]) throws Exception {
    // en este caso el objeto remoto se llama "prueba", notar que se utiliza el
    // puerto default 1099
    //String url = "rmi://localhost/prueba";
    // obtiene una referencia que "apunta" al objeto remoto asociado a la URL
    //InterfaceRMI r = (InterfaceRMI) Naming.lookup(url);

    int[][] A = new int[N][N];
    int[][] B = new int[N][N];
    int[][] C = new int[N][N];

    // Inicializar las matrices A, B y C
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        A[i][j] = i + 3 * j;
        B[i][j] = i - 3 * j;
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

    // Separar la matriz en matrices más pequeñas
    int[][] A1 = separa_matriz(A, 0);
    int[][] A2 = separa_matriz(A, N / 2);
    int[][] B1 = separa_matriz(B, 0);
    int[][] B2 = separa_matriz(B, N / 2);

    // obtiene una referencia que "apunta" al objeto remoto asociado a la URL
    InterfaceRMI nodo1 = (InterfaceRMI) Naming.lookup("rmi://localhost/multmatrix");
    InterfaceRMI nodo2 = (InterfaceRMI) Naming.lookup("rmi://localhost/multmatrix");

    // Multiplica las matrices
    int[][] C1 = nodo1.multiplica_matrices(A1, B1);
    int[][] C2 = nodo1.multiplica_matrices(A1, B2);
    int[][] C3 = nodo2.multiplica_matrices(A2, B1);
    int[][] C4 = nodo2.multiplica_matrices(A2, B2);

    // Une la matriz resultante
    acomoda_matriz(C, C1, 0, 0);
    acomoda_matriz(C, C2, 0, N / 2);
    acomoda_matriz(C, C3, N / 2, 0);
    acomoda_matriz(C, C4, N / 2, N / 2);

    System.out.println("Matriz C:");
    printMatrix(C, N, N);
  }
}
