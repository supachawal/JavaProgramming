import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class JAN16_5_SEAKAM_Salesman_SLOW {
/*
Example

Input:
2
4 3
1 2
2 3
3 4
2 1
1 2

Output:
2
0
*/

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		int testCaseNumber = 0, testCaseCount;
		String textLine;
		String[] splitted;
		long answer;
		int N, M, i, u, v;
		boolean[][] P;
//NOT USED:		int[] permutationBuffer;
		boolean[] visited;
		
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			splitted = textLine.split("\\s+");
			N = Integer.parseInt(splitted[0]);
			M = Integer.parseInt(splitted[1]);
			P = new boolean[N+1][N+1];
//NOT USED:			permutationBuffer = new int[N];
			visited = new boolean[N + 1];
			for (i = 0; i < M; i++) {
				textLine = br.readLine();
				splitted = textLine.split("\\s+");
				u = Integer.parseInt(splitted[0]);
				v = Integer.parseInt(splitted[1]);
				P[u][0] = true;
				P[u][v] = true;
				P[v][u] = true;
			}
			
			answer = permutationCount(visited, P, 0, N - 1);
			pw.printf("%d\n", answer);
		}
		pw.close();
	}

	private static long permutationCount(boolean[] visited, boolean[][] P, int lastVertex, int k) {
		if (k < 0) {
			return 1;
		}
		long result = 0;
		for (int u = P.length - 1; u > 0; u--) {
			if (!visited[u] && !P[lastVertex][u]) {
				visited[u] = true;
				result += permutationCount(visited, P, u, k - 1);
				visited[u] = false;
			}
		}
		return result % 1000000007;
	}
}
