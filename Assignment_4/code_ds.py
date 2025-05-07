import time
import random
import threading

# Global state
node_times = {}             # Stores current time of each node
node_locks = {}             # Lock per node for thread safety
running = True              # Controls thread execution

# Initialize nodes
NUM_NODES = 6
DRIFT_RANGE = 5  # seconds drift max

# Simulate real time drifted clocks
def node_thread(node_id, base_time):
    global node_times, running
    drift = random.uniform(-DRIFT_RANGE, DRIFT_RANGE)  # Random initial drift
    while running:
        with node_locks[node_id]:
            node_times[node_id] += 1 + drift  # Add 1 second per second + drift
        time.sleep(1)

# Display thread for live status
def display_thread():
    while running:
        print("\n=== Live Clock Status ===")
        for node_id in sorted(node_times.keys()):
            with node_locks[node_id]:
                current = node_times[node_id]
                print(f"{node_id}: {time.strftime('%H:%M:%S', time.gmtime(current))}")
        time.sleep(3)

# Master runs Berkeley algorithm
def berkeley_sync(master_id):
    print("\n[Master] Initiating Berkeley Synchronization...")
    times_snapshot = {}
    
    # Collect current times
    for node_id in node_times:
        with node_locks[node_id]:
            times_snapshot[node_id] = node_times[node_id]

    # Calculate average
    average_time = sum(times_snapshot.values()) / len(times_snapshot)
    
    # Calculate corrections
    corrections = {}
    for node_id, t in times_snapshot.items():
        corrections[node_id] = average_time - t
    
    # Apply corrections
    for node_id in corrections:
        with node_locks[node_id]:
            node_times[node_id] += corrections[node_id]
    
    print("[Master] Synchronization complete.\n")
    time.sleep(2)

# Main simulation logic
def main():
    global running
    base_time = time.time()

    # Initialize node times and locks
    for i in range(1, NUM_NODES + 1):
        node_id = f"N{i}"
        node_times[node_id] = base_time + random.uniform(-DRIFT_RANGE, DRIFT_RANGE)
        node_locks[node_id] = threading.Lock()

    # Start threads for each node
    threads = []
    for node_id in node_times:
        t = threading.Thread(target=node_thread, args=(node_id, base_time))
        t.start()
        threads.append(t)

    # Start display thread
    display = threading.Thread(target=display_thread)
    display.start()

    # Run synchronization periodically (every 20 seconds)
    try:
        while True:
            time.sleep(20)
            berkeley_sync(master_id="N1")
    except KeyboardInterrupt:
        print("\nStopping simulation...")
        running = False
        for t in threads:
            t.join()
        display.join()

if __name__ == "__main__":
    main()
