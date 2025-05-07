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
        System.out.println("Server received list to broadcast: " + numbers);
        System.out.println("Broadcasting to " + clients.size() + " clients...");
        for (ClientInterface client : clients) {
            System.out.println("Sending to client: " + client);
            client.sortAndDisplay(new ArrayList<>(numbers));
        }
        System.out.println("Broadcast complete.");
    }

}
