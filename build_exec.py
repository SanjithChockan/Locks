#!/usr/bin/env python3
import config
import os
import subprocess


def collect_data():
    lock_type = []

    for l in config.test_lock:
        if config.test_lock[l]:
            lock_type.append(l)

    threads = config.thread_counts

    for lock in lock_type:
        for t in threads:
            to_append = "false"
            # average 10 runs
            for i in range(10):
                java_command = [
                    "ant",
                    'run',
                    f'-Darg1={lock}',
                    f'-Darg2={t}',
                    f'-Darg3={to_append}',
                ]
                to_append = "true"

                # Execute the Java program using subprocess
                process = subprocess.Popen(
                    java_command, stdout=subprocess.PIPE, stderr=subprocess.PIPE
                )
                # Wait for the process to finish and get the output
                stdout, stderr = process.communicate()
                # Wait for the process to finish
                process.wait()

                # Check if there was any error
                if stderr:
                    print("Error:", stderr.decode("utf-8"))

                # Check the return code of the process
                #print(stdout.decode("utf-8"))
                if process.returncode == 0:
                    print("Java program executed successfully.")
                else:
                    print("Java program failed to execute.")


if __name__ == "__main__":
    collect_data()
