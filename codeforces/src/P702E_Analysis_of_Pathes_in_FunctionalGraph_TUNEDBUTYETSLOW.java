import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.TreeMap;

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
public class P702E_Analysis_of_Pathes_in_FunctionalGraph_TUNEDBUTYETSLOW {
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
		int[] nextK = new int[n];
		int[] w = new int[n];
		int[] visitor = new int[n];
		long[] aSumW = new long[n];
		int[] aMinW = new int[n];

		splitted = br.readLine().split("\\s+");
		for (i = 0; i < n; i++) {
			next[i] = Integer.parseInt(splitted[i]);
			visitor[i] = -1;
			nextK[i] = -1;
		}
		splitted = br.readLine().split("\\s+");
		for (i = 0; i < n; i++) {
			w[i] = Integer.parseInt(splitted[i]);
		}

		TreeMap<Integer, Long> tree = new TreeMap<Integer, Long>();
		Long zeroLong = Long.valueOf(0);
		
		for (i = 0; i < n; i++) {
			int p = i;
			int prev = -1;
			tree.clear();
			
			while (nextK[p] < 0) {
				long sumW;
				
				if (prev >= 0 && nextK[prev] >= 0) {
					Integer prevWeight = w[prev];
					Integer currWeight = w[nextK[prev]];
					sumW = aSumW[prev] - prevWeight + currWeight;
					long freq = tree.get(prevWeight).longValue();
					if (freq <= 1) {
						tree.remove(prevWeight);
					} else {
						tree.put(prevWeight, freq - 1);
					}
					freq = tree.getOrDefault(currWeight, zeroLong).longValue();
					tree.put(currWeight, freq + 1);
					nextK[p] = next[nextK[prev]];
				} else {
					int t = p;
					sumW = 0;
					for (long j = 0; j < k; j++) {
						int weight = w[t];
		
						if (visitor[t] == p) {
							// cycle detected
							long cycleW = 0;
							long cycleSize = 0;
							int v;
							for (v = t; cycleSize == 0 || v != t; v = next[v], cycleSize++) {
								cycleW += w[v];
							}
							long nCycles = (k - j) / cycleSize;
							sumW += nCycles * cycleW;

							boolean firstLoop = true;
							for (v = t; firstLoop || v != t; v = next[v], firstLoop = false) {
								tree.put(w[v], nCycles + tree.getOrDefault(w[v], zeroLong));
							}

							long remaining = (k - j) % cycleSize;
							v = t;
							for (v = t; remaining > 0; v = next[v], remaining--) {
								sumW += w[v];
								tree.put(w[v], 1 + tree.getOrDefault(w[v], zeroLong));
							}
							t = v;
							break;
						}
						
						sumW += weight;
						tree.put(weight, 1 + tree.getOrDefault(weight, zeroLong));
						visitor[t] = p;
						t = next[t];
					}
					nextK[p] = t;
				}
				
				aSumW[p] = sumW;
				aMinW[p] = tree.firstKey();
				prev = p;
				p = next[p];
			}

			pw.printf("%d %d\n", aSumW[i], aMinW[i]);
		}
		
		pw.close();
	}
}