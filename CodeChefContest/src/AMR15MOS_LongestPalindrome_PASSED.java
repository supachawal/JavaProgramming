import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class AMR15MOS_LongestPalindrome_PASSED {
/*
Sample
Input

2
4
aa
aa
bb
bb
3
ab
ba
ab
    
Output

aabbbbaa
abba

*/

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		int testCaseNumber = 0, testCaseCount;
		String textLine;
		@SuppressWarnings("unused")
		String[] splitted;
		String answer;
		
		int N;
		int aa, ab, ba, bb;
		
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			N = Integer.parseInt(textLine);
			aa = 0;
			ab = 0;
			ba = 0;
			bb = 0;
			
			for (int i = 0; i < N; i++) {
				textLine = br.readLine();
				if (textLine.equals("aa")) {
					aa++;
				} else if (textLine.equals("ab")) {
					ab++;
				} else if (textLine.equals("ba")) {
					ba++;
				} else if (textLine.equals("bb")) {
					bb++;
				}
			}

			answer = longestPalindrome(aa, ab, ba, bb);
			
			pw.printf("%s\n", answer);
		}
		pw.close();
	}
	
	private static String longestPalindrome(int aa, int ab, int ba, int bb) {
		StringBuilder left = new StringBuilder();
		StringBuilder right = new StringBuilder();
		String middle = "";
		int i;
		
		if (aa % 2 == 1)
			middle = "aa";
		else if (bb % 2 == 1)
			middle = "bb";
		
		aa /= 2;
		bb /= 2;

		for (i = 0; i < aa; i++) {
			left.append("aa");
			right.insert(0, "aa");
		}

		for (i = 0; i < ab && i < ba; i++) {
			left.append("ab");
			right.insert(0, "ba");
		}

		for (i = 0; i < bb; i++) {
			left.append("bb");
			right.insert(0, "bb");
		}
		
		return left.append(middle).append(right).toString();
	}
}
