import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Random;

public class DatasetGenerator {

    private static final Random random = new Random();

    public static void main(String[] args) {
        
   
        String csvFile = "Dataset.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
            // Write CSV Header
            writer.println("n_digits,multiplicand,multiplier,product");

            for (int n = 1  ; n <= 1001 ; n++) {//Initialise loop to generate dataset from 1 to 1001
                
                // Generate two random n-digit numbers
                BigInteger num1 = new BigInteger(generateRandomNumberString(n));
                BigInteger num2 = new BigInteger(generateRandomNumberString(n));
                BigInteger num3 = num1.multiply(num2);

                // Write the result to CSV
                writer.println(n + "," + num1 + "," + num2 + "," + num3);
            }

        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
    /*
     * Generates a random number string of exactly n digits.
  
     */
    public static String generateRandomNumberString(int n) {
        if (n <= 0) return "0";
        //Ensure no n of 0 will be generated or negative values to be passed through 

        if (n == 1) return String.valueOf(random.nextInt(10));

        StringBuilder sb = new StringBuilder(n);
        // First digit: 1 to 9 (no leading zeros)
        sb.append(random.nextInt(9) + 1);
        
        // Remaining digits: 0 to 9
        for (int i = 1; i < n; i++) {
            sb.append(random.nextInt(10));
        }
        
        return sb.toString();
    }
}
