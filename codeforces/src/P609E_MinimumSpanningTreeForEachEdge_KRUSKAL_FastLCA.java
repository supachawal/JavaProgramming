import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class P609E_MinimumSpanningTreeForEachEdge_KRUSKAL_FastLCA {
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

            ArrayList<ArrayList<Edge>> adjacencyList = new ArrayList<ArrayList<Edge>>(nVertices);
            for (i = 0; i < nVertices; i++) {
            	adjacencyList.add(new ArrayList<Edge>());
            }            
            long mstWeightResult = mstByKruskal(nVertices, edges, adjacencyList);
            FastLCAforTree fastLCA = new FastLCAforTree(adjacencyList);
            
	        for (Edge e : edges) {
	        	if (e.isMstEdge) {
	        		answer = mstWeightResult;
	        	} else {
	        		answer = mstWeightResult + e.weight - fastLCA.maxWeightEdgeAlongPath(e.u, e.v);
	        	}
	        	
	            pw.printf("%d\n", answer);
	        }
        }
        pw.close();
    }

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

    private static class QuickUnionFind {
		private int[] cp; // compressed parents
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

	private static long mstByKruskal(int nVertices, Edge[] edges, ArrayList<ArrayList<Edge>> adjacencyList) {
        // MST by Kruskal's Algorithm
        Edge[] sortedEdges = edges.clone();
        Arrays.sort(sortedEdges, (e1, e2) -> (e1.weight - e2.weight));
        long mstWeight = 0;
        QuickUnionFind quf = new QuickUnionFind(nVertices);

        for (Edge e : sortedEdges) {
        	if (quf.unite(e.u, e.v)) {
            	mstWeight += e.weight;
            	e.isMstEdge = true;
            	adjacencyList.get(e.u).add(e);
            	adjacencyList.get(e.v).add(e);
            	if (quf.nConnectedComponents == 1) {
            		break;
            	}
        	}
        }
        
        return mstWeight;
    }

    private static class FastLCAforTree {	// Lowest Common Ancestors
    	private ArrayList<ArrayList<Edge>> adjacencyList;
    	private int nSparseBits;
    	private int[] layer;
    	private int[][] upLine;
    	private int[][] maxEdgeWeight;
    	private int timer;
    	private int[] timeEnter;
    	private int[] timeLeave;
    	
    	public FastLCAforTree(ArrayList<ArrayList<Edge>> adjacencyList) {
    		int nVertices = adjacencyList.size();
        	this.adjacencyList = adjacencyList;

    		nSparseBits = (int)(Math.log(nVertices) / Math.log(2)) + 1;
    		layer = new int[nVertices];
    		upLine = new int[nSparseBits][nVertices];
    		maxEdgeWeight = new int[nSparseBits][nVertices];
    		timer = 0;
    		timeEnter = new int[nVertices];
    		timeLeave = new int[nVertices];
    		
    	    layer[0] = -1;
    	    dfsForTree(0, 0, 0);
    	}
    	
    	private void dfsForTree(int v, int parent, int pCost) {
    	    layer[v] = layer[parent] + 1;
    	    timeEnter[v] = ++timer;
    	    upLine[0][v] = parent;
    	    maxEdgeWeight[0][v] = pCost;
    	    int i;
    	    for (i = 1; i < nSparseBits; i++) {
    	    	int p = upLine[i - 1][v];
    	        upLine[i][v] = upLine[i - 1][p];
    	        maxEdgeWeight[i][v] = Math.max(maxEdgeWeight[i - 1][v], maxEdgeWeight[i - 1][p]);
    	    }
    	    for (Edge e : adjacencyList.get(v)) {
    	        int to = (e.u == v ? e.v : e.u);
    	        if (to != parent) {
    	            dfsForTree(to, v, e.weight);
    	        }
    	    }
    	    timeLeave[v] = ++timer;
    	}

    	private final boolean isAncestor(int a, int b) {
    	    return (timeEnter[a] <= timeEnter[b] && timeLeave[a] >= timeLeave[b]);
    	}
    	
    	public final int lca(int u, int v) {
    	    if (isAncestor(u, v)) {
    	        return u;
    	    }
    	    if (isAncestor(v, u)) {
    	        return v;
    	    }

    	    for (int i = nSparseBits - 1; i >= 0; i--) {
    	    	int p = upLine[i][u];
    	        if (!isAncestor(p, v)) {
    	            u = p;
    	        }
    	    }
    	    return upLine[0][u];
    	}

    	public final int maxWeightEdgeAlongPath(int u, int v) {
    		int lcaLayer = layer[lca(u, v)];
    		return Math.max(maxEdgeWeightAlongUpwardPath(u, layer[u] - lcaLayer),
    						maxEdgeWeightAlongUpwardPath(v, layer[v] - lcaLayer));
    	}
    	
    	private final int maxEdgeWeightAlongUpwardPath(int v, int span) {
    	    int ret = 0;
    	    int p = v;
    	    int s = span;
    	    for (int i = 0; i < nSparseBits; i++, s >>>= 1) {
	//BUGGY       	    for (int i = nSparseBits - 1; i >= 0; i--, s >>>= 1) {
    	        if ((s & 1) != 0) {
    	            ret = Math.max(ret, maxEdgeWeight[i][p]);
    	            p = upLine[i][p];
    	        }
    	    }
    	    return ret;
    	}
    }
}
