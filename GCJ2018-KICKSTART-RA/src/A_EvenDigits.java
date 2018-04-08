import java.util.Scanner;
import java.io.PrintWriter;

public class A_EvenDigits {
	
/* Examples
input
1
100000

input
4
42
11
1
2018

output
Case #1: 0
Case #2: 3
Case #3: 1
Case #4: 2
 */	
	private Scanner in = new Scanner(System.in);
	private PrintWriter pw = new PrintWriter(System.out);
	
	public static void main(String[] args) {
		(new A_EvenDigits()).solve();
	}
	
	public void solve() {
		int T = in.nextInt();
		
		for (int t = 1; t <= T; t++) {
			long N = in.nextLong();
			long x1 = maxLowerAllEvenDigits(N);
			long x2 = minUpperAllEvenDigits(N);
			long answer = Math.min(N - x1, x2 - N);
			pw.printf("Case #%d: %d\n", t, answer);
		}
	    
		pw.flush();
	}
	
	private static long maxLowerAllEvenDigits(long x) {
		String s = String.valueOf(x);
		StringBuilder sb = new StringBuilder();
		boolean carry = false;
		for (char c : s.toCharArray()) {
			char newC = '8';
			
			if (!carry) {
				int a = c - '0';
				if ((a & 1) == 0) {
					newC = c;
				} else {
					newC = (char)(c - 1);
					carry = true;
				}
			}
			
			sb.append(newC);
		}
		return Long.valueOf(sb.toString(), 10);
	}

	private static long minUpperAllEvenDigits(long x) {
		String s = String.valueOf(x);
		StringBuilder sb = new StringBuilder();
		boolean carry = false;
		for (char c : s.toCharArray()) {
			char newC = '0';
			
			if (!carry) {
				int a = c - '0';
				if ((a & 1) == 0) {
					newC = c;
				} else {
					newC = (char)(c + 1);
					carry = true;
				}
			}
			
			if (newC > '9') {
				sb.append("10");
			} else {
				sb.append(newC);
			}
		}
		return Long.valueOf(sb.toString(), 10);
	}
}