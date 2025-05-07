package Client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.List;

public class ClientImplementation extends UnicastRemoteObject implements ClientInterface {

    protected ClientImplementation() throws RemoteException {
        super();
    }

    @Override
    public void sortAndDisplay(List<Integer> numbers) throws RemoteException {
        System.out.println("Received list from server: " + numbers);
        Collections.sort(numbers);
        System.out.println("Client sorted and displaying: " + numbers);
    }
    
}
