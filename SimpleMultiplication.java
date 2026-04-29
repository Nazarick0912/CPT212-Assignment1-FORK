import java.math.BigInteger;

public class SimpleMultiplication {
    public static long ops = 0; // operation counter
    
    // Uses simple modulus and division on Bigintegers to maintain readability
    // while still being able to natively handle N = 10000 without array loops!
    public static BigInteger multiply(BigInteger A, BigInteger B, boolean printDetails) {
        ops = 0;
        
        String strA = A.toString();
        String strB = B.toString();
        ops += 4; // 2 method calls, 2 assign -> +1 constant (runs once)
        
        int width = strA.length() + strB.length() + 2;
        ops += 5; // 2 method calls, 2 arith, 1 assign -> +1 constant (runs once)
        
        if (printDetails) {
            System.out.printf("%" + width + "s\n", strA);
            System.out.printf("x%" + (width - 1) + "s\n", strB);
            System.out.println("-".repeat(width));
        }
        
        BigInteger result;
        
        int n = strA.length();
        int m = strB.length();
        int[] resultArr = new int[n + m + 1];
        ops += 8; // 2 method calls, 2 assign, 2 arith, 1 obj inst, 1 assign -> +1 constant (runs once)
        
        int jCount = 0; 
        ops += 1; // 1 assign -> +1 constant (runs once)
        
        // --- OUTER LOOP ---
        ops += 2; // int j = m - 1 (1 arith, 1 assign) -> +1 constant (runs once)
        for (int j = m - 1; j >= 0; j--) {
            // Loop condition check
            ops += 1; // j >= 0 (1 comp) -> runs m times (increases with element size)
            
            int digitB = strB.charAt(j) - '0';
            ops += 3; // 1 lookup, 1 arith, 1 assign -> runs m times
            
            StringBuilder partialStr = new StringBuilder();
            StringBuilder carryStr = new StringBuilder();
            ops += 4; // 2 obj inst, 2 assign -> runs m times
            
            // --- INNER LOOP ---
            ops += 2; // int i = n - 1 (1 arith, 1 assign) -> +1 constant (runs 1 time per j iteration)
            for (int i = n - 1; i >= 0; i--) {
                // Loop condition check
                ops += 1; // i >= 0 (1 comp) -> runs n times (increases with element size)
                
                int digitA = strA.charAt(i) - '0';
                ops += 3; // 1 lookup, 1 arith, 1 assign -> runs n times
                
                int prod = digitA * digitB;
                ops += 2; // 1 arith, 1 assign -> runs n times
                
                int partial = prod % 10;
                int carry = prod / 10;
                ops += 4; // 2 arith, 2 assign -> runs n times
                
                partialStr.append(partial);
                carryStr.append(carry);
                ops += 2; // 2 append method calls -> runs n times
                
                // Accumulation variables
                int posA = n - 1 - i;
                int posB = m - 1 - j;
                ops += 6; // posA(2 arith, 1 assign), posB(2 arith, 1 assign) -> runs n times
                
                resultArr[posA + posB] += partial;
                resultArr[posA + posB + 1] += carry;
                ops += 9; // line 1: 1 arith, 1 arr index, 1 arith(+), 1 assign(=). line 2: 2 arith, 1 arr index, 1 arith(+), 1 assign(=) -> runs n times
                
                // Loop update
                ops += 2; // i-- (1 arith, 1 assign) -> runs n times
            }
            // Inner loop exit failed-check
            ops += 1; // final i >= 0 loop exit check -> +1 constant (runs 1 time per j iteration)
            
            if (printDetails) {
                // partialStr appends from right to left, so reverse to read left to right
                String pStr = partialStr.reverse().toString() + " ".repeat(jCount);
                String cStr = carryStr.reverse().toString() + " ".repeat(jCount);
                
                String partialDesc = "partial products for (=" + strA + " x " + digitB + ")";
                String carryDesc = "carriers for (" + strA + " x " + digitB + ")";

                System.out.printf("%" + width + "s     %s\n", pStr, partialDesc);
                
                if (j == 0) { // For string loops, j==0 resolves the very last most significant numerical digit
                    String paddedCarry = String.format("%" + width + "s", cStr);
                    System.out.printf("+%s     %s\n", paddedCarry.substring(1), carryDesc);
                } else {
                    System.out.printf("%" + width + "s     %s\n", cStr, carryDesc);
                }
            }
            
            jCount++;
            ops += 2; // 1 arith, 1 assign -> runs m times
            
            // Loop update
            ops += 2; // j-- (1 arith, 1 assign) -> runs m times
        }
        // Outer loop exit failed-check
        ops += 1; // final j >= 0 loop exit check -> +1 constant (runs once)
        
        // --- NORMALIZATION LOOP ---
        ops += 1; // int i = 0 (1 assign) -> +1 constant (runs once)
        for (int i = 0; i < resultArr.length - 1; i++) {
            // Loop condition check
            ops += 3; // i < resultArr.length - 1 (1 arr lookup, 1 arith, 1 comp) -> runs (n+m) times
            
            resultArr[i + 1] += resultArr[i] / 10;
            resultArr[i] %= 10;
            ops += 9; // line 1: 1 arith, 1 arr index, 1 arr index, 1 arith, 1 arith(+), 1 assign(=). line 2: 1 arr index, 1 arith(%), 1 assign(=) -> runs (n+m) times
            
            // Loop update
            ops += 2; // i++ (1 arith, 1 assign) -> runs (n+m) times
        }
        // Normalization loop exit failed-check
        ops += 3; // final check failure -> +1 constant (runs once)
        
        // --- STRING BUILDER AND TRIM ZEROES ---
        StringBuilder finalResStr = new StringBuilder();
        int firstNonZero = resultArr.length - 1;
        ops += 5; // 1 obj inst, 1 assign, 1 arr lookup, 1 arith, 1 assign -> +1 constant (runs once)
        
        while (firstNonZero > 0 && resultArr[firstNonZero] == 0) {
            ops += 4; // 2 comp, 1 logical, 1 arr index -> runs variable zero-padding times
            
            firstNonZero--;
            ops += 2; // 1 arith, 1 assign -> runs variable zero-padding times
        }
        // While loop exit failed-check
        ops += 4; // final condition failure check (worst-case eval) -> +1 constant (runs once)
        
        // --- APPEND TO STRING BUILDER ---
        ops += 1; // int i = firstNonZero (1 assign) -> +1 constant (runs once)
        for (int i = firstNonZero; i >= 0; i--) {
            ops += 1; // i >= 0 (1 comp) -> runs remaining times
            
            finalResStr.append(resultArr[i]);
            ops += 2; // 1 arr index, 1 method call -> runs remaining times
            
            ops += 2; // i-- (1 arith, 1 assign) -> runs remaining times
        }
        // Append loop exit failed-check
        ops += 1; // final i >= 0 check failure -> +1 constant (runs once)
        
        result = new BigInteger(finalResStr.toString());
        ops += 3; // 2 method call/obj inst, 1 assign -> +1 constant (runs once)
        
        if (printDetails) { 
            System.out.println("-".repeat(width));
            System.out.printf("%" + width + "s\n", result.toString());
            System.out.println("=".repeat(width));
        }
        ops += 1; // if statement comparison

        ops += 1; // return method call
        return result; 
       
        
    }

