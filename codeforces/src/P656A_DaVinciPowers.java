import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class P656A_DaVinciPowers {
	private static BufferedReader br;
	private static PrintWriter pw;
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String textLine = br.readLine();
		//String[] splitted = textLine.split("\\s+");
		int power = Integer.parseInt(textLine);
		long result = 1L << power;
		
		power -= 13;
		if (power >= 0) {
			result -= (1L << power) * 100;
		}

		pw.println(result);
		pw.close();
	}
}