import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class P10405_LongestCommonSubsequence {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		String textLine;
		int answer;

		while ((textLine = br.readLine()) != null) {
			if (textLine.length() == 0)
				break;

			StringBuilder solution = new StringBuilder();
			answer = longestCommonSubsequence(textLine.toCharArray(), br.readLine().toCharArray(), solution);
			pw.printf("%d %s\n", answer, solution);
		}
		
		pw.close();
	}
	
	private static int longestCommonSubsequence(char[] A, char[] B, StringBuilder solution) {
		int i = 0, j = 0;
		int m = A.length, n = B.length;
		int[][] C = new int[m + 1][n + 1];
		int max = 0;
// No need		
//		for (i = 0; i <= m; i++) {
//			C[i][0] = 0;
//		}
//		for (j = 0; j <= n; j++) {
//			C[0][j] = 0;
//		}
		
		for (i = 1; i <= m; i++) {
			for(j = 1; j <= n; j++) {
				if (A[i - 1] == B[j - 1]) {
					C[i][j] = C[i-1][j-1] + 1;
					if (C[i][j] > max) {
						max = C[i][j];
					}
				} else {
					if (C[i][j-1] >= C[i-1][j]) {
						C[i][j] = C[i][j-1];
					} else {
						C[i][j] = C[i-1][j];
					}
				}
			}
		}

		if (solution != null) {
			if (m <= n) {
				for (i = 1; i <= m; i++) {
					if (C[i][n] > C[i-1][n]) {
						solution.append(A[i - 1]);
					}
				}
			} else {
				for (j = 1; j <= n; j++) {
					if (C[m][j] > C[m][j-1]) {
						solution.append(B[j - 1]);
					}
				}
			}
		}
		return max;
	}
}