    private static final java.util.Random random = new java.util.Random();

    public static void main(String[] args) {
       
        
        System.out.println("\n--- Required Demonstration (5230 * 3802) ---");
        BigInteger A = new BigInteger("5230");
        BigInteger B = new BigInteger("3802");
        SimpleMultiplication.multiply(A, B, true);
        
        System.out.println("\n--- Experiment: Operation Count vs N (n digits) ---");
        int[] N_values = {10, 50, 100, 500, 1000, 5000, 10000};
        
        System.out.println(String.format("%-8s | %-15s", "N_digits", "Operations Count (O(N^2))"));
        System.out.println("----------------------------------------");
        
        for (int N : N_values) {
            BigInteger randA = generateRandomBigInt(N);
            BigInteger randB = generateRandomBigInt(N); 
            
            SimpleMultiplication.multiply(randA, randB, false);
            System.out.println(String.format("%-8d | %-15d", N, SimpleMultiplication.ops));
        }

    }

    private static BigInteger generateRandomBigInt(int n) {
        if (n <= 0) return BigInteger.ZERO;
        StringBuilder sb = new StringBuilder(n);
        sb.append(random.nextInt(9) + 1); 
        for (int i = 0; i < n - 1; i++) {
            sb.append(random.nextInt(10));
        }
        return new BigInteger(sb.toString());
    }

  
}
