import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;

class JAN16_3_DEVPERF_Perfume_BUG {
/*
Example

Input:
3
2 2
*.
..
3 4
.*..
***.
.*..
6 5
.*...
*****
.*...
*****
....*
...*.

Output:
1
2
5

Input:
3
1 1
*
1 1
.
4 5
.....
.....
.....
.....

Output:
1
0
0

Input:
1
4 7
.*****.
.*****.
..****.
.******

Output:
4
*/
	private static PrintWriter pw;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);

		int testCaseNumber = 0, testCaseCount;
		String textLine;
		String[] splitted;
		int answer;
		
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			splitted = textLine.split("\\s+");
			int rowCount = Integer.parseInt(splitted[0]);
			int columnCount = Integer.parseInt(splitted[1]);
			GridGraph G = new GridGraph(rowCount, columnCount);
			
			for (int i = 0; i < rowCount; i++) {
				textLine = br.readLine();
				for (int j = 0; j < columnCount && j < textLine.length(); j++) {
					if (textLine.charAt(j) == '.') {
						G.gridMatrix[i][j].isBlocking = true;
					}
				}
			}

			answer = G.computeFastestSpreadUsingBFS();
			pw.printf("%d\n", answer);
		}
		pw.close();
	}
	
	private static class GridGraph {
		public static final int INF = 999999999;
		public int rowCount;
		public int columnCount;
		public Node[][] gridMatrix;
		private static final int[][] allDirs = { {-1, 0}, {1, 0}, {0, -1}, {0, 1},
												 {-1, -1}, {-1, 1}, {1, -1}, {1, 1} 
											   };
		
		public class Node {
			public int id;
			private int row;
			private int col;

			public int depth;	// distance from root;
			public boolean isBlocking;	// true = obstacle
			public Node bfsRootNode;

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

			public ArrayList<Node> routeAllDirections(Node bfsRootNode) {
				ArrayList<Node> result = new ArrayList<Node>();
				int nextRow, nextCol;
				
				for (int k = allDirs.length - 1; k >= 0; k--) {
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
			public ArrayList<Node> routeAllDirectionsCollectingFrontiers(Node bfsRootNode, HashSet<Node> frontiers) {
				ArrayList<Node> result = new ArrayList<Node>();
				int nextRow, nextCol;
				
				for (int k = allDirs.length - 1; k >= 0; k--) {
					nextRow = row + allDirs[k][0];
					nextCol = col + allDirs[k][1];
					
					if (nextRow >= 0 && nextRow < rowCount && nextCol >= 0 && nextCol < columnCount) {
						Node neighbor = gridMatrix[nextRow][nextCol];
						
						if (neighbor.isBlocking) {
							frontiers.add(this);
						} else if (neighbor.bfsRootNode != bfsRootNode) {
							neighbor.bfsRootNode = bfsRootNode;
							result.add(neighbor);
						}
					} else {
						frontiers.add(this);
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
		
		public int computeFastestSpreadUsingBFS() {
			ArrayDeque<Node> Q = new ArrayDeque<Node>();
			Node dummySeeker = new Node(-1, -1, -1, 0);
			Node p = null, root = null;
			int i, j;

			// step 1: start from arbitrary nonblocking node, flood BFS to find the frontiers that are adjacent to blocking nodes or boundaries
			for (i = 0; i < rowCount && root == null; i++) {
				for (j = 0; j < columnCount; j++) {
					if (!gridMatrix[i][j].isBlocking) {
						root = gridMatrix[i][j];
						break;
					}
				}
			}
			
			if (root == null)
				return 0;
			
			HashSet<Node> frontiers = new HashSet<Node>();
			root.bfsRootNode = dummySeeker;
			Q.add(root);
			while (!Q.isEmpty()) {
				p = Q.poll();
				Q.addAll(p.routeAllDirectionsCollectingFrontiers(dummySeeker, frontiers));
			}

			// step 2: find the fastest spread from each frontier
			int minFarthestDepth = Integer.MAX_VALUE;
//FOR DEBUG:			Node fastestSpreadingFrontier = null; 
			for (Node frontier : frontiers) {
				frontier.depth = 0;
				frontier.bfsRootNode = frontier;
				Q.add(frontier);
				while (!Q.isEmpty()) {
					p = Q.poll();
					if (p.depth < minFarthestDepth) {
						Q.addAll(p.routeAllDirections(frontier));
					}
				}
				
				if (minFarthestDepth > p.depth) {
					minFarthestDepth = p.depth;
//FOR DEBUG:					fastestSpreadingFrontier = frontier;
				}
			}
			
//FOR DEBUG:			pw.printf("Fastest frontier = %s\n", fastestSpreadingFrontier);

			return minFarthestDepth + 1;
		}
	}
}
