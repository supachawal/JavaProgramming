import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class MSTEP_CountStepsInMatrix_PASSED {
/*Example

Input:
2
2
1 3
2 4
3
1 7 9
2 4 8
3 6 5
Output:
4
12
*/
	// https://www.codechef.com/problems/WDTBAM
	// TAG: walk in map

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		int testCaseNumber = 0, testCaseCount;
		String textLine;
		String[] splitted;
		int answer;
		int i, j;

		int N; // # elements
		int[][] A;
		int cellValue;
		
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			N = Integer.parseInt(textLine);
			A = new int[N * N + 1][2];
			
			for (i = 0; i < N; i++) {
				textLine = br.readLine();
				splitted = textLine.split("\\s+");
				for (j = 0; j < N; j++) {
					cellValue = Integer.parseInt(splitted[j]);
					A[cellValue][0] = i;
					A[cellValue][1] = j;
				}
			}

//			if (testCaseNumber == 6) {
//				System.gc();
//			}
			answer = countSteps(A, N);
			
			pw.printf("%d\n", answer);
		}
		pw.close();
		br.close();
	}

	public static int countSteps(int[][] A, int n) {
		int count = 0;
		int nn = n * n;
		for (int i = 1; i < nn; i++) {
			int k = i + 1;
			
			count += Math.abs(A[k][0] - A[i][0]) + Math.abs(A[k][1] - A[i][1]);
		}
		return count;
	}
}
