import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/*
 * Input
7
9 7 7 8 8 7 8
Output
10
Answer
9

Input
10
94 96 91 95 99 94 96 92 95 99
Output
100
Answer
106
 */
public class P348A_Mafia_CORRECT {
	private static BufferedReader br;
	private static PrintWriter pw;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		int i, n = Integer.parseInt(br.readLine());
		String[] splitted = br.readLine().split("\\s+");
		long maxValue = 0;
		long sum = 0;

		for (i = 0; i < n; i++) {
			long num = Long.parseLong(splitted[i]);
			maxValue = Math.max(maxValue, num);
			sum += num;
		}
		
		long answer = Math.max(maxValue, (sum + n - 2) / (n - 1) /* round-up */);		

		pw.printf("%d\n", answer);
		pw.close();
	}
}