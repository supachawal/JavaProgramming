import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;

class SMPLSUM_SimpleSum_FAILED {
/*
Example
 
Input: 
5
1
2
3
4
5

Output: 
1 
3 
7 
11 
21
 */
	private static class PrimeNumbering {
		private HashSet<Integer> primeNums;

		public PrimeNumbering(int PMax) {
			primeNums = new HashSet<Integer>(PMax / 2);
			boolean isPrime;
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
			 * i += w 
			 * w = 6 - w
			 * 
			 * return True
			 */
			primeNums.add(2);
			primeNums.add(3);

			int num, primeNum = 3;

			for (num = 5; primeNum <= PMax; num += 2) {
				isPrime = true;

				if (num % 3 == 0)
					continue;

				int i = 5;
				int w = 2;

				while (i * i <= num) {
					if (num % i == 0) {
						isPrime = false;
						break;
					}

					i += w;
					w = 6 - w;
				}

				if (isPrime) {
					primeNum = num;
					primeNums.add(num);
				}
			}
		}

		@SuppressWarnings("unused")
		public boolean isPrimeNumber(int a) {
			return primeNums.contains(a);
		}
	}

	@SuppressWarnings("unused")
	private static PrimeNumbering primeNumbering = new PrimeNumbering(10000000);

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		int testCaseNumber = 0, testCaseCount;
		String textLine;
		@SuppressWarnings("unused")
		String[] splitted;
		long answer;
		int n;

		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			n = Integer.parseInt(textLine);

			answer = findSum(n);

			pw.printf("%d\n", answer);
		}
		pw.close();
		br.close();
	}

	public static int gcd(int a, int b) {
		while (b != 0) {
			int mod = a % b;
			a = b;
			b = mod;
		}
		return a;
	}

	public static long findSum(int n) {
		long result = 0;
		int i;
		for (i = 1; i < n; i++) {
			result += n / gcd(i, n);
		}

//		if (primeNumbering.isPrimeNumber(n)) {
//			for (i = 1; i < n; i++) {
//				result += n;
//			}
//		} else {
//			for (i = 1; i < n; i++) {
//				if (primeNumbering.isPrimeNumber(i)) {
//					if (n % i == 0) {
//						result += n / i;
//					} else {
//						result += n;
//					}
//				} else {
//					result += n / gcd(i, n);
//				}
//			}
//		}

		return result + 1;
	}
}
