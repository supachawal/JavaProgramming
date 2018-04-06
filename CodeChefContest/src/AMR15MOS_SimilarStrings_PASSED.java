import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class AMR15MOS_SimilarStrings_PASSED {
/*Example

Input:
2
1
2

Output:
26
754
*/
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		int testCaseNumber = 0, testCaseCount;
		String textLine;
		@SuppressWarnings("unused")
		String[] splitted;
		long answer;

		int N; // # elements
		
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			N = Integer.parseInt(textLine);
			
			answer = countSimilarStrings(N);
			
			pw.printf("%d\n", answer);
		}
		pw.close();
	}

	public static long countSimilarStrings(int n) {
		final long MOD = 1000000007L;
		long result = 26L * n * n + 26L * 25L * (n - 1);
		return result % MOD;
	}
}
