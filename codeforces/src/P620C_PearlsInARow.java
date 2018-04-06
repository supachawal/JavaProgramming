import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
/* TEST CASE:
INPUT:
7
1 2 1 3 1 2 1
OUTPUT:
2
1 3
4 7

Input
9
1 2 1 2 1 2 1 2 1
Output
1
1 9
Answer
3
1 3
4 6
7 9

Input
10
1 0 0 1 1 1 1 0 0 1
Output
3
1 4
5 6
7 10
Answer
4
1 3
4 5
6 7
8 10

 */
public class P620C_PearlsInARow {
	private static BufferedReader br;
	private static PrintWriter pw;
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String textLine = br.readLine();
		int n = Integer.parseInt(textLine);
		int i;
		HashSet<Integer> pearlTypeSet = new HashSet<Integer>();
		ArrayList<Integer> partitions = new ArrayList<Integer>();
		partitions.add(0);
		textLine = br.readLine();
		String[] splitted = textLine.split("\\s+");

		for (i = 0; i < n; i++) {
			if (!pearlTypeSet.add(Integer.valueOf(splitted[i]))) {
				pearlTypeSet.clear();
				partitions.add(i + 1);
			}
		}
		
		int m = partitions.size() - 1;
		if (m == 0) {
			m = -1;
		} else {
			if (partitions.get(m) < n) {
				partitions.set(m, n);
			}
		}
		
		pw.println(m);
		for (i = 0; i < m; i++) {
			pw.printf("%d %d\n", partitions.get(i) + 1, partitions.get(i + 1));
		}

		pw.close();
	}
}