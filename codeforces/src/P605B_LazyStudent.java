import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class P605B_LazyStudent {
	// source: http://codeforces.com/profile/BobLee (tuned by me)
/* TEST CASE:
4 4
2 0
1 1
2 1
3 1
 	
 */
	public static class Edge {
		public int u;
		public int v;
		
		public Edge(int u, int v) {
			this.u = u;
			this.v = v;
		}
	}

	public static class UnknownEdge implements Comparable<UnknownEdge> {
		public int weight;
		public byte mstEdgeFlag;
		public int edgeIndex;
		public UnknownEdge(int weight, byte mstEdgeFlag, int edgeIndex) {
			this.weight = weight;
			this.mstEdgeFlag = mstEdgeFlag;
			this.edgeIndex = edgeIndex;
		}
		@Override
		public int compareTo(UnknownEdge o) {
			// ORDER BY weight, mstEdgeFlag DESC
			int wDiff = this.weight - o.weight;
			return wDiff == 0 ? o.mstEdgeFlag - this.mstEdgeFlag : wDiff;
		}
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String textLine;
		String[] splitted;
		Edge[] answer;

		int n; // # vertices
		int m; // # edges
		int i;
		UnknownEdge[] ueArray;
		boolean impossible = false;
		
		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		n = Integer.parseInt(splitted[0]);
		m = Integer.parseInt(splitted[1]);
		ueArray = new UnknownEdge[m];
		
		for (i = 0; i < m; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			ueArray[i] = new UnknownEdge(Integer.parseInt(splitted[0]), Byte.parseByte(splitted[1]), i);
		}

		Arrays.sort(ueArray);
		int mst = 0, nonMst = 0, nonMstLimit = 0;
		int u = 1, v = 2;

		answer = new Edge[m];

		for (UnknownEdge ue : ueArray) {
			if (ue.mstEdgeFlag != 0) {
				nonMstLimit += mst;
				answer[ue.edgeIndex] = new Edge(++mst, n);
			} else {
				if (++nonMst > nonMstLimit) {
					impossible = true;
					break;
				}
				answer[ue.edgeIndex] = new Edge(u, v);
				if (++u == v) {
					u = 1;
					v++;
				}
			}
		}

		if (impossible) {
			System.out.println("-1");
		} else {
			for (i = 0; i < m; i++) {
				System.out.printf("%d %d\n", answer[i].u, answer[i].v);
			}
		}
	}
}
