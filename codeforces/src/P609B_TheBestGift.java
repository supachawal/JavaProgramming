import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class P609B_TheBestGift {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		String textLine;
		String[] splitted;
		int i, j;
		long answer;

		int n;
		int m;
		int[] freq;
		
		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		n = Integer.parseInt(splitted[0]);
		textLine = br.readLine();
		m = Integer.parseInt(splitted[1]);

		freq = new int[m + 1];
		splitted = textLine.split("\\s+");
		for (i = 0; i < n; i++) {
			freq[Integer.parseInt(splitted[i])]++;
		}

		answer = 0;
		for (i = 1; i < m; i++) {
			for (j = i + 1; j <= m; j++) {
				answer += (long)freq[i] * freq[j];
			}
		}
		
		pw.printf("%d", answer);
		pw.close();
	}
}
