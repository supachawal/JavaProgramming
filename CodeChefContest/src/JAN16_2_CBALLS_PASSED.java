import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class JAN16_2_CBALLS_PASSED {
/*
Example

Input:
4
3
11 13 15
3
2 59 59
3
6 35 49
5
2 33 39 81 87 

Output:
3
2
1
1

*/

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		int testCaseNumber = 0, testCaseCount;
		String textLine;
		String[] splitted;
		long answer = 0;
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			int N = Integer.parseInt(textLine);

			textLine = br.readLine();
			splitted = textLine.split("\\s+");

			long[] buckets = new long[N + 1];
			buckets[0] = 2;
			for (int i = 1; i <= N; i++) {
				buckets[i] = Long.parseLong(splitted[i - 1]);
			}
			
			answer = minimumExtraBalls(buckets, N);
			
			
			pw.printf("%d\n", answer);
		}
		pw.close();
	}

	public static long minimumExtraBalls(long[] buckets, int n) {
		long fill, totalFill = 0;
		long totalTestFill, minTotalTestFill = Long.MAX_VALUE;
		long primeNum;
		int i;
		
		for (i = 1; i <= n; i++) {
			fill = buckets[i - 1] - buckets[i];
			if (fill > 0) {
				buckets[i] += fill;
				totalFill += fill;
			}
		}

		long maxPrimeNum = nextPrimeNumber(buckets[1]); 
		for (primeNum = 2; primeNum <= maxPrimeNum; primeNum = nextPrimeNumber(primeNum)) {
			totalTestFill = 0;
			for (i = 1; i <= n; i++) {
//				fill = (primeNum - buckets[i] % primeNum) % primeNum;
				fill = primeNum * ((buckets[i] + primeNum - 1) / primeNum) - buckets[i];
				if (fill > 0) {
					totalTestFill += fill;
				}
			}
			if (minTotalTestFill > totalTestFill) {
				minTotalTestFill = totalTestFill;
			}
		}
		
		return totalFill + minTotalTestFill;
	}
	
	public static long nextPrimeNumber(long num) {
		/*
		 * def isprime(n): """Returns True if n is prime""" 
		 * if n == 2:
		 * return True 
		 * if n == 3: return True 
		 * if n % 2 == 0: return False 
		 * if n % 3 == 0: return False
		 * 
		 * i = 5
		 * w = 2 
		 * while i * i <= n: 
		 *    if n % i == 0: return False
		 * 
		 * 	  i += w 
		 * 	  w = 6 - w
		 * 
		 * return True
		 */
		if (num < 2)
			return 2;
		if (num < 3)
			return 3;

		boolean isPrime = false;
		if (num % 2 == 0) {
			num--;
		}
		while (!isPrime) {
			num += 2;
			if (num % 3 != 0) {
				int i = 5;
				int w = 2;
	
				isPrime = true;
				while (i * i <= num) {
					if (num % i == 0) {
						isPrime = false;
						break;
					}
	
					i += w;
					w = 6 - w;
				}
			}
		}
		return num;
	}
}
