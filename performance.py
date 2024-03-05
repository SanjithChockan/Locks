#!/usr/bin/env python3
import config
import copy
import math
import matplotlib.pyplot as plt

# compare efficiency of mutual exlcusion locks implemented in the project


lock_type = ["Tournament", "FilterV1", "FilterV2", "BakeryLockV1", "BakeryLockV2"]
threads = [1, 2, 3, 4, 5, 6, 7, 8]

mapping = {
    "Tournament": [],
    "FilterV1": [],
    "FilterV2": [],
    "BakeryLockV1": [],
    "BakeryLockV2": [],
}

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


# Plot the data and set labels
plt.plot(
    threads, mapping[lock_type[0]], label=f"Peterson's {lock_type[0]}"
)  # Label for dataset 1
plt.plot(
    threads, mapping[lock_type[1]], label=f"Filter Lock: Textbook"
)  # Label for dataset 2
plt.plot(
    threads, mapping[lock_type[2]], label=f"Filter Lock: Blackbok"
)  # Label for dataset 3
plt.plot(
    threads, mapping[lock_type[3]], label=f"Bakery Lock: Textbook"
)  # Label for dataset 3
plt.plot(
    threads, mapping[lock_type[4]], label=f"Bakery Lock: Research Paper"
)  # Label for dataset 4

# Add labels to the x-axis and y-axis
plt.xlabel("# of Threads")
plt.ylabel("Time in (ms)")

# Add a title to the plot
plt.title("Lock Performances")

# Add a legend to the plot
plt.legend()

plt.savefig("plot.pdf")

# Show the plot
plt.show()
