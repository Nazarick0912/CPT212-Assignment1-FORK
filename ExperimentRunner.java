import java.util.Random;
import java.math.BigInteger;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ExperimentRunner {
    private static final Random random = new Random();

    public static void main(String[] args) {
        
        // Setup N values to test
        int[] N_values = new int[41];
        N_values[0] = 10;
        for (int i = 1; i <= 40; i++) {
            N_values[i] = i * 250;
        }
        
        System.out.println("\nRunning Experiments...");
        try (PrintWriter writer = new PrintWriter(new FileWriter("ExperimentResults.csv"))) {
            // Updated CSV Header to include Karatsuba
            writer.println("N_digits,SimpleOps,KaratsubaOps"); 
            
            for (int N : N_values) {
                // Generate random numbers
                int[] A = generateRandomArray(N);
                int[] B = generateRandomArray(N);
                BigInteger bigA = new BigInteger(toString(A));
                BigInteger bigB = new BigInteger(toString(B));
                
                // 1. Run Simple Multiplication
                SimpleMultiplication.ops = 0;
                SimpleMultiplication.multiply(bigA, bigB, false);
                long opsSimple = SimpleMultiplication.ops;
                
                // 2. Run Karatsuba Algorithm
                Karatsuba.ops = 0;
                Karatsuba.mult(bigA, bigB);
                long opsKarat = Karatsuba.ops;
                
                // Write both to CSV
                writer.println(N + "," + opsSimple + "," + opsKarat);
                System.out.printf("N = %5d | Simple: %12d | Karatsuba: %12d%n", N, opsSimple, opsKarat);
            }
            System.out.println("Results successfully written to ExperimentResults.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] generateRandomArray(int n) {
        int[] arr = new int[n];
        arr[0] = random.nextInt(9) + 1; // 1-9 to avoid leading zero
        for (int i = 1; i < n; i++) {
            arr[i] = random.nextInt(10);
        }
        return arr;
    }

    private static String toString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = arr.length - 1; i >= 0; i--) {
            sb.append(arr[i]);
        }
        return sb.toString();
    }
}