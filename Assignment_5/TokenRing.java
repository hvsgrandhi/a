import java.util.Scanner;

class Process extends Thread {
    private int pid;
    private boolean wantsToEnter;
    private boolean hasToken = false;
    private Process nextProcess;

    public Process(int pid, boolean wantsToEnter) {
        this.pid = pid;
        this.wantsToEnter = wantsToEnter;
    }

    public void setNextProcess(Process nextProcess) {
        this.nextProcess = nextProcess;
    }

    public void receiveToken() {
        hasToken = true;
    }

    @Override
    public void run() {
        while (true) {
            if (hasToken) {
                System.out.println("Process " + pid + " has the token.");

                if (wantsToEnter) {
                    System.out.println("Process " + pid + " is entering the critical section...");
                    try {
                        Thread.sleep(1000); // simulate CS
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Process " + pid + " is leaving the critical section.");
                    wantsToEnter = false; // Done
                } else {
                    System.out.println("Process " + pid + " does not want to enter the critical section.");
                }

                hasToken = false;
                nextProcess.receiveToken();
                break;
            }

            try {
                Thread.sleep(100); // Check again later
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class TokenRing {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Get number of processes
        System.out.print("Enter number of processes: ");
        int n = scanner.nextInt();

        // 2. Get request list
        boolean[] requestList = new boolean[n];
        for (int i = 0; i < n; i++) {
            System.out.print("Should Process " + i + " enter critical section? (true/false): ");
            requestList[i] = scanner.nextBoolean();
        }

        // 3. Create processes
        Process[] processes = new Process[n];
        for (int i = 0; i < n; i++) {
            processes[i] = new Process(i, requestList[i]);
        }

        // 4. Link them in a ring
        for (int i = 0; i < n; i++) {
            processes[i].setNextProcess(processes[(i + 1) % n]);
        }

        // 5. Give token to first process
        processes[0].receiveToken();

        // 6. Start all processes
        for (int i = 0; i < n; i++) {
            processes[i].start();
        }

        // 7. Wait for all to finish one round
        for (int i = 0; i < n; i++) {
            try {
                processes[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        scanner.close();
        System.out.println("All processes finished one round of token passing.");
    }
}


// javac TokenRing.java
// java TokenRing
