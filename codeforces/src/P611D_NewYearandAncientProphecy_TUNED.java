import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class P611D_NewYearandAncientProphecy_TUNED {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		String textLine;
		long answer = 0;

		int n;
		
		textLine = br.readLine();
		//splitted = textLine.split("\\s+");
		n = Integer.parseInt(textLine);
		textLine = br.readLine();
		countWaysToSplit_memoization = new Long[n][n];
		answer = 1 + countWaysToSplit(textLine.toCharArray(), n, 0, 0); 
		
		pw.printf("%d", answer);
		pw.close();
	}
	
	private static Long[][] countWaysToSplit_memoization;
	
	public static long countWaysToSplit(char[] s, int n, int p, int k) {
		if (countWaysToSplit_memoization[p][k] != null) {
			return countWaysToSplit_memoization[p][k];
		}
		
		long result = 0;
		for (int i = k + 1; i < n; i++) {
			if (s[i] != '0' 
					&& compareSubstring(s, p, k, i - 1) < 0 
					&& compareSubstring(s, k, i, n - 1) < 0) {
				result += 1L + countWaysToSplit(s, n, k, i);
				if (result > 1000000007L) {
					result %= 1000000007L;
				}
			}
		}
		
		countWaysToSplit_memoization[p][k] = result;
		return result;
	}
	public static int compareSubstring(char[] s, int start1, int start2, int end2) {
		int end1 = start2 - 1;
		int len1 = end1 - start1 + 1;
		int len2 = end2 - start2 + 1;
		
		if (len1 != len2) {
			return len1 - len2; 
		}

		for (int i = 0; i < len1; i++) {
			if (s[start1 + i] != s[start2 + i]) {
				return s[start1 + i] - s[start2 + i];
			}
		}
		return 0;
	}
}
