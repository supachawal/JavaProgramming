import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;

public class P616C_TheLabyrinth_SLOW {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		String textLine = br.readLine();
		String[] splitted = textLine.split("\\s+");
		int nRows = Integer.parseInt(splitted[0]);
		int nCols = Integer.parseInt(splitted[1]);
		
		GridGraph G = new GridGraph(nRows, nCols);
		
		for (int i = 0; i < nRows; i++) {
			textLine = br.readLine();
			for (int j = 0; j < nCols; j++) {
				if (textLine.charAt(j) == '*') {
					G.gridMatrix[i][j].isBlocking = true;
				}
			}
		}
		
		G.computeConnectedComponentSizeUsingBFS();
		
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				GridGraph.Node p = G.gridMatrix[i][j];
				pw.printf("%s", p.isBlocking ? p.connectedComponentSize % 10 : ".");
				//DEBUG: pw.printf(" %3d", p.connectedComponentSize);
			}
			pw.println();
		}
		pw.close();
	}
	private static class GridGraph {
		public static final int INF = 999999999;
		public int rowCount;
		public int columnCount;
		public Node[][] gridMatrix;
		public static final int[][] orthogonalDirs = { {-1, 0}, {1, 0}, {0, -1}, {0, 1},
			   };
		@SuppressWarnings("unused")
		public static final int[][] allDirs = { {-1, 0}, {1, 0}, {0, -1}, {0, 1},
				 {-1, -1}, {-1, 1}, {1, -1}, {1, 1} 
			   };
		
		public class Node {
			public int id;
			private int row;
			private int col;

			public int depth;	// distance from root;
			public boolean isBlocking;	// true = obstacle
			public Node bfsRootNode;
			public int messageCost;
			public int connectedComponentSize;
			

			public Node(int id, int row, int col, int depth) {
				this.id = id;
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

			public ArrayList<Node> routeAdjacentNeighbors(Node bfsRootNode, int[][] directions) {
				ArrayList<Node> result = new ArrayList<Node>();
				int nextRow, nextCol;
				
				for (int k = directions.length - 1; k >= 0; k--) {
					nextRow = row + directions[k][0];
					nextCol = col + directions[k][1];
					
					if (nextRow >= 0 && nextRow < rowCount && nextCol >= 0 && nextCol < columnCount) {
						Node neighbor = gridMatrix[nextRow][nextCol];
						
						if (!neighbor.isBlocking && neighbor.bfsRootNode != bfsRootNode) {
							neighbor.bfsRootNode = bfsRootNode;
							neighbor.depth = depth + 1;
							bfsRootNode.messageCost++;
							result.add(neighbor);
						}
					}
				}
				return result;
			}

			public ArrayList<Node> getAdjacentNeighbors(int[][] directions) {
				ArrayList<Node> result = new ArrayList<Node>();
				int nextRow, nextCol;
				
				for (int k = directions.length - 1; k >= 0; k--) {
					nextRow = row + directions[k][0];
					nextCol = col + directions[k][1];
					
					if (nextRow >= 0 && nextRow < rowCount && nextCol >= 0 && nextCol < columnCount) {
						Node neighbor = gridMatrix[nextRow][nextCol];
						
						if (!neighbor.isBlocking) {
							result.add(neighbor);
						}
					}
				}
				return result;
			}
		}

		public GridGraph(int rowCount, int columnCount) {
			gridMatrix = new Node[rowCount][columnCount];
			int id = 0;
			for (int i = 0; i < rowCount; i++) {
				for (int j = 0; j < columnCount; j++) {
					gridMatrix[i][j] = new Node(id++, i, j, INF);
				}
			}
			
			this.rowCount = rowCount;
			this.columnCount = columnCount;
		}
		
		public void computeConnectedComponentSizeUsingBFS() {
			ArrayDeque<Node> Q = new ArrayDeque<Node>();
			ArrayList<Node> connectedNodes = new ArrayList<Node>();
			Node p = null, root = null;
			int i, j;

			for (i = 0; i < rowCount && root == null; i++) {
				for (j = 0; j < columnCount; j++) {
					p = gridMatrix[i][j];
					if (!p.isBlocking && p.bfsRootNode == null) {
						root = p;
						break;
					}
				}
			}

			while (root != null) {
				connectedNodes.clear();
				root.bfsRootNode = root;
				root.messageCost = 0;
				Q.add(root);
				while (!Q.isEmpty()) {
					p = Q.poll();
					connectedNodes.add(p);
					Q.addAll(p.routeAdjacentNeighbors(root, orthogonalDirs));
				}
	
				int ccSize = root.messageCost + 1;
				for (Node a : connectedNodes) {
					a.connectedComponentSize = ccSize;
				}

				root = null;
				for (i = 0; i < rowCount && root == null; i++) {
					for (j = 0; j < columnCount; j++) {
						p = gridMatrix[i][j];
						if (!p.isBlocking && p.bfsRootNode == null) {
							root = p;
							break;
						}
					}
				}
			}

			HashSet<Node> bfsRootNodeSet = new HashSet<Node>();
			for (i = 0; i < rowCount; i++) {
				for (j = 0; j < columnCount; j++) {
					p = gridMatrix[i][j];
					if (p.isBlocking) {
						p.connectedComponentSize = 1;
						bfsRootNodeSet.clear();
						for (Node q : p.getAdjacentNeighbors(orthogonalDirs)) {
							if (bfsRootNodeSet.add(q.bfsRootNode)) {
								p.connectedComponentSize += q.connectedComponentSize;
							}
						}
					}
				}
			}
		}
	}
}