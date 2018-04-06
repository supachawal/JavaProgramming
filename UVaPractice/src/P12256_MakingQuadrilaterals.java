import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class P12256_MakingQuadrilaterals {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		int testCaseNumber = 0;
//		String textLine;
//		String[] splitted;
		long answer;
		int n;
		
//		splitted = textLine.split("\\s+");
		cachedResult[0] = 1;
		cachedResult[1] = 1;
		cachedResult[2] = 1;
		maxN = 3;
		
		while ((n = Integer.parseInt(br.readLine())) > 0) {
			answer = minLongestRodLength(n);
			pw.printf("Case %d: %d\n", ++testCaseNumber, answer);
		}
		
		pw.close();
	}
	
	private static long[] cachedResult = new long[60]; 
	private static int maxN = 0; 
	public static long minLongestRodLength(int n) {
		if (n < 1)
			return 0;
		
		if (n > maxN) {
			for (int i = maxN; i < n; i++) {
				cachedResult[i] = cachedResult[i - 1] + cachedResult[i - 2] + cachedResult[i - 3];
			}
		
			maxN = n;
		}

		return cachedResult[n - 1];
	}
}
