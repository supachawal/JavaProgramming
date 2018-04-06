import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class P3072_TrainOrder_PASSED {
/*
Sample Input

2
2
3
4
Sample Output

12
21
123
132
213
231
321
1234
1243
1324
1342
1432
2134
2143
2314
2341
2431
3214
3241
3421
4321

*/
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		String textLine;
		int T, N, t;
		T = Integer.parseInt(br.readLine());
		for (t = 0; t < T; t++) {
			if ((textLine = br.readLine()) == null) {
				break;
			}
			N = Integer.parseInt(textLine);
			int[] buffer = new int[N];
			permute(pw, N, 0L, buffer, 0, 0);
		}
		pw.close();
	}
	
	private static void permute(PrintWriter pw, int n, long visitFlag, int[] buffer, int currentBufferIndex, int maxSoFar) {
		if (currentBufferIndex >= n) {
			for (Integer num : buffer) {
				pw.print(num + 1);
			}
			pw.println();
			return;
		}
		for (int i = 0; i < n; i++) {
			if ((visitFlag & (1 << i)) == 0) {
				if (i < maxSoFar - 1) { 
					long x = ((1 << maxSoFar) - 1) & ~((1 << (i + 1)) - 1);
					if ((visitFlag & x) != x) {
						continue;
					}
				}
				buffer[currentBufferIndex] = i;
				permute(pw, n, visitFlag | (1 << i), buffer, currentBufferIndex + 1, Math.max(maxSoFar, i));
			}
		}
	}
}

