import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class P133A_HQ9Plus_PASSED {

/*TESTCASE:
Examples
input
Hi!
output
YES

input
Codeforces
output
NO
 */
	private static BufferedReader br;
	private static PrintWriter pw;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String textLine;

		textLine = br.readLine();
		pw.printf("%s\n", textLine.matches(".*[HQ9]+.*") ? "YES" : "NO");
		
		pw.close();
	}
}
