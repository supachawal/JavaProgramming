import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class P680A_Bear_and_FiveCards {
	private static BufferedReader br;
	private static PrintWriter pw;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String textLine = br.readLine();
		String[] splitted = textLine.split("\\s+");
		int[] freq = new int[101];
		
		int n = splitted.length;
		int maxD = 0;
		int sumValue = 0;
		int i;

		for (i = 0; i < n; i++) {
			int num = Integer.parseInt(splitted[i]);
			sumValue += num;

			int f = freq[num];
			if (f < 3) {
				f++;
				freq[num] = f;
				
				if (f > 1) {
					maxD = Math.max(maxD, f * num);
				}
			}
		}
		
		pw.printf("%d\n", sumValue - maxD);
		pw.close();
	}
}