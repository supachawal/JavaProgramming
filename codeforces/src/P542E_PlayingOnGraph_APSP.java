import java.util.Arrays;
import java.util.Scanner;
import java.io.PrintWriter;

public class P542E_PlayingOnGraph_APSP {
	
/* Category: shortest path
 * Examples
input
5 4
1 2
2 3
3 4
3 5
output
3
input
4 6
1 2
2 3
1 3
3 4
2 4
1 4
output
-1
input
4 2
1 3
2 4
output
2
 */	
	private Scanner in = new Scanner(System.in);
	private PrintWriter pw = new PrintWriter(System.out);
	
	public static void main(String[] args) {
		(new P542E_PlayingOnGraph_APSP()).solve();
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