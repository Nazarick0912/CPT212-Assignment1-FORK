import csv
import matplotlib.pyplot as plt

n_digits = []
simple_ops = []

# Read data from the existing ExperimentResults.csv
with open('ExperimentResults.csv', 'r') as file:
    reader = csv.DictReader(file)
    for row in reader:
        n_digits.append(int(row['N_digits']))
        # We divide by 1 billion to scale the y-axis down for readability
        simple_ops.append(int(row['SimpleOps']) / 1_000_000_000)

# Create the plot
plt.figure(figsize=(10, 6))
plt.plot(n_digits, simple_ops, linestyle='-', color='#e74c3c', linewidth=2.5, label='Simple Multiplication O(N²)')

# Formatting the graph
plt.title('Simple Multiplication Time Complexity', fontsize=18, fontweight='bold', pad=15)
plt.xlabel('Number of Digits (N)', fontsize=14, fontweight='bold')
plt.ylabel('Operations Count (in Billions)', fontsize=14, fontweight='bold')

# Force axes to start exactly at the (0,0) origin
plt.xlim(left=0)
plt.ylim(bottom=0)

# Configure grid and legend
plt.grid(True, linestyle='--', alpha=0.6)
plt.legend(fontsize=12)

# Adjust layout to prevent cutting off labels
plt.tight_layout()

# Save the plot as a PNG file
output_filename = 'SimpleMultiplication_Plot_Python.png'
plt.savefig(output_filename, dpi=300)
print(f"Successfully generated '{output_filename}' using Python matplotlib!")
