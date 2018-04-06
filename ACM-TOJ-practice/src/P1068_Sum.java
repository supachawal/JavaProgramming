import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class P1068_Sum {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		long answer;

		long N = Long.parseLong(br.readLine());
		
		answer = (Math.abs(N - 1) + 1) * (1 + N) / 2;
		pw.printf("%d\n", answer);
		pw.close();
	}
}
