import time
import threading

class Process(threading.Thread):
    def __init__(self, pid, total_processes, wants_to_enter):
        super().__init__()
        self.pid = pid
        self.total_processes = total_processes
        self.wants_to_enter = wants_to_enter
        self.has_token = False
        self.next_process = None

    def set_next_process(self, next_process):
        self.next_process = next_process

    def run(self):
        while True:
            if self.has_token:
                print(f"Process {self.pid} has the token.")
                if self.wants_to_enter:
                    print(f"Process {self.pid} is entering the critical section.")
                    time.sleep(1)  # Simulate critical section
                    print(f"Process {self.pid} is leaving the critical section.")
                    self.wants_to_enter = False  # Done with critical section
                else:
                    print(f"Process {self.pid} does not want to enter critical section.")
                # Pass token to the next process
                self.has_token = False
                self.next_process.has_token = True
                break
            time.sleep(0.1)  # Wait and check again

def simulate_token_ring(process_count, request_list):
    # Create processes
    processes = [Process(i, process_count, request_list[i]) for i in range(process_count)]

    # Create the ring
    for i in range(process_count):
        processes[i].set_next_process(processes[(i + 1) % process_count])

    # Give the token to the first process
    processes[0].has_token = True

    # Start all processes
    for p in processes:
        p.start()

    # Wait for all to finish one round
    for p in processes:
        p.join()

# Example Simulation
if __name__ == "__main__":
    n = 5  # Total number of processes
    request_list = [False, True, False, True, False]  # Which processes want to enter critical section
    simulate_token_ring(n, request_list)
