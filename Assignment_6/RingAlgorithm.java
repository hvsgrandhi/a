// RingAlgorithm.java
import java.util.*;

public class RingAlgorithm {
    private List<Integer> ring;
    private Integer coordinator;

    public RingAlgorithm(List<Integer> processes) {
        this.ring = new ArrayList<>(processes);
        Collections.sort(this.ring);
        this.coordinator = Collections.max(ring);
    }

    public void displayCoordinator() {
        if (coordinator != null)
            System.out.println("Current Coordinator: Process " + coordinator);
        else
            System.out.println("No coordinator currently.");
    }

    public void failCoordinator() {
        System.out.println("\nProcess " + coordinator + " (Coordinator) has failed.");
        ring.remove(coordinator);
        coordinator = null;
    }

    public void startElection(int initiator) {
        System.out.println("\nProcess " + initiator + " starts election.");
        List<Integer> activeList = new ArrayList<>();
        int index = ring.indexOf(initiator);
        int current = index;

        do {
            int next = (current + 1) % ring.size();
            int from = ring.get(current);
            int to = ring.get(next);
            System.out.println("Election message passed from Process " + from + " to Process " + to);
            activeList.add(to);
            current = next;
        } while (ring.get(current) != initiator);

        activeList.add(initiator);
        int newCoordinator = Collections.max(activeList);
        coordinator = newCoordinator;

        System.out.println("Active List: " + activeList);
        System.out.println("Process " + newCoordinator + " becomes the new coordinator.");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter process IDs (space-separated): ");
        String[] input = scanner.nextLine().split(" ");
        List<Integer> processes = new ArrayList<>();
        for (String s : input) processes.add(Integer.parseInt(s));

        RingAlgorithm ring = new RingAlgorithm(processes);
        ring.displayCoordinator();

        ring.failCoordinator();

        System.out.print("Enter the ID of the initiator process: ");
        int initiator = scanner.nextInt();
        if (!ring.ring.contains(initiator)) {
            System.out.println("Invalid initiator process.");
            return;
        }

        ring.startElection(initiator);
    }
}


// java BullyAlgorithm
// java RingAlgorithm
