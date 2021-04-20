import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceRMI extends Remote {
  public int[][] multiplica_matrices(int[][] A, int[][] B) throws RemoteException;
}
