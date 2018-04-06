import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

public class UnweightedUndirectedGraph {
	public int nodeRadius;

	public class Node {
		public int id;
		public int posX;
		public int posY;
		
		
		public Node(int id, int posX, int posY) {
			this.id = id;
			this.posX = posX;
			this.posY = posY;
		}
		
		public boolean canConnectUsingUDG(Node another) {
			int dX = posX - another.posX;
			int dY = posY - another.posY;
			return dX*dX + dY*dY <= nodeRadius*nodeRadius;
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
		private int[] parent;
		public int[] ccSize;
		public int maxCCSize;

		public QuickUnionFind(int nVertices) {
			parent = new int[nVertices];
			ccSize = new int[nVertices];
			for (int i = 0; i < nVertices; i++) {
				parent[i] = i;
				ccSize[i] = 1;
			}
			maxCCSize = 1;
		}

		public boolean areNodesConnected(int u, int v) {
			return root(u) == root(v);
		}

		public int root(int u) {
			while (u != parent[u]) {
				parent[u] = parent[parent[u]];	// path compression improvement
				u = parent[u];
			}
			return u;
		}

		public boolean unite(int u, int v) {
			int r1 = root(u);
			int r2 = root(v);

			if (r1 != r2) {
				int ccSize1 = ccSize[r1];
				int ccSize2 = ccSize[r2];
				int ccUnitedSize = ccSize1 + ccSize2;
				if (maxCCSize < ccUnitedSize) {
					maxCCSize = ccUnitedSize;
				}

				if (ccSize1 <= ccSize2) {	// weighted tree size improvement
					parent[r1] = r2;
					ccSize[r2] = ccUnitedSize;
				} else {
					parent[r2] = r1;
					ccSize[r1] = ccUnitedSize;
				}
				return true;
			}
			
			return false;
		}
	}
	
	public int maxGridX;
	private static int[][] eightNeighborDir = 
		{
			{-1, 1},  {0, 1},  {1, 1}, 
			{-1, 0},           {1, 0}, 
			{-1, -1}, {0, -1}, {1, -1} 
		};

	public class GridCell {

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
			gridX = node.posX / nodeRadius;
			gridY = node.posY / nodeRadius;
		}
		
		@Override
		public int hashCode() {
			return gridY * (maxGridX + 1) + gridX;
		}

		@Override
		public boolean equals(Object obj) {
			return gridX == ((GridCell)obj).gridX && gridY == ((GridCell)obj).gridY;
		}
		
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
		TreeMap<Integer, ArrayList<Integer>> tree = new TreeMap<Integer, ArrayList<Integer>>(new Comparator<Integer>() {
			@Override
			public int compare(Integer a, Integer b) {
				return b.compareTo(a);
			}
		}); 

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
			
			_isConnectedGraph = (quf.ccSize[0] == n);
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
//			result = computeCliqueOfSizeK_Bruteforce(outputList, K, nodesIndices, 0);
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
					if (cell.nodes.get(i).canConnectUsingUDG(cell.nodes.get(j))) {
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
							if (node.canConnectUsingUDG(proximateNode)) {
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
