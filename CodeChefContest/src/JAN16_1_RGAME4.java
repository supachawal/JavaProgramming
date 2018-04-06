import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class JAN16_1_RGAME4 {
/*
Example

Input:
7
1
1 2
2
1 2 1
2
1 1 1
2
1 1 2
3
1 1 2 2
9
100000000 200000000 300000000 400000000 500000000 600000000 700000000 800000000 900000000 1000000000
20
1000000000 1000000000 1000000000 1000000000 1000000000 1000000000 1000000000 1000000000 1000000000 1000000000 1000000000 1000000000 1000000000 1000000000 1000000000 1000000000 1000000000 1000000000 1000000000 1000000000 1000000000 1000000000

Output:
4
14
8
12
48
800070742
27604473
*/

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		int testCaseNumber = 0, testCaseCount;
		String textLine;
		String[] splitted;
		long answer;
		final int MOD = 1000000007;
		
		int N;
		int[] A;
		int[] B;
		long sum;
		int i, j;
		
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			N = Integer.parseInt(textLine);
			A = new int[N + 1];
			B = new int[N + 1];
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			for (i = 0; i <= N; i++) {
				A[i] = Integer.parseInt(splitted[i]);
			}
			
			System.arraycopy(A, 0, B, 0, N + 1);
			for (i = 3; i <= N; i++) {
				for (j = i; j <= N; j++) {
					B[j] = (int)(((long)B[j] + B[j]) % MOD);
				}
			}
			sum = 0;
			for (i = 1; i <= N; i++) {
				sum = (sum + A[i]) % MOD;
			}
			answer = (A[0] * sum) % MOD;
			sum = positiveMod(sum - B[1], MOD);
			for (i = 1; i < N; i++) { // must x2 every level to child
				answer = (answer + A[i] * sum) % MOD;
				sum = positiveMod(sum - B[i + 1], MOD);
				sum = (sum * 2L) % MOD;
			}
			
			pw.printf("%d\n", (answer * 2) % MOD);
		}
		pw.close();
	}

	private static long positiveMod(long dividend, long divisor) {
		long result = dividend % divisor;
		
		if (result < 0) {
			result += divisor;
		}
		
		return result; 
	}
	
}
