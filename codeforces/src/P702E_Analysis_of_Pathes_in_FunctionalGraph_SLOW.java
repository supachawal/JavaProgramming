import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/* 
 * 7 3
1 2 3 4 3 2 6
6 3 1 4 2 2 3

extreme simple ring
Input 
1 10000000000
0
10000


 */
public class P702E_Analysis_of_Pathes_in_FunctionalGraph_SLOW {
	private static BufferedReader br;
	private static PrintWriter pw;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String[] splitted = br.readLine().split("\\s+");
		int n = Integer.parseInt(splitted[0]);
		long k = Long.parseLong(splitted[1]);
		int i;
		int[] next = new int[n];
		int[] w = new int[n];
		int[] visitor = new int[n];

		splitted = br.readLine().split("\\s+");
		for (i = 0; i < n; i++) {
			next[i] = Integer.parseInt(splitted[i]);
			visitor[i] = -1;
		}
		splitted = br.readLine().split("\\s+");
		for (i = 0; i < n; i++) {
			w[i] = Integer.parseInt(splitted[i]);
		}
		for (i = 0; i < n; i++) {
			int t = i;
			long sumW = 0;
			int minW = Integer.MAX_VALUE;
			for (long j = 0; j < k; j++) {
				int weight = w[t];
				minW = Math.min(minW, weight);

				if (visitor[t] == i) {
					// cycle detected
					long cycleW = weight;
					long cycleSize = 1;
					for (int v = next[t]; v != t; v = next[v]) {
						cycleW += w[v];
						cycleSize++;
					}
					
					long sumCycleW = (k - j) / cycleSize * cycleW;
					sumW += sumCycleW;
					long remaining = (k - j) % cycleSize;
					while (remaining-- > 0) {
						sumW += w[t];
						t = next[t];
					}
					break;
				}
				
				sumW += weight;
				visitor[t] = i;
				t = next[t];
			}
			pw.printf("%d %d\n", sumW, minW);
		}
		
		pw.close();
	}
}