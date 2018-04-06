import java.util.ArrayDeque;
import java.util.ArrayList;

public class GridGraph {
	public static final float INF = Float.POSITIVE_INFINITY;
	public double normalLength;
	public double diagonalLength;
	public int rowCount;
	public int columnCount;
	
	public class Node {
		public int id;
		private int row;
		private int col;

		public double depth;	// distance from root;
		public boolean canDiagonalDirect;

		public Node(int id, int row, int col, double depth) {
			this.row = row;
			this.col = col;
			this.depth = depth;
			this.canDiagonalDirect = false;
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

		public ArrayList<Node> routeAdjacentNeighborsDownwardDiagonal() {
			final int[][] downwardDiagonalDirs = { {1, 1}, {1, 0}, {0, 1} };
			ArrayList<Node> result = new ArrayList<Node>();
			int nextRow, nextCol;
			
			for (int k = canDiagonalDirect ? 0 : 1; k < 3; k++) {
				nextRow = row + downwardDiagonalDirs[k][0];
				nextCol = col + downwardDiagonalDirs[k][1];
				
				if (nextRow < rowCount && nextCol < columnCount) {
					Node neighbor = gridMatrix[nextRow][nextCol];
					
					if (neighbor.depth == INF) {
						if (k == 0) {
							neighbor.depth = depth + diagonalLength;
							result.add(neighbor);
							break;
						}
	
						neighbor.depth = depth + normalLength;
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
		int id = 0;
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				gridMatrix[i][j] = new Node(id++, i, j, INF);
			}
		}
		
		this.rowCount = rowCount;
		this.columnCount = columnCount;
	}
	
	public double bfsDownwardDiagonalSP(Node src, Node dest) {
		ArrayDeque<Node> Q = new ArrayDeque<Node>(Math.max(rowCount, columnCount));
		
		src.depth = 0;

		if (src != dest) {
			Q.add(src);
			
			while (!Q.isEmpty()) {
				Node p = Q.poll();
				
				for (Node q : p.routeAdjacentNeighborsDownwardDiagonal()) {
					if (q == dest) {
						return dest.depth;
					}
					Q.add(q);
				}
			}
		}
		
		return dest.depth;
	}
}