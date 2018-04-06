import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class JAN16_3_DEVPERF_Perfume {
/*
Example

Input:
3
2 2
*.
..
3 4
.*..
***.
.*..
6 5
.*...
*****
.*...
*****
....*
...*.

Output:
1
2
5

Input:
3
1 1
*
1 1
.
4 5
.....
.....
.....
.....

Output:
1
0
0

Input:
1
4 7
.*****.
.*****.
..****.
.******

Output:
4
*/
	private static PrintWriter pw;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);

		int testCaseNumber = 0, testCaseCount;
		String textLine;
		String[] splitted;
		int answer;
		
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			splitted = textLine.split("\\s+");
			int rowCount = Integer.parseInt(splitted[0]);
			int columnCount = Integer.parseInt(splitted[1]);
			int r1 = Integer.MAX_VALUE, c1 = Integer.MAX_VALUE, r2 = -1, c2 = -1;
			for (int i = 0; i < rowCount; i++) {
				textLine = br.readLine();
				for (int j = 0; j < columnCount; j++) {
					if (textLine.charAt(j) == '*') {
						r1 = Math.min(r1, i);
						c1 = Math.min(c1, j);
						r2 = Math.max(r2, i);
						c2 = Math.max(c2, j);
					}
				}
			}

			int maxDim = Math.max(r2 - r1 + 1, c2 - c1 + 1);
			answer = maxDim <= 0 ? 0 : 1 + maxDim / 2;
			pw.printf("%d\n", answer);
		}
		pw.close();
	}
}
