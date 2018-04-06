import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
/*
C. Pearls in a Row
There are n pearls in a row. Let's enumerate them with integers from 1 to n from the left to the right. The pearl number i has the type ai.

Let's call a sequence of consecutive pearls a segment. Let's call a segment good if it contains two pearls of the same type.

Split the row of the pearls to the maximal number of good segments. Note that each pearl should appear in exactly one segment of the partition.

As input/output can reach huge size it is recommended to use fast input/output methods: for example, prefer to use scanf/printf instead of cin/cout in C++, prefer to use BufferedReader/PrintWriter instead of Scanner/System.out in Java.

Input
The first line contains integer n (1 ≤ n ≤ 3·105) — the number of pearls in a row.

The second line contains n integers ai (1 ≤ ai ≤ 109) – the type of the i-th pearl.

Output
On the first line print integer k — the maximal number of segments in a partition of the row.

Each of the next k lines should contain two integers lj, rj (1 ≤ lj ≤ rj ≤ n) — the number of the leftmost and the rightmost pearls in the j-th segment.

Note you should print the correct partition of the row of the pearls, so each pearl should be in exactly one segment and all segments should contain two pearls of the same type.

If there are several optimal solutions print any of them. You can print the segments in any order.

If there are no correct partitions of the row print the number "-1".

Examples
input
5
1 2 3 4 1
output
1
1 5
input
5
1 2 3 4 5
output
-1
input
7
1 2 1 3 1 2 1
output
2
1 3
4 7

 */
public class P620B_Calculator_PASSED {
	private static BufferedReader br;
	private static PrintWriter pw;
	private static int[] digitSegments = {6, 2, 5, 5, 4, 5, 6, 3, 7, 6};
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String textLine = br.readLine();
		String[] splitted = textLine.split("\\s+");
		int a = Integer.parseInt(splitted[0]);
		int b = Integer.parseInt(splitted[1]);
		int answer = 0;
		for (int i = a; i <= b; i++) {
			int num = i;
			
			while (num > 0) {
				int d = num % 10;
				answer += digitSegments[d];
				num /= 10;
			}
		}
		pw.println(answer);
		pw.close();
	}

}