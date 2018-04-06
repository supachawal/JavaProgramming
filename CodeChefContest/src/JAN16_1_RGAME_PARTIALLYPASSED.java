import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;

class JAN16_1_RGAME_PARTIALLYPASSED {
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
		BigInteger bigMOD = new BigInteger("1000000007");
		
		int N;
		int i;
		int[] A;
		
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			N = Integer.parseInt(textLine);
			A = new int[N + 1];
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			for (i = 0; i <= N; i++) {
				A[i] = Integer.parseInt(splitted[i]);
			}

			long accum = A[0] * 2L;
			answer = 0;
			for (i = 1; i <= N; i++) {
				answer = (answer * 2 + A[i] * accum) % MOD;
				accum = (accum + (new BigInteger(splitted[i])).shiftLeft(i).mod(bigMOD).longValue()) % MOD;
			}
			
			pw.printf("%d\n", answer);
		}
		pw.close();
	}
}
