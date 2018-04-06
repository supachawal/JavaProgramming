import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class GCDandLCM {
/*Example

Input:
3
120 11
10213 312
10 3

Output

1 1320
1 3186456
1 30
*/
	// https://www.codechef.com/problems/WDTBAM
	// TAG: greedy optimization

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		int testCaseNumber = 0, testCaseCount;
		String textLine;
		String[] splitted;
		long answer;
//		int i;

		long a, b;
		
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			splitted = textLine.split("\\s+");
			a = Long.parseLong(splitted[0]);
			b = Long.parseLong(splitted[1]);

			answer = gcdEuclidean(a, b);
			
			pw.printf("%d %d\n", answer, a * b / answer);
		}
		pw.close();
		br.close();
	}

	private static long gcdEuclidean(long a, long b) {
		long p = a, q = b;
		while (q != 0) {
			long mod = p % q;
			p = q;
			q = mod;
		}
		return p;
	}

	@SuppressWarnings("unused")
	private static long gcdDijkstra(long a, long b) {
		long m = a, n = b;
		while (m != n) {
			if (m > n)
				m -= n;
			else
				n -= m;
		}
		return m;
	}
}
