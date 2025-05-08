// BullyAlgorithm.java
import java.util.*;

public class BullyAlgorithm {
    private List<Integer> processes;
    private Integer coordinator;

    public BullyAlgorithm(List<Integer> processes) {
        this.processes = new ArrayList<>(processes);
        this.coordinator = Collections.max(processes);
    }

    // Display current coordinator
    public void displayCoordinator() {
        if (coordinator != null)
            System.out.println("Current Coordinator: Process " + coordinator);
        else
            System.out.println("No coordinator currently.");
    }

    // Simulate coordinator failure
    public void failCoordinator() {
        System.out.println("\nProcess " + coordinator + " (Coordinator) has failed.");
        processes.remove(coordinator);
        coordinator = null;
    }

    // Election logic
    public void startElection(int initiator) {
        System.out.println("\nProcess " + initiator + " starts election.");
        boolean higherExists = false;

        for (int process : processes) {
            if (process > initiator) {
                System.out.println("Election message sent from Process " + initiator + " to Process " + process);
                System.out.println("Process " + process + " responds to Process " + initiator);
                higherExists = true;
            }
        }

        if (!higherExists) {
            coordinator = initiator;
            System.out.println("Process " + initiator + " becomes the new coordinator.");
        } else {
            coordinator = Collections.max(processes);
            System.out.println("Process " + coordinator + " becomes the new coordinator.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter process IDs (space-separated): ");
        String[] input = scanner.nextLine().split(" ");
        List<Integer> processes = new ArrayList<>();
        for (String s : input) processes.add(Integer.parseInt(s));

        BullyAlgorithm bully = new BullyAlgorithm(processes);
        bully.displayCoordinator();

        bully.failCoordinator();

        System.out.print("Enter the ID of the initiator process: ");
        int initiator = scanner.nextInt();
        if (!bully.processes.contains(initiator)) {
            System.out.println("Invalid initiator process.");
            return;
        }

        bully.startElection(initiator);
    }
}


// javac BullyAlgorithm.java
// javac RingAlgorithm.java
