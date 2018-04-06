import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

public class P609E_MinimumSpanningTreeForEachEdge_KRUSKAL {
    private static class Edge {
        public int u;
        public int v;
        public int weight;
        public boolean isMstEdge;
        public Edge(int u, int v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
            this.isMstEdge = false;
        }
        @Override
		public String toString() {
			return String.format("%d<-(%d%s)->%d", u, weight, isMstEdge ? "*" : "", v);
		}
    }
    /*
Input
5 7
1 2 3
1 3 1
1 4 5
2 3 2
2 5 3
3 4 2
4 5 4

Output
9
8
11
8
8
8
9
 */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out);
        String textLine = br.readLine();
        String[] splitted = textLine.split("\\s+");
        long answer = 0;
        int nVertices = Integer.parseInt(splitted[0]);
        int nEdges = Integer.parseInt(splitted[1]);

        if (nEdges > 0) {
            int i;
            Edge[] edges = new Edge[nEdges];
            for (i = 0; i < nEdges; i++) {
                textLine = br.readLine();
                splitted = textLine.split("\\s+");
                edges[i] = new Edge(Integer.parseInt(splitted[0]) - 1, Integer.parseInt(splitted[1]) - 1, Integer.parseInt(splitted[2])); 
            }
            int[] treeNodeParent = new int[nVertices];
            int[] treeNodeLayer = new int[nVertices];
            int[] treeNodeWeight = new int[nVertices];
	        long mstWeightResult = mstByKruskal(nVertices, edges, treeNodeParent, treeNodeLayer, treeNodeWeight);

	        for (Edge e : edges) {
	        	if (e.isMstEdge) {
	        		answer = mstWeightResult;
	        	} else {
	        		answer = mstWeightResult + e.weight - maxWeightEdgeAlongPath(treeNodeParent, treeNodeLayer, treeNodeWeight, e.u, e.v);
	        	}
	        	
	            pw.printf("%d\n", answer);
	        }
        }
        pw.close();
    }

	private static class QuickUnionFind {
		private int[] cp; // compressed parrent
		public int[] connectedComponentSize;
		public int nConnectedComponents;

		public QuickUnionFind(int nVertices) {
			cp = new int[nVertices];
			connectedComponentSize = new int[nVertices];
			for (int i = 0; i < nVertices; i++) {
				cp[i] = i;
				connectedComponentSize[i] = 1;
			}
			nConnectedComponents = nVertices;
		}

		public int root(int i) {
			while (i != cp[i]) {
				cp[i] = cp[cp[i]];	// path compression improvement
				i = cp[i];
			}
			return i;
		}

		public boolean unite(int nodeIndex1, int nodeIndex2) {
			int r1 = root(nodeIndex1);
			int r2 = root(nodeIndex2);

			if (r1 != r2) {
				int ccSize1 = connectedComponentSize[r1];
				int ccSize2 = connectedComponentSize[r2];
				int ccUnitedSize = ccSize1 + ccSize2;

				if (ccSize1 <= ccSize2) {	// weighted tree size improvement
					cp[r1] = r2;
					connectedComponentSize[r2] = ccUnitedSize;
				} else {
					cp[r2] = r1;
					connectedComponentSize[r1] = ccUnitedSize;
				}

				nConnectedComponents--;
				return true;
			}
			
			return false;
		}
	}

	private static long mstByKruskal(int nVertices, Edge[] edges, int[] parent, int[] layer, int[] weight) {
        // MST by Kruskal's Algorithm
        Edge[] sortedEdges = edges.clone();
        Arrays.sort(sortedEdges, (e1, e2) -> (e1.weight - e2.weight));
        long mstWeight = 0;
        QuickUnionFind quf = new QuickUnionFind(nVertices);
        Integer rootNode = null;

        for (Edge e : sortedEdges) {
        	if (quf.unite(e.u, e.v)) {
            	mstWeight += e.weight;
            	e.isMstEdge = true;
            	if (rootNode == null) {
            		rootNode = e.v;
            	}
            	if (quf.nConnectedComponents == 1) {
            		break;
            	}
        	}
        }
        
        // build array-based weighted tree
        Arrays.fill(layer, -1);
		parent[rootNode] = 0;
		layer[rootNode] = 0;

		Edge[] mstEdges = new Edge[nVertices - 1];
		int i = 0;
        for (Edge e : sortedEdges) {
        	if (e.isMstEdge) {
        		mstEdges[i++] = e;
        	}
        }
		
		boolean finished;
		do {
			finished = true;
	        for (Edge e : mstEdges) {
        		if (layer[e.u] < 0 && layer[e.v] >= 0) {
            		parent[e.u] = e.v;
            		layer[e.u] = layer[e.v] + 1;
            		weight[e.u] = e.weight;
            		finished = false;
        		} else if (layer[e.v] < 0 && layer[e.u] >= 0) {
            		parent[e.v] = e.u;
            		layer[e.v] = layer[e.u] + 1;
            		weight[e.v] = e.weight;
            		finished = false;
        		}
	        }
		} while (!finished);
        return mstWeight;
    }

	private static int maxWeightEdgeAlongPath(int[] parent, int[] layer, int[] weight, int u, int v) {
		int w;
		int maxWeight = Integer.MIN_VALUE;

		if (layer[u] < layer[v]) {
			u ^= v;	//swap
			v ^= u;
			u ^= v;
		}
		
		while (u >= 0 && layer[u] > layer[v]) {
			w = weight[u];
			if (maxWeight < w) {
				maxWeight = w;
			}

			u = parent[u];
		}

		while (u >= 0 && u != v) {
			w = weight[u];
			if (maxWeight < w) {
				maxWeight = w;
			}
			w = weight[v];
			if (maxWeight < w) {
				maxWeight = w;
			}

			u = parent[u];
			v = parent[v];
		}

		return maxWeight;
    }

	@SuppressWarnings("unused")
	private static int lowestCommonAncestor(int[] parent, int[] layer, int u, int v) {
		if (layer[u] < layer[v]) {
			u ^= v;	//swap
			v ^= u;
			u ^= v;
		}
		
		while (u >= 0 && layer[u] > layer[v]) {
			u = parent[u];
		}

		while (u >= 0 && u != v) {
			u = parent[u];
			v = parent[v];
		}
		
		return u;
	}
}
