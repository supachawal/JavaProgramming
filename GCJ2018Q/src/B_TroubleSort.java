import java.io.PrintWriter;
import java.util.Scanner;

public class B_TroubleSort {
	
/* Examples

input
5
5
5 6 8 4 3
3
8 9 7
4
5 6 8 4
6
5 6 8 4 3 1
6
5 6 8 4 1 3



output
Case #1: OK
Case #2: 1

*/	
	private Scanner in = new Scanner(System.in);
	private PrintWriter pw = new PrintWriter(System.out);
	
	public static void main(String[] args) {
		(new B_TroubleSort()).solve();
	}
	
	public void solve() {
		int T = in.nextInt();
		
		for (int t = 1; t <= T; t++) {
			int n = in.nextInt();
			int[] V = new int[n];
			
			for (int i = 0; i < n; i++) {
				V[i] = in.nextInt();
			}
			
			troubleSort(V);
			int answer = -1;
			int last = n - 1;
			for (int i = 0; i < last; i++) {
				if (V[i] > V[i + 1]) {
					answer = i;
					break;
				}
			}
			
			pw.printf("Case #%d: %s\n", t, (answer < 0 ? "OK" : answer));
		}
	    
		pw.flush();
	}
	
	private static void troubleSort(int[] L) {
		/*
		    TroubleSort(L): // L is a 0-indexed list of integers
			    let done := false
			    while not done:
			      done = true
			      for i := 0; i < len(L)-2; i++:
			        if L[i] > L[i+2]:
			          done = false
			          reverse the sublist from L[i] to L[i+2], inclusive
		 */
		boolean done = false;
		int n = L.length - 2;
		
		while (!done) {
			done = true;
			
			for (int i = 0; i < n; i++) {
		        if (L[i] > L[i+2]) {
					done = false;
					int temp = L[i];
					L[i] = L[i + 2];
					L[i + 2] = temp;
		        }
			}
		}
	}
		
}