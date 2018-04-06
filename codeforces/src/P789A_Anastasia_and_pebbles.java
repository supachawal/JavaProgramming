import java.util.Scanner;
import java.io.PrintWriter;

/* 
 * Examples
input
3 2
2 3 4
output
3
input
5 4
3 1 8 9 7
output
5

 */
public class P789A_Anastasia_and_pebbles {
	
	private static PrintWriter pw;
	
	public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
		pw = new PrintWriter(System.out);
		int n = in.nextInt();
		int k = in.nextInt();
		int carry = 0;
		long answer = 0;

		for (int i = 0; i < n; i++) {
			int w = in.nextInt() - carry * k;
			carry = 0;
			int days = w / (2 * k);
			answer += days;
			w -= days * 2 * k;
			if (w > 0) {
				answer++;
				if (w <= k) {
					carry = 1;
				}
			}
		}
		pw.printf("%d\n", answer);
		in.close();
		pw.close();
	}
}