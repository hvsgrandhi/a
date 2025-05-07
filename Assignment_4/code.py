# Step 1: Initialize nodes with current clock times in minutes (24-hour format for simplicity)
nodes = {
    'N1': 14 * 60 + 0,   # 14:00 - Master node
    'N2': 13 * 60 + 46,  # 13:46
    'N3': 14 * 60 + 20   # 14:20
}

# Step 2: Choose master node
master_node = 'N1'

# Step 3: Master requests time from all nodes (simulated)
def collect_times(nodes):
    return list(nodes.values())

# Step 4: Calculate average time
def calculate_average(times):
    return sum(times) // len(times)

# Step 5: Calculate correction and send to nodes
def calculate_corrections(nodes, avg_time):
    corrections = {}
    for node, time in nodes.items():
        corrections[node] = avg_time - time
    return corrections

# Step 6: Apply corrections
def synchronize_clocks(nodes, corrections):
    new_times = {}
    for node, time in nodes.items():
        new_times[node] = time + corrections[node]
    return new_times

# Run the algorithm
collected_times = collect_times(nodes)
average_time = calculate_average(collected_times)
corrections = calculate_corrections(nodes, average_time)
synchronized_nodes = synchronize_clocks(nodes, corrections)

# Helper to convert minutes to HH:MM format
def minutes_to_time(minutes):
    return f"{minutes // 60:02d}:{minutes % 60:02d}"

# Output the results
print("Original Clock Times:")
for node, time in nodes.items():
    print(f"{node}: {minutes_to_time(time)}")

print("\nCorrections:")
for node, corr in corrections.items():
    sign = '+' if corr >= 0 else ''
    print(f"{node}: {sign}{corr} minutes")

print("\nSynchronized Clock Times:")
for node, time in synchronized_nodes.items():
    print(f"{node}: {minutes_to_time(time)}")
