#!/usr/bin/env python3
import config
import copy
import math
import matplotlib.pyplot as plt

lock_type = [

    "Tournament",
    "TASLock",
    "TTASLock"
]
threads = [1, 2, 4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48]

mapping = {

    "Tournament": [],
    "TASLock": [],
    "TTASLock": []

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
    threads, mapping[lock_type[0]], label=f"Peterson's Tournament Tree"
)  # Label for dataset 1

plt.plot(
    threads, mapping[lock_type[1]], label=f"TAS Lock"
)  # Label for dataset 5
plt.plot(
    threads, mapping[lock_type[2]], label=f"TTAS Lock"
)  # Label for dataset 6

# Add labels to the x-axis and y-axis
plt.xlabel("# of Threads")
plt.ylabel("Time in (ms)")

# Add a title to the plot
plt.title("Lock Performances")

# Add a legend to the plot
plt.legend()

plt.savefig("plot_2_3mil.pdf")

# Show the plot
plt.show()
