import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;

public class P702B_PowerOfTwo_PASSED {
	private static BufferedReader br;
	private static PrintWriter pw;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		int i, n = Integer.parseInt(br.readLine());
		String[] splitted = br.readLine().split("\\s+");
		long answer = 0;
		HashMap<Integer, Integer> c = new HashMap<Integer, Integer>();

		for (i = 0; i < n; i++) {
			int Ai = Integer.parseInt(splitted[i]);
			long p = 1L << 31;
			
			while (p > 1) {
				answer += c.getOrDefault((int)(p - Ai), 0);
				p >>= 1;
			}
			
			c.put(Ai, c.getOrDefault(Ai, 0) + 1);
		}
		
		pw.printf("%d\n", answer);
		pw.close();
	}
}