import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class SUBINC_CountSubarrays_PASSED {
/*Example

Input:
4
4
1 4 2 3
1
5
8
8 9 1 1 2 2 0 0
8
0 0 0 0 0 0 0 0

Output:
6
1
16
*/
	// https://www.codechef.com/problems/SUBINC
	// TAG: non-decreasing substring
	static int[] A = new int[100000];	// by problem assumption

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		int testCaseNumber = 0, testCaseCount;
		String textLine;
		String[] splitted;
		long answer;
		int i;

		int N; // # elements
		
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			N = Integer.parseInt(textLine);

			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			
			for (i = 0; i < N; i++) {
				A[i] = Integer.parseInt(splitted[i]);
			}

//			if (testCaseNumber == 6) {
//				System.gc();
//			}
			answer = countPossibleNondecreasingSubarrays(N);
			
			pw.printf("%d\n", answer);
		}
		pw.close();
		br.close();
	}

	public static long countPossibleNondecreasingSubarrays(int n) {
		long count = 0;
		int i, j;
		
		for (i = 0; i < n; i = j) {
			for (j = i + 1; j < n && A[j - 1] <= A[j]; j++) {
			}
			
			count += ((long)j - i) * (j - i + 1L) / 2L;
		}
		
		return count;
	}
}
