import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

public class P609C_LoadBalancing_BUG {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		String textLine;
		String[] splitted;
		int i;
		long answer;

		int n;
		long sum = 0;
		@SuppressWarnings("unused")
		long avg, moves, remainder;
		int[] m;
		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		n = Integer.parseInt(splitted[0]);
		m = new int[n];

		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		for (i = 0; i < n; i++) {
			m[i] = Integer.parseInt(splitted[i]);
			sum += m[i];
		}
		avg = sum / n;
		remainder = sum - avg * n;
		Arrays.sort(m);
		answer = 0;
		for (i = 0; i < n && (moves = avg - m[i]) > 0; i++) {
			answer += moves;
		}

		pw.printf("%d", answer );
		pw.close();
	}
}
