import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/* 
 * Examples
input
5 4
1
2
1
3
3
4 3
1 1
2 6
1 5
output
1
7
2
3
6
8
4
5
9

input
10 10
2
2
2
3
1
1
1
2
3
1
2 3
1 8
3 11
2 2
2 8
2 10
1 5
1 14
3 12
2 18
output
1
2
14
18
20
3
11
13
4
5
17
6
7
8
15
12
19
9
10
16

 */
public class Zooma1 {
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) throws IOException {
		PrintWriter pw = new PrintWriter(System.out);
		String[] splitted = br.readLine().split("\\s+");
		int n = Integer.parseInt(splitted[0]);
		int m = Integer.parseInt(splitted[1]);
		int nTotal = n + m;
		int[] next = new int[nTotal];
		int i, j, k;
		
		for (i = 0; i < n; i++) {
			next[i] = i + 1;
			br.readLine();	// just walk pass
		}
		next[n - 1] = -1;
	
		for (j = 0; j < m; j++) {
			splitted = br.readLine().split("\\s+");
			k = Integer.parseInt(splitted[1]) - 1;
			
			// insert to linked list in array
			next[n + j] = next[k];
			next[k] = n + j;
		}
		
		i = 0;
		while (i >= 0) {
			pw.println(i + 1);
			i = next[i];
		}
		
		pw.close();
	}
}