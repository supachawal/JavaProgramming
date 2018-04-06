import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class P272_TeX_Quotes {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		StringBuilder answer = new StringBuilder();
		
		int c;
		boolean openQuote = true;
		boolean openBackSlash = false;
		
		while ((c = br.read()) != -1) {
			if (!openBackSlash && c == '"') {
				answer.append(openQuote ? "``" : "''");
				openQuote = !openQuote;
			} else if (!openBackSlash && c == '\\'){
				openBackSlash = true;
			} else {
				answer.append((char)c);
				openBackSlash = false;
			}
		}
		pw.print(answer.toString());
		pw.close();
	}
}
