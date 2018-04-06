import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class WDTBAM_WhoDaresToBeAMillionaire_PASSED {
/*Example

Input:
3
5
ABCDE
EBCDA
0 10 20 30 40 50
4
CHEF
QUIZ
4 3 2 1 0
8
ABBABAAB
ABABABAB
100 100 100 100 100 100 100 100 100

Output:
30
4
100
*/
	// https://www.codechef.com/problems/WDTBAM
	// TAG: greedy optimization

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		int testCaseNumber = 0, testCaseCount;
		String textLine;
		String[] splitted;
		int answer;
		int i;

		int N; // # elements
		String ca, a;
		int[] W;
		
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			N = Integer.parseInt(textLine);

			ca = br.readLine().substring(0, N);
			a = br.readLine().substring(0, N);

			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			
			W = new int[N + 1];
			
			for (i = 0; i <= N; i++) {
				W[i] = Integer.parseInt(splitted[i]);
			}

//			if (testCaseNumber == 6) {
//				System.gc();
//			}
			answer = maxWinnings(ca, a, N, W);
			
			pw.printf("%d\n", answer);
		}
		pw.close();
		br.close();
	}

	private static int countCorrectnesses(String correctAns, String ans, int n) {
		char[] a = correctAns.toCharArray();
		char[] b = ans.toCharArray();
		int count = 0;
		for (int i = 0; i < n; i++) {
			if (a[i] == b[i])
				count++;
		}
		return count;
	}

	public static int maxWinnings(String correctAns, String ans, int n, int[] W) {
		int correctnessCount = countCorrectnesses(correctAns, ans, n);
		int X, maxW = 0;
		
		if (correctnessCount == n)
			maxW = W[correctnessCount];
		else {
			for (X = 0; X <= correctnessCount; X++) {
				if (maxW < W[X]) {
					maxW = W[X];
				}
			}
		}
		return maxW;
	}
}
