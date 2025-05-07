package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import Client.ClientInterface;

public interface ServerInterface extends Remote {
    void registerClient(ClientInterface client) throws RemoteException;
    void broadcastNumbers(List<Integer> numbers) throws RemoteException;
}
