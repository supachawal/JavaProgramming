import java.util.Scanner;
import java.io.PrintWriter;

/* 
 * Examples
input
4
73
67
38
33
output
75
67
40
33

 */
public class Implementation_Grading {
	
	private static PrintWriter pw;
	
	public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
		pw = new PrintWriter(System.out);
		int i, k, n = in.nextInt();
		for (i = 0; i < n; i++) {
			k = in.nextInt();
			
			if (k >= 38) {
				int roundUp = (k + 2) / 5 * 5; 
				if (roundUp > k) {
					k = roundUp;
				}
			}
			pw.printf("%d\n", k);
		}
		in.close();
		pw.close();
	}
}