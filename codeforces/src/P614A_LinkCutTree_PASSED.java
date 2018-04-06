import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;

public class P614A_LinkCutTree_PASSED {
	private static BufferedReader br;
	private static PrintWriter pw;
/*
TEST CASE:

Input
62769392426654367 567152589733560993 688813
Answer
326816522793383797 	

Input
237171123124584251 923523399718980912 7150
Answer
-1

Input
2861381721051425 2861381721051425 1234
Answer
-1

 */
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String textLine = br.readLine();
		String[] splitted = textLine.split("\\s+");
		long l = Long.parseLong(splitted[0]);
		long r = Long.parseLong(splitted[1]);
		long k = Long.parseLong(splitted[2]);
		int count = 0;
		long nextI = 1;
		boolean overflow = false;
		try {
			long i = BigInteger.valueOf(k).pow((int)Math.ceil(Math.log(l)/Math.log(k))).longValueExact();
			if (i < l) {
				nextI = i * k;
				if (nextI / k != i) {
					overflow = true;
				} else {
					i = nextI;
				}
			}
			
			if (!overflow) {
				while (i <= r) {
					if (count++ > 0)
						pw.printf(" %d", i);
					else 
						pw.printf("%d", i);
					
					nextI = i * k;
					if (nextI / k != i) {
						break;
					}
					i = nextI;
				}
			}
		} catch (ArithmeticException e) {
		}
		
		if (count == 0) {
			pw.print("-1");
		}

		pw.close();
	}
}