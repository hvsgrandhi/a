package Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import Client.ClientInterface;

public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {

    private List<ClientInterface> clients;

    public ServerImplementation() throws RemoteException {
        super();
        clients = new ArrayList<>();
    }

    @Override
    public void registerClient(ClientInterface client) throws RemoteException {
        clients.add(client);
        System.out.println("Client registered: " + client);
    }

    @Override
    public void broadcastNumbers(List<Integer> numbers) throws RemoteException {
        System.out.println("Broadcasting numbers: " + numbers);
        for (ClientInterface client : clients) {
            client.sortAndDisplay(new ArrayList<>(numbers)); // Send a copy
        }
    }
}
