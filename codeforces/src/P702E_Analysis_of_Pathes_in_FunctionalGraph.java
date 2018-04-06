import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/* 
 * Examples
input
7 3
1 2 3 4 3 2 6
6 3 1 4 2 2 3
output
10 1
8 1
7 1
10 2
8 2
7 1
9 3
input
4 4
0 1 2 3
0 1 2 3
output
0 0
4 1
8 2
12 3
input
5 3
1 2 3 4 0
4 1 2 14 3
output
7 1
17 1
19 2
21 3
8 1

extreme simple ring
Input 
1 10000000000
0
10000


 */
public class P702E_Analysis_of_Pathes_in_FunctionalGraph {
	private static BufferedReader br;
	private static PrintWriter pw;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String[] splitted = br.readLine().split("\\s+");
		int n = Integer.parseInt(splitted[0]);
		long k = Long.parseLong(splitted[1]);
		int i, j;
		int nSparseBits = (int)(Math.log(1e10)/Math.log(2)) + 1;
		int[][] next = new int[nSparseBits][n + 1];
		int[][] w = new int[nSparseBits][n + 1];
		long[][] S = new long[nSparseBits][n + 1];

		splitted = br.readLine().split("\\s+");
		for (j = 1; j <= n; j++) {
			next[0][j] = Integer.parseInt(splitted[j - 1]) + 1;
		}
		splitted = br.readLine().split("\\s+");
		for (j = 1; j <= n; j++) {
			S[0][j] = w[0][j] = Integer.parseInt(splitted[j - 1]);
		}

		for (i = 1; i < nSparseBits; i++) {
			for (j = 1; j <= n; j++) {
				int p = next[i-1][j];
				next[i][j] = next[i-1][p];
				w[i][j] = Math.min(w[i-1][j], w[i-1][p]);
				S[i][j] = S[i-1][j] + S[i-1][p];
			}
		}
		
		for (j = 1; j <= n; j++) {
			long sumW = 0;
			int minW = Integer.MAX_VALUE;
			int p = j;
			long span = k;
			for (i = 0; i < nSparseBits; i++, span >>>= 1) {
				if ((span & 1) != 0) {
					minW = Math.min(minW, w[i][p]);
					sumW += S[i][p];
					p = next[i][p];
				}
			}
			
			pw.printf("%d %d\n", sumW, minW);
		}
		
		pw.close();
	}
}