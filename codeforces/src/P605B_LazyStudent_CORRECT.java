import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;

public class P605B_LazyStudent_CORRECT {
	// source: http://codeforces.com/profile/BobLee
	// tag: graph reconstruction
	private static class Edge {
		public int u;
		public int v;
		
		public Edge(int u, int v) {
			this.u = u;
			this.v = v;
		}
	}

	private static class UnknownEdge implements Comparable<UnknownEdge> {
		public int weight;
		public byte mstEdgeFlag;  // 0=no, 1=yes
		public int edgeIndex;
		@Override
		public int compareTo(UnknownEdge o) {
			// ORDER BY weight, mstEdgeFlag DESC 
			return (this.weight != o.weight) ? (this.weight - o.weight) : (o.mstEdgeFlag - this.mstEdgeFlag);
		}
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String textLine;
		String[] splitted;
		Edge[] answer;

		@SuppressWarnings("unused")
		int n; // # vertices
		int m; // # edges
		int i;
		PriorityQueue<UnknownEdge> minHeap;
		boolean impossible = false;
		
		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		n = Integer.parseInt(splitted[0]);
		m = Integer.parseInt(splitted[1]);
		minHeap = new PriorityQueue<UnknownEdge>(m);
		
		for (i = 0; i < m; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			UnknownEdge ue = new UnknownEdge();
			ue.weight = Integer.parseInt(splitted[0]);
			ue.mstEdgeFlag = Byte.parseByte(splitted[1]);
			ue.edgeIndex = i;
			minHeap.add(ue);
		}
		
		int mst = 0, nonMst = 0, total = 0;
		int u = 2, v = 3;

		answer = new Edge[m];
		// pin to node 1 and then extract the MST edge to it
		while (!minHeap.isEmpty()) {
			UnknownEdge ue = minHeap.poll();
			if (ue.mstEdgeFlag != 0) {
				total += mst;
				answer[ue.edgeIndex] = new Edge(1, ++mst + 1);
			} else {
				if (++nonMst > total) {
					impossible = true;
					break;
				}
				answer[ue.edgeIndex] = new Edge(u, v);
				if (++u == v) {
					u = 2;
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
