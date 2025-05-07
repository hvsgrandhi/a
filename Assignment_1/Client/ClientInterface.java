package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientInterface extends Remote {
    void sortAndDisplay(List<Integer> numbers) throws RemoteException;
}
