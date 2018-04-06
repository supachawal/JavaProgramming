import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;

public class P1000_AplusB {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		String textLine;
		String[] splitted;
		BigInteger answer;
		
		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		BigInteger a = new BigInteger(splitted[0]);
		BigInteger b = new BigInteger(splitted[1]);
		answer = a.add(b);
		pw.printf("%s\n", answer);
		pw.close();
	}
}
