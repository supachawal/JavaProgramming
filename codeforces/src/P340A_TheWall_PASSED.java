import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class P340A_TheWall_PASSED {
	private static BufferedReader br;
	private static PrintWriter pw;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String textLine = br.readLine();
		String[] splitted = textLine.split("\\s+");
		long x = Long.parseLong(splitted[0]);
		long y = Long.parseLong(splitted[1]);
		long a = Long.parseLong(splitted[2]);
		long b = Long.parseLong(splitted[3]);
		long lcmXY = x * y / euclideanGcd(x, y);
		
		if (lcmXY < a) {
			a = lcmXY * (a / lcmXY + (a % lcmXY != 0 ? 1 : 0));
		} else {
			a = lcmXY;
		}
		long answer = (b - a < 0 ? 0 : 1 + (b - a) / lcmXY);
		pw.print(answer);
		pw.close();
	}
	
    /**
     * Calculate GCD of a and b interpreted as unsigned integers.
     */
    @SuppressWarnings("unused")
	private static long binaryGcd(long x, long y) {
    	long a = x, b = y;
        if (b == 0)
            return a;
        if (a == 0)
            return b;

        // Right shift a & b till their last bits equal to 1.
        int aZeros = Long.numberOfTrailingZeros(a);
        int bZeros = Long.numberOfTrailingZeros(b);
        a >>>= aZeros;
        b >>>= bZeros;

        int t = (aZeros <= bZeros ? aZeros : bZeros);

        while (a != b) {
            if (a > b) {  // a > b as unsigned
                a -= b;
                a >>>= Long.numberOfTrailingZeros(a);
            } else {
                b -= a;
                b >>>= Long.numberOfTrailingZeros(b);
            }
        }
        return a<<t;
    }

    private static long euclideanGcd(long a, long b) {
		long p = a, q = b; // using local var will be looped faster 
		while (q != 0) {
			long mod = p % q;
			p = q;
			q = mod;
		}
		return p;
	}
    
}