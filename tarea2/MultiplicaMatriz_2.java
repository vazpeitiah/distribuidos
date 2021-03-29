class MultiplicaMatriz_2
{
  static int N = 1000;
  static int[][] A = new int[N][N];
  static int[][] B = new int[N][N];
  static int[][] C = new int[N][N];

  public static void main(String[] args)
  {
    long t1 = System.currentTimeMillis();

    // inicializa las matrices A y B

    for (int i = 0; i < N; i++)
      for (int j = 0; j < N; j++)
      {
        A[i][j] = 2 * i - j;
        B[i][j] = i + 2 * j;
        C[i][j] = 0;
      }

    // transpone la matriz B, la matriz traspuesta queda en B

    for (int i = 0; i < N; i++)
      for (int j = 0; j < i; j++)
      {
        int x = B[i][j];
        B[i][j] = B[j][i];
        B[j][i] = x;
      }

    // multiplica la matriz A y la matriz B, el resultado queda en la matriz C
    // notar que los indices de la matriz B se han intercambiado

    for (int i = 0; i < N; i++)
      for (int j = 0; j < N; j++)
        for (int k = 0; k < N; k++)
           C[i][j] += A[i][k] * B[j][k];

    long t2 = System.currentTimeMillis();
    System.out.println("Tiempo: " + (t2 - t1) + "ms");
  }
}