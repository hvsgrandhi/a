package Client;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Server.ServerInterface;

public class ClientMain {
    public static void main(String[] args) {
        try {
            ServerInterface server = (ServerInterface) Naming.lookup("rmi://localhost/Server");
            ClientInterface client = new ClientImplementation();
            server.registerClient(client);

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter space-separated numbers to broadcast: ");
            String input = scanner.nextLine();

            String[] tokens = input.trim().split("\\s+");
            List<Integer> numbers = new ArrayList<>();
            for (String token : tokens) {
                numbers.add(Integer.parseInt(token));
            }

            server.broadcastNumbers(numbers);
            scanner.close();
        } catch (Exception e) {
            System.out.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
