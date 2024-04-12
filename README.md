# Spin Locks for Multiprocessor systems
Implementation of various spin locks to ensure mutual exlusion among threads.

Benchmarked performance on Intel® Xeon(R) Gold 5220R CPU @ 2.20GHz × 48 (24 Cores with Intel Hyper-Threading)


# Implemented Locks
## Mutual Exlusion of N processes (N >= 2)
- Filter Lock (Generalized Peterson)
- Lamport's Bakery Algorithm
- Peterson's Tournament (Binary Tree Structure)
## Test and Set Locks
- TAS Lock
- TTAS Lock
## Queue Locks
- CLH Lock
- MCS Lock
- HemLock (Oracle)

# Benchmark
Measured scalability performance of locks using a simple Counter.
Each data point is averaged over 10 runs. 

![Plot](/plots/plot/plot-1.png)
![Plot](/plots/Tournament--and--TAS/Tournament--and--TAS-1.png)
![Plot](/plots/queue-locks/queue-locks-1.png)

# Implementation
- Java: openjdk version "11.0.22"
- Apache Ant for building the project
- Python scripts to automate data collection and performance analysis

# References and Papers

Lamport's bakery: https://dl.acm.org/doi/10.1145/361082.361093

Hemlock: https://arxiv.org/abs/2102.03863

Art of Multiprocessor Programming by Maurice Herlihy & Nir Shavit