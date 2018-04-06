import java.io.PrintWriter;
import java.util.Scanner;

/* 
 * Examples
input
2 3
2 4
16 32 96
output
3

 */
public class Implementation_Between2Sets {
	
	private static PrintWriter pw;
	
	public static void main(String[] args) {
		pw = new PrintWriter(System.out);
        Scanner in = new Scanner(System.in);
        int i, j, n = in.nextInt(), m = in.nextInt();
        int[] a = new int[n];
        for(i = 0; i < n; i++) {
            a[i] = in.nextInt();
        }
        int[] b = new int[m];
        for(i = 0; i < m; i++){
            b[i] = in.nextInt();
        }

		in.close();

		int answer = 0;
		//String[] splitted = br.readLine().split("\\s+");
        for (i = 1; i <= 100; i++) {
        	for (j = 0; j < n; j++) {
        		if (i % a[j] != 0) {
        			break;
        		}
        	}
        	
        	if (j >= n) {
            	for (j = 0; j < m; j++) {
            		if (b[j] % i != 0) {
            			break;
            		}
            	}
            	
            	if (j >= m) {
            		answer++;
            	}
        	}
        }
		pw.printf("%d\n", answer);
		pw.close();
	}
}