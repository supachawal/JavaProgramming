import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Arrays;

class GALACTIK_GalactikFootball {
/*
Example

Input:
01234567890

Output:
1

Input:
012134444444443

Output:
4
 */
	// Using BFS to find shortest steps
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		String textLine;
		int answer;
		int i, n;

		textLine = br.readLine();
		n = textLine.length();
		int destIndex = n - 1;
		int[] distance = new int[n];
		Arrays.fill(distance, -1);
		boolean[] warped = new boolean[10]; 
		
		ArrayDeque<Integer> Q = new ArrayDeque<Integer>(n);
		distance[0] = 0;
		Q.add(0);

		
		while (!Q.isEmpty()) {
			int p = Q.poll().intValue();
			
			if (p == destIndex) {
				break;
			}
				
			if (p + 1 < n && distance[p + 1] < 0) {
				distance[p + 1] = distance[p] + 1;
				Q.add(p + 1);
			}
			if (p - 1 >= 0 && distance[p - 1] < 0) {
				distance[p - 1] = distance[p] + 1;
				Q.add(p - 1);
			}
			char c = textLine.charAt(p);
			if (!warped[c - '0']) {
				warped[c - '0'] = true;
				for (i = n - 1; i > 0; i--) {
					if (distance[i] < 0 && textLine.charAt(i) == c) {
						distance[i] = distance[p] + 1;
						Q.add(i);
					}
				}
			}
		}
		
		answer = distance[destIndex];
//			if (testCaseNumber == 6) {
//				System.gc();
//			}
		
		pw.printf("%d\n", answer);
		pw.close();
		br.close();
	}
}
