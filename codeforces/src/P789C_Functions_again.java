import java.util.Scanner;
import java.io.PrintWriter;

/* 
 * Examples
input
5
1 4 2 3 1
output
3

input
4
1 5 4 7
output
6

input
4
1000000000 -1000000000 -1000000000 1000000000
output
4000000000

 */
public class P789C_Functions_again {
	
	private static PrintWriter pw;
	
	public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
		pw = new PrintWriter(System.out);
		int i, n = in.nextInt() - 1;
		long[] diffList = new long[n];
		long num = in.nextLong();
		long answer = Long.MIN_VALUE;
		
		for (i = 0; i < n; i++) {
			long nextNum = in.nextLong();
			diffList[i] = Math.abs(num - nextNum);
			num = nextNum;
		}
		
		num = 0;
		for (i = 0; i < n; i++) {
		    if (num < 0) {
		        num = 0;
		    }    

		    num += diffList[i] * (1 - (i & 1) * 2);
		    
		    if (answer < num) {
			    answer = num;
		    }
		}

		num = 0;
		for (i = 1; i < n; i++) {
		    if (num < 0) {
		        num = 0;
		    }    
		    num += diffList[i] * ((i & 1) * 2 - 1);
		    
		    if (answer < num) {
			    answer = num;
		    }
		}

		pw.printf("%d\n", answer);
		in.close();
		pw.close();
	}
}