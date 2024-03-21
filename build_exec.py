#!/usr/bin/env python3
import config
import os
import subprocess

source_directory = config.source_directory
target_directory = config.target_directory


def compile_java_project():
    compile_command = f"javac -d {target_directory} {source_directory}/*.java"
    os.system(compile_command)
    print("Compilation completed!")


def run_project():
    print("Executing locks...")
    compile_command = f'java -cp "{target_directory}" Main'
    os.system(compile_command)


def collect_data():
    lock_type = [
        "TASLock",
        "TTASLock",
        "Tournament",
        "FilterV1",
        "FilterV2",
        "BakeryLockV1",
        "BakeryLockV2",
    ]
    threads = [1, 2, 4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48]

    for lock in lock_type:
        for t in threads:
            to_append = "false"
            # average 10 runs
            for i in range(10):
                java_command = [
                    "java",
                    "-cp",
                    f"{target_directory}",
                    "Main",
                    lock,
                    f"{t}",
                    to_append,
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

                # Print the output of the Java program
                print("Output:\n", stdout.decode("utf-8"))

                # Check if there was any error
                if stderr:
                    print("Error:", stderr.decode("utf-8"))

                # Check the return code of the process
                if process.returncode == 0:
                    print("Java program executed successfully.")
                else:
                    print("Java program failed to execute.")


if __name__ == "__main__":
    compile_java_project()
    # run_project()
    collect_data()
