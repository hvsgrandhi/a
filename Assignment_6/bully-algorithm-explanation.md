# BullyAlgorithm.java - Line by Line Explanation

```java
// BullyAlgorithm.java
import java.util.*;
```
- Imports all classes from the `java.util` package, which includes collections (ArrayList, List) and utility classes (Scanner, Collections).

```java
public class BullyAlgorithm {
    private List<Integer> processes;
    private Integer coordinator;
```
- Defines a class named `BullyAlgorithm` with two private instance variables:
  - `processes`: A List of Integer objects representing the IDs of active processes
  - `coordinator`: An Integer representing the ID of the current coordinator process

```java
    public BullyAlgorithm(List<Integer> processes) {
        this.processes = new ArrayList<>(processes);
        this.coordinator = Collections.max(processes);
    }
```
- Constructor that initializes the class with a list of process IDs
- Creates a new ArrayList containing all elements from the input list (defensive copying)
- Sets the coordinator to the process with the highest ID using `Collections.max()`

```java
    // Display current coordinator
    public void displayCoordinator() {
        if (coordinator != null)
            System.out.println("Current Coordinator: Process " + coordinator);
        else
            System.out.println("No coordinator currently.");
    }
```
- Method to display the current coordinator
- Checks if a coordinator exists (not null) and prints appropriate message

```java
    // Simulate coordinator failure
    public void failCoordinator() {
        System.out.println("\nProcess " + coordinator + " (Coordinator) has failed.");
        processes.remove(coordinator);
        coordinator = null;
    }
```
- Method to simulate the failure of the current coordinator
- Prints a message indicating coordinator failure
- Removes the coordinator from the list of active processes
- Sets the coordinator reference to null

```java
    // Election logic
    public void startElection(int initiator) {
        System.out.println("\nProcess " + initiator + " starts election.");
        boolean higherExists = false;
```
- Method to start an election, taking the initiator process ID as parameter
- Prints message indicating which process started the election
- Initializes a boolean flag to track if any higher-ID process exists

```java
        for (int process : processes) {
            if (process > initiator) {
                System.out.println("Election message sent from Process " + initiator + " to Process " + process);
                System.out.println("Process " + process + " responds to Process " + initiator);
                higherExists = true;
            }
        }
```
- Loops through all active processes
- For each process with an ID higher than the initiator:
  - Prints message indicating election message sent from initiator to higher process
  - Prints message indicating higher process responds to initiator
  - Sets the `higherExists` flag to true

```java
        if (!higherExists) {
            coordinator = initiator;
            System.out.println("Process " + initiator + " becomes the new coordinator.");
        } else {
            coordinator = Collections.max(processes);
            System.out.println("Process " + coordinator + " becomes the new coordinator.");
        }
    }
```
- If no higher-ID process exists:
  - Sets the initiator as the new coordinator
  - Prints message indicating the initiator becomes coordinator
- If higher-ID process(es) exist:
  - Sets the highest-ID process as the new coordinator using `Collections.max()`
  - Prints message indicating which process becomes coordinator

```java
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter process IDs (space-separated): ");
        String[] input = scanner.nextLine().split(" ");
        List<Integer> processes = new ArrayList<>();
        for (String s : input) processes.add(Integer.parseInt(s));
```
- Main method as entry point for the program
- Creates a Scanner to read user input
- Prompts user to enter process IDs as space-separated values
- Splits input string into an array of strings
- Creates an ArrayList of Integers, parsing each input string to an Integer

```java
        BullyAlgorithm bully = new BullyAlgorithm(processes);
        bully.displayCoordinator();
```
- Creates an instance of BullyAlgorithm with the list of process IDs
- Calls displayCoordinator() to show the initial coordinator

```java
        bully.failCoordinator();
```
- Simulates the failure of the current coordinator

```java
        System.out.print("Enter the ID of the initiator process: ");
        int initiator = scanner.nextInt();
        if (!bully.processes.contains(initiator)) {
            System.out.println("Invalid initiator process.");
            return;
        }
```
- Prompts user to enter the ID of the process that will initiate the election
- Reads the initiator ID
- Validates that the initiator process exists in the list of active processes
- If invalid, prints error message and exits

```java
        bully.startElection(initiator);
    }
}
```
- Starts the election with the specified initiator
- Closes the class definition