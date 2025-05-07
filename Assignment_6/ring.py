class RingAlgorithm:
    def __init__(self, processes):
        self.processes = sorted(processes)
        self.ring = self.processes.copy()
        self.coordinator = max(self.processes)

    def start_election(self, initiator):
        print(f"\nProcess {initiator} starts an election.")
        idx = self.ring.index(initiator)
        active_list = [initiator]
        current = (idx + 1) % len(self.ring)

        while self.ring[current] != initiator:
            print(f"Election message sent from Process {self.ring[(current - 1) % len(self.ring)]} to Process {self.ring[current]}")
            active_list.append(self.ring[current])
            current = (current + 1) % len(self.ring)

        new_coordinator = max(active_list)
        self.coordinator = new_coordinator
        print(f"Active List: {active_list}")
        print(f"Process {new_coordinator} becomes the new coordinator.")

    def fail_coordinator(self):
        print(f"\nCoordinator Process {self.coordinator} has failed.")
        self.ring.remove(self.coordinator)
        self.coordinator = None


# Example usage
processes = [1, 2, 3, 4, 5]
ring = RingAlgorithm(processes)
print(f"Initial Coordinator: Process {ring.coordinator}")

ring.fail_coordinator()
ring.start_election(2)
