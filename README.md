# CPT212-Assignment1
This repository aims to explore the theoretical analysis of different algorithms performing large-number multiplication, specifically comparing a Simple $O(N^2)$ Multiplication algorithm against the Karatsuba algorithm.

## File Overview

### Core Algorithm Files
* **`SimpleMultiplication.java`**: Implements the standard $O(N^2)$ simple multiplication algorithm. It includes a static `ops` counter to accurately track every primitive operation (assignments, arithmetic, array lookups) during execution. It also handles printing partial products and carriers for demonstration purposes.
* **`Karatsuba.java`**: Contains the provided Karatsuba multiplication algorithm. It is instrumented with an `ops` counter to measure its time complexity. It uses recursive calls and handles very large `BigInteger` values.

### Experiment & Data Generation Files
* **`ExperimentRunner.java`**: The main testing orchestrator. It runs correctness tests and then iterates through increasing digit sizes ($N=10$ up to $10000$). It measures the operation counts and writes the results to a CSV file for analysis.
* **`DatasetGenerator.java`**: A utility script that generates a dataset of random numbers of varying lengths and calculates their exact product using Java's native libraries, saving them to `Dataset.csv`.

### Visualization & Output Files
* **`plot_results.py`**: A Python script using `matplotlib` to read the experimental data and generate a time complexity graph showing Operations vs. Number of Digits.
* **`ExperimentResults.csv`**: The data output file created by `ExperimentRunner.java` containing the raw operation counts.
* **`Dataset.csv`**: The output file created by `DatasetGenerator.java` containing test numbers and their exact expected answers.
* **`SimpleMultiplication_Plot_Python.png`**: The generated visual output from the Python script, used for the assignment report.
