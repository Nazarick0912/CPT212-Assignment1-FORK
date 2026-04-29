import java.math.BigInteger;
import java.util.Random;

class Karatsuba {

    // Primitive operation counter
    public static long ops = 0;

    // Main driver method 
    public static BigInteger mult(BigInteger x, BigInteger y) {
        ops++; // Method call overhead
        
        ops += 3; // 2 comparisons and 1 logical OR
        // Checking if either input is a single digit to safely terminate recursion
        if (x.compareTo(BigInteger.TEN) < 0 || y.compareTo(BigInteger.TEN) < 0) {
            ops++; // Multiplication
            return x.multiply(y);
        }
      
        ops += 2; // 2 length calculations
        int noOneLength = x.toString().length();
        int noTwoLength = y.toString().length();
 
        ops += 2; // Math.max and assignment
        int maxNumLength = Math.max(noOneLength, noTwoLength);
 
        ops += 4; // division, modulo, addition, assignment
        int halfMaxNumLength = (maxNumLength / 2) + (maxNumLength % 2);
 
        ops += 2; // pow and assignment
        BigInteger maxNumLengthTen = BigInteger.TEN.pow(halfMaxNumLength);
 
        ops += 8; // 2 divides, 2 remainders, 4 assignments
        BigInteger a = x.divide(maxNumLengthTen);
        BigInteger b = x.remainder(maxNumLengthTen);
        BigInteger c = y.divide(maxNumLengthTen);
        BigInteger d = y.remainder(maxNumLengthTen);
 
        ops += 3; // 3 recursive calls (the recursive tree adds its own ops inside)
        BigInteger z0 = mult(a, c);
        
        ops += 4; // 2 additions, 2 assignments
        BigInteger aPlusB = a.add(b);
        BigInteger cPlusD = c.add(d);
        
        BigInteger z1 = mult(aPlusB, cPlusD);
        BigInteger z2 = mult(b, d);
 
        ops += 3; // 2 subtractions, 1 assignment (Gauss's trick)
        BigInteger middleTerm = z1.subtract(z0).subtract(z2);

        ops += 4; // 2 pow, 2 assignments
        BigInteger pow1 = BigInteger.TEN.pow(halfMaxNumLength * 2);
        BigInteger pow2 = maxNumLengthTen; // Reusing 10^halfMaxNumLength
 
        ops += 5; // 2 multiplies, 2 adds, 1 assignment
        BigInteger ans = z0.multiply(pow1).add(middleTerm.multiply(pow2)).add(z2);
 
        ops++; // return
        return ans;
    }
   
    // Helper to generate an n-digit random BigInteger
    public static BigInteger generateRandomBigInt(int digits, Random r) {
        StringBuilder sb = new StringBuilder(digits);
        sb.append(r.nextInt(9) + 1); // Ensure first digit is non-zero
        for(int i = 1; i < digits; i++) {
            sb.append(r.nextInt(10));
        }
        return new BigInteger(sb.toString());
    }

    // Main driver function
    public static void main(String[] args)
    {
        // Showcasing karatsuba multiplication
         
      // Case 1: Big integer lengths
        BigInteger expectedProduct = new BigInteger("1234").multiply(new BigInteger("5678"));
        ops = 0;
        BigInteger actualProduct = mult(new BigInteger("1234"), new BigInteger("5678"));
 
        // Printing the expected and corresponding actual product 
        System.out.println("Expected 1 : " + expectedProduct);
        System.out.println("Actual 1 : " + actualProduct + " | Ops: " + ops + "\n\n");
       
        assert(expectedProduct.equals(actualProduct));
 
 
        expectedProduct = new BigInteger("102").multiply(new BigInteger("313"));
        ops = 0;
        actualProduct = mult(new BigInteger("102"), new BigInteger("313"));
 
        System.out.println("Expected 2 : " + expectedProduct);
        System.out.println("Actual 2 : " + actualProduct + " | Ops: " + ops + "\n\n");
         
      assert(expectedProduct.equals(actualProduct));
 
        expectedProduct = new BigInteger("1345").multiply(new BigInteger("63456"));
        ops = 0;
        actualProduct = mult(new BigInteger("1345"), new BigInteger("63456"));
 
        System.out.println("Expected 3 : " + expectedProduct);
        System.out.println("Actual 3 : " + actualProduct + " | Ops: " + ops + "\n\n");
         
      assert(expectedProduct.equals(actualProduct));        
     
        int MAX_VALUE = 10000; // Using 10000 for the assertion trap logic
        Random r = new Random();
 
        // We will run 10 iterations going up to N=10000 digits.
        // Running 10000 loops of 10000 digits would take too long, so we adapt the loop
        // to iterate exactly MAX_VALUE times but use small numbers, except when demonstrating larger digits.
        int[] digitTests = {10, 100, 500, 1000, 5000, 10000};
        System.out.println("--- Testing larger digits up to N=10000 ---");
        for (int i = 0; i < digitTests.length; i++) {
            int n = digitTests[i];
            BigInteger x = generateRandomBigInt(n, r);
            BigInteger y = generateRandomBigInt(n, r);

            expectedProduct = x.multiply(y);

            ops = 0; // Reset operations counter
            actualProduct = mult(x, y);

            System.out.println("Testing N = " + n + " digits");
            // Only display numbers for smaller N to avoid console flooding, but show ops for all
            if (n <= 100) {
                System.out.println("x = " + x);
                System.out.println("y = " + y);
            } else {
                System.out.println("x and y are too large to print (" + n + " digits)");
            }
            System.out.println("Operations count (ops): " + ops);
            System.out.println("Match: " + expectedProduct.equals(actualProduct) + "\n");
        }

        System.out.println("--- Running 10000 Iteration Loop for Assertion Catch ---");
        for (int i = 0; i < MAX_VALUE; i++) {
            // Generating small numbers for the brute force loop to complete quickly
            BigInteger x = new BigInteger(20, r);
            BigInteger y = new BigInteger(20, r);
 
            expectedProduct = x.multiply(y);
 
            if (i == 9999) {
              // Prove assertions catch the bad stuff on the final loop!
                expectedProduct = BigInteger.ONE;    
            }
            
            ops = 0;
            actualProduct = mult(x, y);
 
            if (i == 9999) {
                // Display the numbers and operations count
                System.out.println("Iteration: " + i);
                System.out.println("x = " + x);
                System.out.println("y = " + y);
                System.out.println("Operations count: " + ops);
                System.out.println("Expected: " + expectedProduct);
                System.out.println("Actual: " + actualProduct + "\n");
            }
 
            try {
                assert(expectedProduct.equals(actualProduct));        
            } catch (AssertionError e) {
                if (i == 9999) {
                    System.out.println("Assertion error successfully caught as intended on the last iteration!");
                } else {
                    throw e;
                }
            }
        }
    }
}