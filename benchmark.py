#!/usr/bin/env python3
import config
import copy
import math
import matplotlib.pyplot as plt

lock_type = []
mapping = {}

for l in config.test_lock:
    if config.test_lock[l]:
        lock_type.append(l)
        mapping[l] = []

threads = config.thread_counts

for lock in lock_type:
    avg_list = []
    for t in threads:
        with open(f"{config.data_directory}/{lock}/{lock}-{t}.txt", "r") as file:
            # Read all lines from the file and store them in a list
            lines = [int(line.strip()) for line in file.readlines()]
            # making sure newline char isn't part of list
            if len(lines) > 10:
                lines = lines[0:10]
            average = sum(lines) / len(lines)
            avg_list.append(average)
    mapping[lock] = copy.deepcopy(avg_list)

for l in lock_type:
    plt.plot(
        threads, mapping[l], label=f"{l}"
    ) 

# Add labels to the x-axis and y-axis
plt.xlabel("# of Threads")
plt.ylabel("Time in (ms)")

# Add a title to the plot
plt.title("Queue Lock Performances")

# Add a legend to the plot
plt.legend()

plt.savefig("plots/queue-locks.pdf")

# Show the plot
plt.show()
