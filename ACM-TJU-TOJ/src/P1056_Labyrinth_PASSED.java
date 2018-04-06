import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

class P1056_Labyrinth_PASSED {
/*
Sample Input
2
3 3
###
#.#
###
7 6
#######
#.#.###
#.#.###
#.#.#.#
#.....#
#######

Sample Output
Maximum rope length is 0.
Maximum rope length is 8.

*/
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		String textLine;
		String[] splitted;
		int T, t;
		int rowCount, columnCount;
		int i, j;
		T = Integer.parseInt(br.readLine());
		for (t = 0; t < T; t++) {
			if ((textLine = br.readLine()) == null) {
				break;
			}
			splitted = textLine.split("\\s+");
			columnCount = Integer.parseInt(splitted[0]);
			rowCount = Integer.parseInt(splitted[1]);
			GridGraph G = new GridGraph(rowCount, columnCount);
			
			for (i = 0; i < rowCount; i++) {
				textLine = br.readLine();
				for (j = 0; j < columnCount; j++) {
					if (textLine.charAt(j) == '#') {
						G.gridMatrix[i][j].isBlocking = true;
					}
				}
			}
			
			pw.printf("Maximum rope length is %d.\n", G.computeDiameterUsingBFS());
		}
		pw.close();
	}

	public static class GridGraph {
		public static final int INF = 999999999;
		public int rowCount;
		public int columnCount;
		
		public class Node {
			public int id;
			private int row;
			private int col;

			public int depth;	// distance from root;
			public boolean isBlocking;	// true = obstacle
			private Node bfsRootNode;

			public Node(int id, int row, int col, int depth) {
				this.row = row;
				this.col = col;
				this.depth = depth;
				this.isBlocking = false;
			}

			@Override
			public int hashCode() {
				return id;
			}
			@Override
			public boolean equals(Object obj) {
				return id == ((Node)obj).id;
			}
			@Override
			public String toString() {
				return String.format("%d(%d,%d)", id, row, col);
			}

			public ArrayList<Node> routeAdjacentNeighborsAllDirections(Node bfsRootNode) {
				final int[][] allDirs = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };
				ArrayList<Node> result = new ArrayList<Node>();
				int nextRow, nextCol;
				
				for (int k = 0; k < 4; k++) {
					nextRow = row + allDirs[k][0];
					nextCol = col + allDirs[k][1];
					
					if (nextRow >= 0 && nextRow < rowCount && nextCol >= 0 && nextCol < columnCount) {
						Node neighbor = gridMatrix[nextRow][nextCol];
						
						if (neighbor.bfsRootNode != bfsRootNode && !neighbor.isBlocking) {
							neighbor.bfsRootNode = bfsRootNode;
							neighbor.depth = depth + 1;
							result.add(neighbor);
						}
					}
				}
				return result;
			}
		}

		public Node[][] gridMatrix;

		public GridGraph(int rowCount, int columnCount) {
			gridMatrix = new Node[rowCount][columnCount];
			int id = 0, i, j;
			for (i = 0; i < rowCount; i++) {
				for (j = 0; j < columnCount; j++) {
					gridMatrix[i][j] = new Node(id++, i, j, INF);
				}
			}
			
			this.rowCount = rowCount;
			this.columnCount = columnCount;
		}
		
		public int computeDiameterUsingBFS() {
			ArrayBlockingQueue<Node> Q = new ArrayBlockingQueue<Node>(rowCount + columnCount);
			Node p = null, root = null;
			int i, j;

			// step 1: start from arbitrary nonblocking node, flood BFS to find the farthest frontier
			for (i = 0; i < rowCount && root == null; i++) {
				for (j = 0; j < columnCount; j++) {
					if (!gridMatrix[i][j].isBlocking) {
						root = gridMatrix[i][j];
						break;
					}
				}
			}
			
			root.depth = 0;
			root.bfsRootNode = root;
			Q.add(root);
			while (!Q.isEmpty()) {
				p = Q.poll();
				for (Node q : p.routeAdjacentNeighborsAllDirections(root)) {
					Q.add(q);
				}
			}
			root = p;

			// step 2: now the farthest frontier is new root
			root.depth = 0;
			root.bfsRootNode = root;
			Q.add(root);
			while (!Q.isEmpty()) {
				p = Q.poll();
				for (Node q : p.routeAdjacentNeighborsAllDirections(root)) {
					Q.add(q);
				}
			}
			
			return p.depth;
		}
	}
}

