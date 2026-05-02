import csv
import matplotlib.pyplot as plt
from matplotlib.ticker import LogFormatterMathtext

n_digits = []
simple_ops = []
karatsuba_ops = []

# Read data from the CSV
with open('ExperimentResults.csv', 'r') as file:
    reader = csv.DictReader(file)
    for row in reader:
        n_digits.append(int(row['N_digits']))
        # Extract raw integers
        simple_ops.append(int(row['SimpleOps']))
        karatsuba_ops.append(int(row['KaratsubaOps']))

# Create the plot
plt.figure(figsize=(10, 6))
plt.plot(n_digits, simple_ops, linestyle='-', color='#e74c3c', linewidth=2.5, label='Simple Multiplication O(N²)')
plt.plot(n_digits, karatsuba_ops, linestyle='-', color='#3498db', linewidth=2.5, label='Karatsuba Algorithm O(N^1.585)')

# --- SET SCALE TO LOGARITHMIC ---
plt.yscale('log')

# --- FORCE EXPONENT (10^x) FORMATTING ON Y-AXIS ---
plt.gca().yaxis.set_major_formatter(LogFormatterMathtext())

# Formatting the graph
plt.title('Algorithm Time Complexity Comparison (Log Scale)', fontsize=18, fontweight='bold', pad=15)
plt.xlabel('Number of Digits (N)', fontsize=14, fontweight='bold')
plt.ylabel('Operations Count (Exponents)', fontsize=14, fontweight='bold')

# Force X-axis to start at 0
plt.xlim(left=0)

# Configure grid to show both major and minor log lines
plt.grid(True, which="both", linestyle='--', alpha=0.6)
plt.legend(fontsize=12)

# Adjust layout and save
plt.tight_layout()
plt.savefig('Complexity_Graph_Log_Exponent.png')
plt.show()