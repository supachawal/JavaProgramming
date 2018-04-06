import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class P1119_Metro {
/*
SAMPLE ...

INPUT:
3 2
3
1 1
3 2
1 2

OUTPUT:
383

*/
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		String textLine;
		String[] splitted;
//NOT USED:		int answer;
		
		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		
		int N = Integer.parseInt(splitted[0]) + 1;
		int M = Integer.parseInt(splitted[1]) + 1;
		int i;
		GridGraph G = new GridGraph(M, N);
		G.normalLength = 100;
		G.diagonalLength = 141.4213562373095;

		textLine = br.readLine();
		int K = Integer.parseInt(textLine);

		for (i = 0; i < K; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			int diagI = Integer.parseInt(splitted[1]) - 1;
			int diagJ = Integer.parseInt(splitted[0]) - 1;
			G.gridMatrix[diagI][diagJ].canDiagonalDirect = true;
		}
		
		pw.printf("%d", Math.round(G.bfsDownwardDiagonalSP(G.gridMatrix[0][0], G.gridMatrix[M - 1][N - 1])));
		pw.close();
	}

	public static class GridGraph {
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

			public ArrayList<Node> routedNeighborNodes() {
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
		
		public double bfsDownwardDiagonalSP(Node src, Node dest) {
			ArrayDeque<Node> Q = new ArrayDeque<Node>(Math.max(rowCount, columnCount));
			
			src.depth = 0;

			if (src != dest) {
				Q.add(src);
				
				while (!Q.isEmpty()) {
					Node p = Q.poll();
					
					for (Node q : p.routedNeighborNodes()) {
						if (q == dest) {
							return dest.depth;
						}
						Q.add(q);
					}
				}
			}
			
			return dest.depth;
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
		
	}
}

