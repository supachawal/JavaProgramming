import java.util.HashSet;
import java.util.Scanner;
import java.io.PrintWriter;

/* 
 * Examples
input
3 2 30 4
6 14 25 48
output
3
input
123 1 2143435 4
123 11 -5453 141245
output
0
input
123 1 2143435 4
54343 -13 6 124
output
inf
 */
public class P789B_Masha_and_geometric_depression {
	
	private static PrintWriter pw;
	
	public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
		pw = new PrintWriter(System.out);
		int b1 = in.nextInt();
		int q = in.nextInt();
		long l = in.nextLong();
		int m = in.nextInt();
		int i, answer = 0;
		long b = b1;
	
		HashSet<Integer> a = new HashSet<Integer>();
		HashSet<Integer> B = new HashSet<Integer>();
		B.add(b1);
		
		for (i = 0; i < m; i++) {
			a.add(in.nextInt());
		}
		boolean[] isBanned = new boolean[2];
		int nRepeat = 0;
		while (Math.abs(b) <= l) {
			boolean toBan = a.contains((int)b);	
			if (!toBan) {
				answer++;
			}

			b *= q;
			if (B.contains((int)b)) {
				isBanned[nRepeat] = toBan;
				if (++nRepeat > 1)
					break;
			}
			
			B.add((int)b);
		}

		if (answer > 0 && nRepeat > 1 && (!isBanned[0] || !isBanned[1])) {
			pw.printf("inf\n", answer);
		} else {
			pw.printf("%d\n", answer);
		}
		in.close();
		pw.close();
	}
}