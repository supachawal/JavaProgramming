import java.io.PrintWriter;
import java.util.Scanner;

public class D_CubicUFO {
	
/* Examples

input
2
1.000000
1.414213


output
Case #1:
0.5 0 0
0 0.5 0
0 0 0.5
Case #2:
0.3535533905932738 0.3535533905932738 0
-0.3535533905932738 0.3535533905932738 0
0 0 0.5

*/	
	private Scanner in = new Scanner(System.in);
	private PrintWriter pw = new PrintWriter(System.out);
	
	public static void main(String[] args) {
		(new D_CubicUFO()).solve();
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