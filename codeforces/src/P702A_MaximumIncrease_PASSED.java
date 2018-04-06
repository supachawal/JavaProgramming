import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class P702A_MaximumIncrease_PASSED {
	private static BufferedReader br;
	private static PrintWriter pw;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		int n = Integer.parseInt(br.readLine());
		String[] splitted = br.readLine().split("\\s+");
		
		int maxIncreasingLength = 1, increasingLength = 1;
		int prevVal = Integer.parseInt(splitted[0]), currVal;

		for (int i = 1; i < n; i++) {
			currVal = Integer.parseInt(splitted[i]);
			if (prevVal < currVal) {
				increasingLength++;
				if (maxIncreasingLength < increasingLength) {
					maxIncreasingLength = increasingLength;
				}
			} else {
				increasingLength = 1;
			}
			
			prevVal = currVal;
		}
		
		pw.printf("%d\n", maxIncreasingLength);
		pw.close();
	}
}