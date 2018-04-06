import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class RECRECOV_RecipeRecovery_PASSED {
/*TEST CASE:
 
INPUT: 
7
3 3
1 2
2 3
1 3
3 2
1 2
1 3
5 4
1 3
2 3
3 4
2 5
3 0
4 4
1 2
2 1
1 3
1 4
4 5
1 2
2 1
1 3
1 4
4 2
4 4
4 2
1 2
2 1
1 3
OUTPUT:
1
2
2
3
2
1
1	 
*/
	// Using Hungarian Algorithm to find maximum matching (for vertex = 1..N)
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		int testCaseNumber = 0, testCaseCount;
		String textLine;
		String[] splitted;
		int answer;
		int i;

		int N; // # pieces
		int M; // # relations
		int u, v;
		
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			splitted = textLine.split("\\s+");
			N = Integer.parseInt(splitted[0]);
			M = Integer.parseInt(splitted[1]);
			
			HashMap<Integer, ArrayList<Integer>> directedEdgeMap = new HashMap<Integer, ArrayList<Integer>>(N);
			
			for (i = 0; i < M; i++) {
				textLine = br.readLine();
				splitted = textLine.split("\\s+");
				u = Integer.parseInt(splitted[0]);
				v = Integer.parseInt(splitted[1]);
				if (u != v) {
					ArrayList<Integer> adjacentVertices = directedEdgeMap.get(u);
					if (adjacentVertices == null) {
						adjacentVertices = new ArrayList<Integer>();
						directedEdgeMap.put(u, adjacentVertices);
					}
					adjacentVertices.add(v);
				}
			}

//			if (testCaseNumber == 6) {
//				System.gc();
//			}
			answer = N - maximumMatching(N, directedEdgeMap);
			
			pw.printf("%d\n", answer);
		}
		pw.close();
		br.close();
	}

	public static boolean dfsAugmentingPath(HashMap<Integer, ArrayList<Integer>> directedEdgeMap, int[] match, boolean[] visited, int u) {
		ArrayList<Integer> adjacentVertices = directedEdgeMap.get(u);
		if (adjacentVertices != null) {
			for (Integer v : adjacentVertices) {
				if (!visited[v]) {
					visited[v] = true;
					if (match[v] == 0 || dfsAugmentingPath(directedEdgeMap, match, visited, match[v])) {
						match[v] = u;
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public static int maximumMatching(int n, HashMap<Integer, ArrayList<Integer>> directedEdgeMap) {
		int result = 0;
		int[] match = new int[n + 1];
		boolean[] visited = new boolean[n + 1];
		
		for (int u = 1; u <= n; u++) {
			Arrays.fill(visited, false);
			if (dfsAugmentingPath(directedEdgeMap, match, visited, u))
				result++;
		}
		
		return result;
	}
}
