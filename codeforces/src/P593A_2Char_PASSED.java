import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
/*
A. 2Char

Andrew often reads articles in his favorite magazine 2Char. The main feature of these articles is that each of them uses 
at most two distinct letters. Andrew decided to send an article to the magazine, but as he hasn't written any article, 
he just decided to take a random one from magazine 26Char. However, before sending it to the magazine 2Char, he needs 
to adapt the text to the format of the journal. To do so, he removes some words from the chosen article, in such a way 
that the remaining text can be written using no more than two distinct letters.

Since the payment depends from the number of non-space characters in the article, Andrew wants to keep the words with the maximum total length.

Input
The first line of the input contains number n (1 ≤ n ≤ 100) — the number of words in the article chosen by Andrew. 
Following are n lines, each of them contains one word. All the words consist only of small English letters and their total 
length doesn't exceed 1000. The words are not guaranteed to be distinct, in this case you are allowed to use a word in 
the article as many times as it appears in the input.

Output
Print a single integer — the maximum possible total length of words in Andrew's article.

Examples
input
4
abb
cacc
aaa
bbb
output
9

input
5
a
a
bcbcb
cdecdecdecdecdecde
aaaa
output
6

Note
In the first sample the optimal way to choose words is {'abb', 'aaa', 'bbb'}.
In the second sample the word 'cdecdecdecdecdecde' consists of three distinct letters, and thus cannot be used in the 
article. The optimal answer is {'a', 'a', 'aaaa'}.

 */

public class P593A_2Char_PASSED {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		String textLine;
	//	String[] splitted;
		int i, j;
		int answer;

		int n;
		char[][] s;
		int[] sLen;
		String aChar;
		boolean[] charUsed = new boolean[26];
		
		textLine = br.readLine();
		n = Integer.parseInt(textLine);

		s = new char[n][2];
		sLen = new int[n];

	//	splitted = textLine.split("\\s+");
		for (i = 0; i < n; i++) {
			textLine = br.readLine();
			sLen[i] = textLine.length();
			for (j = 0; j < 2 && textLine.length() > 0; j++) {
				aChar = textLine.substring(0, 1);
				textLine = textLine.replace(aChar, "");
				s[i][j] = aChar.charAt(0);
			}
			if (textLine.length() > 0) {
				sLen[i] = 0;
			}
		}

		answer = maxLengthOf2Char(s, charUsed, sLen, 2, n - 1);

		pw.printf("%d", answer);
		pw.close();
	}
	
	private static int maxLengthOf2Char(char[][] s, boolean[] charUsed, int[] sLen, int quota, int k) {
		if (k < 0)
			return 0;

		// branch1: not used
		int branch1 = maxLengthOf2Char(s, charUsed, sLen, quota, k - 1);

		// branch2: used
		int branch2 = 0;
		if (sLen[k] > 0) {
			boolean quotaExceeded = false;
			int[] bookList = new int[2];
			int booking = 0;

			for (int j = 0; j < 2; j++) {
				if (s[k][j] != '\0' && !charUsed[s[k][j] - 'a']) {
					if (quota > booking) {
						bookList[booking++] = s[k][j] - 'a';
					} else {
						quotaExceeded = true;
						break;
					}
				}
			}
			if (!quotaExceeded) {
				for (int j = 0; j < booking; j++) {
					charUsed[bookList[j]] = true;
				}

				if (booking > 0) {
					branch2 = sLen[k] + maxLengthOf2Char(s, charUsed, sLen, quota - booking, k - 1);
				} else {
					branch2 = sLen[k] + branch1;
				}
				
				for (int j = 0; j < booking; j++) {
					charUsed[bookList[j]] = false;
				}
			}
		}
		
		return Math.max(branch1, branch2);
	}
}
