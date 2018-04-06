import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;

public class P1048_SuperlongSums_BUG {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		String textLine;
		String[] splitted;
		BigInteger answer;
		
		textLine = br.readLine();
		int n = Integer.parseInt(textLine);
		StringBuilder s1 = new StringBuilder(n); 
		StringBuilder s2 = new StringBuilder(n); 
		
		for (int i = 0; i < n; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			s1.append(splitted[0]);
			s2.append(splitted[1]);
		}
		BigInteger a = new BigInteger(s1.toString());
		BigInteger b = new BigInteger(s2.toString());
		answer = a.add(b);
		pw.printf("%s\n", answer);
		pw.close();
	}
}
