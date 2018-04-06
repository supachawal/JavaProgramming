import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class P616A_Comparing2LongIntegers_PASSED {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		String s1 = br.readLine();
		String s2 = br.readLine();
		int compare = compareStringInteger(s1, s2);
		pw.printf("%c\n", compare == 0 ? '=' : (compare > 0 ? '>' : '<'));
		pw.close();
	}
	
	public static int compareStringInteger(String s1, String s2) {
		int len1 = s1.length();
		int len2 = s2.length();
		
		int start1 = 0;
		while (start1 < len1 && s1.charAt(start1) == '0')
			start1++;
		len1 -= start1;

		int start2 = 0;
		while (start2 < len2 && s2.charAt(start2) == '0')
			start2++;
		len2 -= start2;
		
		if (len1 != len2) {
			return len1 - len2; 
		}

		for (int i = 0; i < len1; i++) {
			char c1 = s1.charAt(start1 + i);
			char c2 = s2.charAt(start2 + i);
			
			if (c1 != c2) {
				return c1 < c2 ? -1 : 1;
			}
		}
		return 0;
	}
}