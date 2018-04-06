import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class P1106_TwoTeams_BUG {
/*
SAMPLE ...

INPUT:
7
2 3 0
3 1 0
1 2 4 5 0
3 0
3 0
7 0
6 0

OUTPUT:
4
2 4 5 6
*/
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		String textLine;
		String[] splitted;
		StringBuilder answer = new StringBuilder();
		
		textLine = br.readLine();
		int N = Integer.parseInt(textLine);
		int u, v;
		
		HashMap<Integer, ArrayList<Integer>> directedEdgeMap = new HashMap<Integer, ArrayList<Integer>>(N);
		for (u = 1; u <= N; u++) {
			ArrayList<Integer> adjacentVertices = new ArrayList<Integer>();
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			for (String vStr : splitted) {
				v = Integer.parseInt(vStr);
				adjacentVertices.add(v);
			}
			directedEdgeMap.put(u, adjacentVertices);
		}

		int[] matching = new int[N + 1];
		List<Integer> firstGroup = hungarianAlgorithm(N, directedEdgeMap, matching);
		if (firstGroup.isEmpty() || firstGroup.size() == N) {
			pw.printf("0\n");
		} else {
			for (Integer vInt : firstGroup) {
				if (answer.length() > 0)
					answer.append(" ");
				answer.append(vInt);
			}
			pw.printf("%d\n%s\n", firstGroup.size(), answer);
		}
		
		pw.close();
	}

	public static boolean dfsAugmentingPath(HashMap<Integer, ArrayList<Integer>> directedEdgeMap, int[] matching, boolean[] visited, int u) {
		ArrayList<Integer> adjacentVertices = directedEdgeMap.get(u);
		if (adjacentVertices != null) {
			for (Integer v : adjacentVertices) {
				if (!visited[v]) {
					visited[v] = true;
					if (matching[v] == 0 || dfsAugmentingPath(directedEdgeMap, matching, visited, matching[v])) {
						matching[v] = u;
						return true;
					}
				}
			}
		}

		return false;
	}
	
	public static List<Integer> hungarianAlgorithm(int n, HashMap<Integer, ArrayList<Integer>> directedEdgeMap, int[] matching) {
		List<Integer> firstGroup = new ArrayList<Integer>();
		boolean[] visited = new boolean[n + 1];
		int u, v;
		
		for (u = 1; u <= n; u++) {
			Arrays.fill(visited, false);
			dfsAugmentingPath(directedEdgeMap, matching, visited, u);
		}

		Arrays.fill(visited, false);
		for (u = 1; u <= n; u++) {
			if (!visited[u] && (v = matching[u]) > 0) {
				visited[u] = true;
				visited[v] = true;
				firstGroup.add(v);
			}
		}	
		for (u = 1; u <= n; u++) {
			if (!visited[u]) {
				boolean found = false;
				for (Integer d : directedEdgeMap.get(u)) {
					if (firstGroup.contains(d)) {
						found = true;
						break;
					}
				}
				if (!found) {
					firstGroup.add(u);
				}
			}
		}		
		
		return firstGroup;
	}
}
