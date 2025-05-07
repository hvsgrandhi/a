class BullyAlgorithm:
    def __init__(self, processes):
        self.processes = processes
        self.coordinator = max(processes)

    def start_election(self, initiator):
        print(f"\nProcess {initiator} starts an election.")
        higher = [p for p in self.processes if p > initiator]

        if not higher:
            self.coordinator = initiator
            print(f"Process {initiator} becomes the new coordinator.")
            return

        for p in higher:
            print(f"Election message sent from Process {initiator} to Process {p}")
        print(f"Processes {higher} respond to Process {initiator}")

        highest = max(higher)
        self.coordinator = highest
        print(f"Process {highest} becomes the new coordinator.")

    def fail_coordinator(self):
        print(f"\nCoordinator Process {self.coordinator} has failed.")
        self.processes.remove(self.coordinator)
        self.coordinator = None


# Example usage
processes = [1, 2, 3, 4, 5]
bully = BullyAlgorithm(processes)
print(f"Initial Coordinator: Process {bully.coordinator}")

bully.fail_coordinator()
bully.start_election(2)
