import java.util.Arrays;
import java.util.Scanner;
import java.io.PrintWriter;

public class A_SavingTheUniverseAgain {
	
/* Examples

input
7
1 CS
2 CS
1 SS
6 SCCSSC
2 CC
3 CSCSS
6 CCCCSS

output
Case #1: 1
Case #2: 0
Case #3: IMPOSSIBLE
Case #4: 2
Case #5: 0
Case #6: 5
Case #7: 5

input
1
6 CCCCSS

*/	
	private Scanner in = new Scanner(System.in);
	private PrintWriter pw = new PrintWriter(System.out);
	
	public static void main(String[] args) {
		(new A_SavingTheUniverseAgain()).solve();
	}
	
	public void solve() {
		int T = in.nextInt();
		
		for (int t = 1; t <= T; t++) {
			long D = in.nextLong();
			char[] P = in.next().toCharArray();
			int answer = minAdjacentSwaps(D, P);
			pw.printf("Case #%d: %s\n", t, (answer < 0 ? "IMPOSSIBLE" : String.valueOf(answer)));
		}
	    
		pw.flush();
	}
	
	private static int minAdjacentSwaps(long maxDamage, char[] robotProg) {
		int ret = 0;
		int n = robotProg.length;
		long[] strength = new long[n + 1];
		long[] damage = new long[n + 1];
		boolean impossible = false;
		
		Arrays.fill(strength, 1);
/*
1
6 CCCCSS
 */
		for (int i = 0; !impossible && i < n; i++) {
			char actionCode = robotProg[i];

			if (actionCode == 'S') {
				damage[i + 1] = damage[i] + strength[i];
				strength[i + 1] = strength[i];
				
				if (damage[i + 1] > maxDamage) {
					impossible = true;
					for (int k = n - 1; k > 0; k--) {
						if(robotProg[k - 1] == 'C' && robotProg[k] == 'S') {
							impossible = false;
							
							robotProg[k - 1] = 'S';
							strength[k] >>= 1;
							robotProg[k] = 'C';
							ret++;
							
							if (k - 1 <= i)
								i = k - 1;
							
							i--;
							break;
						}
					}
				}
			} else { // actionCode == 'C'
				damage[i + 1] = damage[i];
				strength[i + 1] = strength[i] << 1;
			}
		}

		return impossible ? -1 : ret;
	}
}