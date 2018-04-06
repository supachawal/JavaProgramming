import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

class P1705_SensorNetwork {
	//TAG: Unit disk graph (UDG) construction and find the maximum clique
	
/*test
INPUT:
4 1
0 0
0 1
1 0
1 1
OUTPUT:
2
1 2

INPUT:
5 20
0 0
0 2
100 100
100 110
100 120
OUTPUT:
3
4 3 5

INPUT:
3 1
1 1
10 10
20 20
OUTPUT:
1
1

INPUT:
6 20
98 110
90 95
75 110
90 120
93 136
96 135
OUTPUT:
3
4 5 6

INPUT:
24 20
98 110
90 95
80 80
65 70
55 55
36 50
18 50
7 34
-10 25
-30 25
96 136
-40 40
-40 60
-30 75
-20 90
-10 104
1 120
20 120
35 120
50 120
65 120
78 120
90 120
98 135
OUTPUT:
3
11 23 24

INPUT:
48 20
98 110
90 95
80 80
65 70
55 55
36 50
18 50
7 34
-10 25
-30 25
96 136
-40 40
-40 60
-30 75
-20 90
-10 104
1 120
20 120
35 120
50 120
65 120
78 120
90 120
98 135
98 110
90 95
80 80
65 70
55 55
36 50
18 50
7 34
-10 25
-30 25
96 136
-40 40
-40 60
-30 75
-20 90
-10 104
1 120
20 120
35 120
50 120
65 120
78 120
90 120
98 135

OUTPUT:
6
23 11 47 24 35 48 
computationCost = 144 iterations

 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		String textLine;
		String[] splitted;
//		int answer;
		int i;
		
		int nodeCount;
		
		while ((textLine = br.readLine()) != null) {
			if (textLine.length() == 0) {
				break;
			}
			splitted = textLine.split("\\s+");
			nodeCount = Integer.parseInt(splitted[0]);
			UnweightedUndirectedGraph.Node.radius = Integer.parseInt(splitted[1]);
			UnweightedUndirectedGraph.GridCell.maxGridX = 10000 / UnweightedUndirectedGraph.Node.radius;

			if (nodeCount > 0) {
				UnweightedUndirectedGraph nw = new UnweightedUndirectedGraph(nodeCount);
			
				for (i = 0; i < nodeCount; i++) {
					textLine = br.readLine();
					splitted = textLine.split("\\s+");
					nw.nodes[i] = new UnweightedUndirectedGraph.Node(i + 1, Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]));
				}
	
				nw.constructUsingUDG();
				UnweightedUndirectedGraph.computationCost = 0;
				List<Integer> maximumClique = nw.computeMaximumClique();
				
				pw.printf("%d\n", maximumClique.size());
				for (Integer nodeIndex : maximumClique) {
					pw.printf("%d ", nw.nodes[nodeIndex].id);
				}
				
				pw.println();
//TEMP:				pw.printf("computationCost = %d iterations\n", UnweightedUndirectedGraph.computationCost);
			}
		}
		
		pw.close();
	}

	public static class UnweightedUndirectedGraph {
		public static class Node {
			public int id;
			public int posX;
			public int posY;
			
			public static int radius;
			
			public Node(int id, int posX, int posY) {
				this.id = id;
				this.posX = posX;
				this.posY = posY;
			}
			
			public boolean canConnect(Node another) {
				int dX = posX - another.posX;
				int dY = posY - another.posY;
				return dX*dX + dY*dY <= radius*radius;
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
				return String.format("%d(%d,%d)", id, posX, posY);
			}
		}

		public static class Edge {
			public Node node1;
			public Node node2;
			
			public Edge(Node node1, Node node2) {
				this.node1 = node1;
				this.node2 = node2;
			}

			@Override
			public int hashCode() {
				return node1.id + node2.id;
			}
			@Override
			public boolean equals(Object obj) {
				return node1.equals(((Edge)obj).node1) && node2.equals(((Edge)obj).node2)
						|| node1.equals(((Edge)obj).node2) && node2.equals(((Edge)obj).node1);
			}
			@Override
			public String toString() {
				return String.format("%s<->%s", node1, node2);
			}
			
		}

		public static class QuickUnionFind {
			private int[] parentIndex;
			public	int[] connectedComponentSize;

			public QuickUnionFind(int n) {
				parentIndex = new int[n];
				connectedComponentSize = new int[n];
				for (int i = 0; i < n; i++) {
					parentIndex[i] = i;
					connectedComponentSize[i] = 1;
				}
			}

			public boolean areNodesConnected(int nodeIndex1, int nodeIndex2) {
				return root(nodeIndex1) == root(nodeIndex2);
			}

			public void unite(int nodeIndex1, int nodeIndex2) {
				int i = root(nodeIndex1);
				int j = root(nodeIndex2);

				if (i != j) {
					if (connectedComponentSize[i] < connectedComponentSize[j]) {	// weighted tree size improvement
						parentIndex[i] = j;
						connectedComponentSize[j] += connectedComponentSize[i];
					} else {
						parentIndex[j] = i;
						connectedComponentSize[i] += connectedComponentSize[j];
					}
				}
			}

			public int root(int i) {
				while (i != parentIndex[i]) {
					parentIndex[i] = parentIndex[parentIndex[i]];	// path compression improvement
					i = parentIndex[i];
				}
				return i;
			}
		}
		
		public static class GridCell {
			public static int maxGridX;

			public int gridX;
			public int gridY;
			public boolean processedUdgAlready;
			public ArrayList<Node> nodes;
			
			public GridCell() {
				gridX = 0;
				gridY = 0;
				processedUdgAlready = false;
				nodes = new ArrayList<Node>();
			}

			public void assign(Node node) {
				gridX = node.posX / Node.radius;
				gridY = node.posY / Node.radius;
			}
			
			@Override
			public int hashCode() {
				return gridY * (maxGridX + 1) + gridX;
			}

			@Override
			public boolean equals(Object obj) {
				return gridX == ((GridCell)obj).gridX && gridY == ((GridCell)obj).gridY;
			}
			
			private static int[][] eightNeighborDir = 
				{
					{-1, 1},  {0, 1},  {1, 1}, 
					{-1, 0},           {1, 0}, 
					{-1, -1}, {0, -1}, {1, -1} 
				};

			public void getProximateCell(GridCell another, int index) {
				another.gridX = gridX + eightNeighborDir[index][0];
				another.gridY = gridY + eightNeighborDir[index][1];
			}
		}

		private Boolean _isConnectedGraph;
		private Boolean _isCompleteGraph;
		private HashMap<Integer, Integer> _nodeIndexMap;
		public Node[] nodes;
		public ArrayList<Edge> edges;					// this may be shared along subgraph (no need to recreate)

		public int[] nodesDegrees, backupNodesDegrees;						// null by default, need to run recomputeDegreesAndAdjacencyMatrix first
		int prunedNodeCount;
		public int[][] adjacencyMatrix, backupAdjacencyMatrix;					// parameterized with nodeIndex
		public int[] nodesIndicesOrderByDegreeDesc;

		public UnweightedUndirectedGraph(int nodeCount) {
			nodes = new Node[nodeCount];
		}
		
		public HashMap<Integer, Integer> getNodeIndexMap() {
			if (_nodeIndexMap == null) {
				_nodeIndexMap = new HashMap<Integer, Integer>();
				
				for (int i = 0, n = nodes.length; i < n; i++)
					_nodeIndexMap.put(nodes[i].id, i);
			}
			return _nodeIndexMap;
		}

		public void rebuildDegreesAndAdjacencyMatrix() {
			int i, n = nodes.length;
			nodesDegrees = new int[n];	//zero by default
			adjacencyMatrix = new int[n][n];//zero by default
			HashMap<Integer, Integer> nodeIndexMap = getNodeIndexMap();

			prunedNodeCount = 0;
			
			for (Edge e : edges) {
				Integer nodeIndex1 = nodeIndexMap.get(e.node1.id);
				Integer nodeIndex2 = nodeIndexMap.get(e.node2.id);
				
				if (nodeIndex1 != null && nodeIndex2 != null) {
					if (adjacencyMatrix[nodeIndex1][nodeIndex2] == 0) {
						nodesDegrees[nodeIndex1]++;
						adjacencyMatrix[nodeIndex1][nodeIndex2] = 1;
					}
						
					if (adjacencyMatrix[nodeIndex2][nodeIndex1] == 0) {
						nodesDegrees[nodeIndex2]++;
						adjacencyMatrix[nodeIndex2][nodeIndex1] = 1;
					}
				}
			}
			
			backupNodesDegrees = nodesDegrees.clone();
			backupAdjacencyMatrix = new int[n][n];
			for (i = 0; i < n; i++) {
				System.arraycopy(adjacencyMatrix[i], 0, backupAdjacencyMatrix[i], 0, n);
			}

			nodesIndicesOrderByDegreeDesc = new int[n];
			TreeMap<Integer, ArrayList<Integer>> tree = new TreeMap<Integer, ArrayList<Integer>>((a, b) -> b.compareTo(a));

			for (i = 0; i < n; i++) {
				ArrayList<Integer> nodeIndexList = tree.get(nodesDegrees[i]);
				
				if (nodeIndexList == null) {
					nodeIndexList = new ArrayList<Integer>();
					tree.put(nodesDegrees[i], nodeIndexList);
				}
				
				nodeIndexList.add(i);
			}
			
			i = 0;
			for (Entry<Integer, ArrayList<Integer>> e : tree.entrySet()) {
				ArrayList<Integer> nodeIndexList = e.getValue();
				for (Integer nodeIndex : nodeIndexList) {
					nodesIndicesOrderByDegreeDesc[i++] = nodeIndex;
				}
			}
		}
		
		public void resetGraphProperties() {
			_isConnectedGraph = null;
			_isCompleteGraph = null;
		}
		
		public boolean isConnectedGraph() {
			if (_isConnectedGraph == null) {
				int n = nodes.length;

				if (n == 0) {
					return false;
				}

				QuickUnionFind quf = new QuickUnionFind(n);
				HashMap<Integer, Integer> nodeIndexMap = getNodeIndexMap();
				
				for (Edge e : edges) {
					Integer nodeIndex1 = nodeIndexMap.get(e.node1.id);
					Integer nodeIndex2 = nodeIndexMap.get(e.node2.id);
					
					if (nodeIndex1 != null && nodeIndex2 != null)
						quf.unite(nodeIndex1, nodeIndex2);
				}
				
				_isConnectedGraph = (quf.connectedComponentSize[0] == n);
			}
			return _isConnectedGraph;
		}

		public boolean isCompleteGraph() {
			// assume that the graph is connected.
			if (!isConnectedGraph())
				return false;
			
			if (_isCompleteGraph == null) {
				_isCompleteGraph = true;
				int K = nodes.length - prunedNodeCount;

				for (int degree : nodesDegrees) {
					if (degree >= 0) {
						if (degree < K - 1) {
							_isCompleteGraph = false;
						}
					}
				}
			}
			
			return _isCompleteGraph;
		}
		
		private boolean pruneNodes(Collection<Integer> nodesIndices) {
			boolean pruned = false;
			for (Integer nodeIndex : nodesIndices) {
				if (nodesDegrees[nodeIndex] >= 0) {
					nodesDegrees[nodeIndex] = -2;	//trick
					pruned = true;
				}
			}
			for (Integer nodeIndex : nodesIndices) {
				if (nodesDegrees[nodeIndex] == -2) {
					nodesDegrees[nodeIndex] = -1;
					prunedNodeCount++;
					for (int i = 0, n = nodes.length; i < n; i++) {
						if (nodesDegrees[i] > 0 && adjacencyMatrix[i][nodeIndex] > 0) {
							nodesDegrees[i]--;
						}
					}
				}
			}
			return pruned;
		}

		private void unpruneNodes(Collection<Integer> nodesIndices) {
			int n = nodes.length;
			if (nodesIndices == null) {
				System.arraycopy(backupNodesDegrees, 0, nodesDegrees, 0, n);
				prunedNodeCount = 0;
			} else {
				for (Integer nodeIndex : nodesIndices) {
					if (nodesDegrees[nodeIndex] < 0) {
						nodesDegrees[nodeIndex] = 0;
						prunedNodeCount--;
						for (int i = 0; i < n; i++) {
							if (nodesDegrees[i] >= 0 && adjacencyMatrix[nodeIndex][i] > 0) {
								nodesDegrees[nodeIndex]++;
								nodesDegrees[i]++;
							}
						}
					}
				}
			}
		}

		public static long computationCost; 
		@SuppressWarnings("unused")
		private boolean computeCliqueOfSizeK_Bruteforce(ArrayList<Integer> outputList, int K, int[] nodesIndices, int currentIndex) {
			if (K <= 0) {
				return true;
			}
			
			if (currentIndex >= nodesIndices.length) {
				return false;
			}
			boolean result = false;
			boolean canAppend = true;
			for (Integer existingNodeIndex : outputList) {
				computationCost++;
				if (adjacencyMatrix[existingNodeIndex][nodesIndices[currentIndex]] == 0) {
					canAppend = false;
					break;
				}
			}

			if (canAppend) {
				outputList.add(nodesIndices[currentIndex]);
				result = computeCliqueOfSizeK_Bruteforce(outputList, K - 1, nodesIndices, currentIndex + 1);
				if (!result) {
					outputList.remove(outputList.size() - 1);
				}
			}

			if (!result) {
				result = computeCliqueOfSizeK_Bruteforce(outputList, K, nodesIndices, currentIndex + 1);
			}
			
			return result;
		}

		private boolean computeCliqueOfSizeK_Greedy(ArrayList<Integer> outputList, int K, int[] nodesIndices) {
			// prerequisite: pruning edge is needed
			int n = nodesIndices.length;
			int lastI = n - 1;
			int i, j, k, index1, index2;

			if (K == 1) {
				if (n > 0) {
					outputList.add(nodesIndices[0]);
					return true;
				}
			} else if (K == 2) {
				for (i = 0; i < lastI; i++) {
					index1 = nodesIndices[i];
					for (j = i + 1; j < n; j++) {
						index2 = nodesIndices[j];
						
						if (adjacencyMatrix[index1][index2] > 0) {
							outputList.add(index1);
							outputList.add(index2);
							return true;
						}
					}
				}
			} else {
				for (i = 0; i < lastI; i++) {
					index1 = nodesIndices[i];
					for (j = i + 1; j < n; j++) {
						index2 = nodesIndices[j];
						outputList.clear();
						if (adjacencyMatrix[index1][index2] > 0) {
							int commonNeighborCount = 0;
							for (k = 0; k < n; k++) {
								computationCost++;
								if (adjacencyMatrix[index1][nodesIndices[k]] > 0 && adjacencyMatrix[index2][nodesIndices[k]] > 0) {
									commonNeighborCount++;
								}
							}
							if (commonNeighborCount == K - 2) {
								outputList.add(index1);
								outputList.add(index2);
								for (k = 0; k < n; k++) {
									if (adjacencyMatrix[index1][nodesIndices[k]] > 0 && adjacencyMatrix[index2][nodesIndices[k]] > 0) {
										outputList.add(nodesIndices[k]);
									}
								}
								return true;
							}
						}
					}
				}
			}
			
			return false;
		}
		
		public boolean computeCliqueOfSizeK(ArrayList<Integer> outputList, int K) {
			// prerequisite: 
			// call recomputeDegreesAndAdjacencyMatrix() first
			boolean result = false;
			int i, j, k, n = nodes.length;
			// step1: prune all nodes that have degree < K - 1
			int Kminus1 = K - 1;
			ArrayList<Integer> nodesIndicesToBePruned = new ArrayList<Integer>();
			boolean wannaPrune;
			boolean prunedEdge = false;
			int actualNodeCount;
			
			do {
				do {
					nodesIndicesToBePruned.clear();
					for (i = 0; i < n; i++) {
						if (nodesDegrees[i] >= 0 && nodesDegrees[i] < Kminus1) {
							nodesIndicesToBePruned.add(i);
						}
					}
						
				} while (pruneNodes(nodesIndicesToBePruned));
				
				// step2: prune edge of two nodes that have #commonNeighbors < K - 1
				actualNodeCount = n - prunedNodeCount;
				wannaPrune = false;
				if (actualNodeCount >= K && K >= 3) {
					for (i = n - 1; i > 0; i--) {
						for (j = i - 1; j >= 0 && nodesDegrees[i] > 0; j--) {
							if (nodesDegrees[j] > 0 && adjacencyMatrix[i][j] > 0) {
								int commonNeighborCount = 0;
								for (k = 0; k < n; k++) {
									if (nodesDegrees[k] > 0 && adjacencyMatrix[i][k] > 0 && adjacencyMatrix[j][k] > 0) {
										commonNeighborCount++;
									}
								}
								
								if (commonNeighborCount < K - 2) {
									// prune edge i, j
									adjacencyMatrix[i][j] = 0;
									nodesDegrees[i]--;
									adjacencyMatrix[j][i] = 0;
									nodesDegrees[j]--;
									wannaPrune = true;
								}
							}
						}
					}
				}
				
			} while (wannaPrune);

			// step3: for each remaining node check complete graph condition
			actualNodeCount = n - prunedNodeCount;
			if (actualNodeCount >= K) {
				int[] nodesIndices = new int[actualNodeCount];
				j = 0;

				for (i = 0; i < n; i++) {
					if (nodesDegrees[i] >= 0) {
						nodesIndices[j++] = i;
					}
				}
//				result = computeCliqueOfSizeK_Bruteforce(outputList, K, nodesIndices, 0);
				result = computeCliqueOfSizeK_Greedy(outputList, K, nodesIndices);
			}
			
			if (prunedNodeCount > 0) {
				unpruneNodes(null);	// restore all
			}
			
			if (prunedEdge) {
				for (i = 0; i < n; i++) {
					System.arraycopy(backupAdjacencyMatrix[i], 0, adjacencyMatrix[i], 0, n);
				}
			}
			
			return result;
		}
		
		public List<Integer> computeMaximumClique() {
			ArrayList<Integer> result = new ArrayList<Integer>();

			rebuildDegreesAndAdjacencyMatrix();

			// 1. find max degree;
			int maxDegree = 0;
			for (int deg : nodesDegrees) {
				if (deg >= 0) {	// if not pruned
					if (maxDegree < deg) {
						maxDegree = deg;
					}
				}
			}
			
			// find clique of size K where K = maxDegree, maxDegree - 1, ... 1 
			for (int K = maxDegree + 1; K > 0; K--) {
				if (computeCliqueOfSizeK(result, K)) {
					break;
				}
			}
			
			return result;
		}

		public void constructUsingUDG() {
			edges = new ArrayList<Edge>();
			HashMap<GridCell, GridCell> hashTable = new HashMap<GridCell, GridCell>(); 
			GridCell tempCell = new GridCell();
			int i, j, k, n;
			
			for (Node node : nodes) {
				tempCell.assign(node);
				GridCell cell = hashTable.get(tempCell);
				if (cell == null) {
					cell = new GridCell();
					cell.assign(node);
					hashTable.put(cell, cell);
				}
				cell.nodes.add(node);
			}
			
			for (GridCell cell : hashTable.keySet()) {
				n = cell.nodes.size();
				
				// nodes in the same cell should connect together
				for (i = 0; i < n; i++) {
					for (j = i + 1; j < n; j++) {
						if (cell.nodes.get(i).canConnect(cell.nodes.get(j))) {
							edges.add(new Edge(cell.nodes.get(i), cell.nodes.get(j)));
						}
					}
				}
				// nodes in others cell (only proximate) should be checked if they can connect together.
				for (k = 0; k < 8; k++) {
					cell.getProximateCell(tempCell, k);
					GridCell proximateCell = hashTable.get(tempCell);
					
					if (proximateCell != null && !proximateCell.processedUdgAlready) {
						for (Node node : cell.nodes) {
							for (Node proximateNode : proximateCell.nodes) {
								if (node.canConnect(proximateNode)) {
									edges.add(new Edge(node, proximateNode));
								}
							}
						}
					}
				}
				
				cell.processedUdgAlready = true;
			}
		}
	}
}
