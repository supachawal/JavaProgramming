import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class P656C_WithoutText {
	private static BufferedReader br;
	private static PrintWriter pw;
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String textLine = br.readLine();
		//String[] splitted = textLine.split("\\s+");
		long result = 0;
		
		for (char c : textLine.toCharArray()) {
			int i1 = ('@' < c && '[' > c) ? 1 : 0;
			int i2 = ('`' < c && '{' > c) ? 1 : 0;
			int alphaIndex = c - (i2 == 0 ? 'A' : 'a') + 1;
			
			result += alphaIndex * (i1 - i2);
		}
		
		pw.println(result);
		pw.close();
	}
}