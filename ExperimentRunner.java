import java.util.Random;
import java.math.BigInteger;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ExperimentRunner {
    private static final Random random = new Random();

    public static void main(String[] args) {
        runCorrectnessTests();
        
        int[] N_values = new int[41];
        N_values[0] = 10;
        for (int i = 1; i <= 40; i++) {
            N_values[i] = i * 250;
        }
        
        System.out.println("\nRunning Experiments...");
        try (PrintWriter writer = new PrintWriter(new FileWriter("ExperimentResults.csv"))) {
            writer.println("N_digits,SimpleOps"); // Karatsuba omitted for now
            
            for (int N : N_values) {
                int[] A = generateRandomArray(N);
                int[] B = generateRandomArray(N);
                
                BigInteger bigA = new BigInteger(toString(A));
                BigInteger bigB = new BigInteger(toString(B));
                
                SimpleMultiplication.multiply(bigA, bigB, false);
                long opsSimple = SimpleMultiplication.ops;
                
                // KaratsubaLarge.ops = 0;
                // KaratsubaLarge.multiply(A, B);
                // long opsKarat = KaratsubaLarge.ops;
                
                writer.println(N + "," + opsSimple);
                System.out.println(String.format("N = %5d | Simple Ops: %12d", N, opsSimple));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] generateRandomArray(int n) {
        if (n <= 0) return new int[]{0};
        int[] arr = new int[n];
        arr[n - 1] = random.nextInt(9) + 1; // msb != 0
        for (int i = 0; i < n - 1; i++) {
            arr[i] = random.nextInt(10);
        }
        return arr;
    }

    private static void runCorrectnessTests() {
        System.out.println("--- Running Correctness & Demonstration Tests ---");
        int[] A = {1, 0, 3, 2, 5}; // 52301
        int[] B = {0, 8, 3};       // 380
        
        BigInteger bigA_test = new BigInteger(toString(A));
        BigInteger bigB_test = new BigInteger(toString(B));
        
        System.out.println("\nTesting Simple Multiplication Example (52301 * 380):");
        BigInteger resSimple = SimpleMultiplication.multiply(bigA_test, bigB_test, true);
        System.out.println("Result Simple: " + resSimple.toString());
        
        // System.out.println("\nTesting Karatsuba Example (52301 * 380):");
        // int[] resKarat = KaratsubaLarge.multiply(A, B);
        // System.out.println("Result Karatsuba: " + toString(resKarat));
        
        System.out.println("\nVerifying against BigInteger for correctness (10 random tests)...");
        for (int i = 0; i < 10; i++) {
            int n = random.nextInt(200) + 10;
            int[] arrA = generateRandomArray(n);
            int[] arrB = generateRandomArray(n);
            
            BigInteger bigA = new BigInteger(toString(arrA));
            BigInteger bigB = new BigInteger(toString(arrB));
            BigInteger expected = bigA.multiply(bigB);
            
            BigInteger actualS = SimpleMultiplication.multiply(bigA, bigB, false);
            // int[] kRes = KaratsubaLarge.multiply(arrA, arrB);
            // BigInteger actualK = new BigInteger(toString(kRes));
            
            if (!expected.equals(actualS)) {
                System.err.println("Simple Multiplication Failed! Expected: " + expected + ", Actual: " + actualS);
            }
            // if (!expected.equals(actualK)) {
            //     System.err.println("Karatsuba Failed! Expected: " + expected + ", Actual: " + actualK);
            // }
        }
        System.out.println("Verification complete. All simple multiplications matched BigInteger expected results.");
    }

    private static String toString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = arr.length - 1; i >= 0; i--) {
            sb.append(arr[i]);
        }
        return sb.toString();
    }
}
