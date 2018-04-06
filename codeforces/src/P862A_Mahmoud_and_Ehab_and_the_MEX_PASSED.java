import java.util.Arrays;
import java.util.Scanner;
import java.io.PrintWriter;

public class P862A_Mahmoud_and_Ehab_and_the_MEX_PASSED {
	
/* the MEX of the set {0, 2, 4} is 1 and the MEX of the set {1, 2, 3} is 0 .
 * Category: Implementation
 * Examples
input
5 3
0 4 5 6 7
output
2
input
1 0
0
output
1
input
5 0
1 2 3 4 5
output
0
input
1 50
0
output
49
input
2 50
1 100
output
49
 */	
	private Scanner in = new Scanner(System.in);
	private PrintWriter pw = new PrintWriter(System.out);
	
	public static void main(String[] args) {
		(new P862A_Mahmoud_and_Ehab_and_the_MEX_PASSED()).solve();
	}
	
	public void solve() {
		int n = in.nextInt();
		int x = in.nextInt();
		
		int[] A = new int[n];
		int answer = 0, r = 0, i;
		int nInsertions;
		
		for (i = 0; i < n; i++) {
			A[i] = in.nextInt();
		}
		
		Arrays.sort(A);
		
		for (i = 0; i < n && A[i] < x; i++) {
			nInsertions = A[i] - r;
		    if (nInsertions > 0) {
		        answer += nInsertions;
		    } 

		    r = A[i] + 1;
		}

		nInsertions = x - r;
	    if (nInsertions > 0) {
	        answer += nInsertions;
		} 
	    
	    if (i < n && A[i] == x) {
			answer++;
		}
	    
		pw.printf("%d\n", answer);
		pw.flush();
	}
}