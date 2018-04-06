import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class P609E_MinimumSpanningTreeForEachEdge_PRIM {
    private static class Edge implements Comparable<Edge> {
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

		@Override
        public int compareTo(Edge o) {
            return weight - o.weight;
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
            ArrayList<Edge> edgeList = new ArrayList<Edge>(nEdges);
            ArrayList<ArrayList<Edge>> edgeMap = new ArrayList<ArrayList<Edge>>(nVertices);
            for (i = 0; i < nVertices; i++) {
                edgeMap.add(new ArrayList<Edge>());
            }
            for (i = 0; i < nEdges; i++) {
                textLine = br.readLine();
                splitted = textLine.split("\\s+");
                Edge e =
                    new Edge(Integer.parseInt(splitted[0]) - 1, Integer.parseInt(splitted[1]) - 1,
                             Integer.parseInt(splitted[2]));
                edgeList.add(e);
                edgeMap.get(e.u).add(e);
                edgeMap.get(e.v).add(e);
            }
            int[] treeNodeParent = new int[nVertices];
            int[] treeNodeLayer = new int[nVertices];
            int[] treeNodeWeight = new int[nVertices];
            long mstWeightResult = mstWeightByPrim(edgeMap, treeNodeParent, treeNodeLayer, treeNodeWeight);
        
	        for (Edge e : edgeList) {
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

    private static long mstWeightByPrim(ArrayList<ArrayList<Edge>> edgeMap, int[] parent, int[] layer, int[] weight) {
        // MST by Prim's Algorithm tuned by MinHeap
        int nVertices = edgeMap.size();
        boolean[] visited = new boolean[nVertices];
        PriorityQueue<Edge> minHeap = new PriorityQueue<Edge>();
        int v = 0;
        int visitedNodeCount = 0;
        long sumMinCost = 0;
        int minCost = 0;
        
        Arrays.fill(layer, -1);
		parent[0] = 0;
		layer[0] = 0;

        while (v >= 0) {
            visited[v] = true;
            visitedNodeCount++;
            
            for (Edge e : edgeMap.get(v)) {
                if (!visited[e.u] || !visited[e.v]) {
                    minHeap.add(e);
                }
            }
            sumMinCost += minCost;
            v = -1;
            if (visitedNodeCount < nVertices) {
                minCost = Integer.MAX_VALUE;
	            while (!minHeap.isEmpty()) {
	                Edge e = minHeap.poll();
	
	                if (!visited[e.u]) {
	                    v = e.u;
	                    minCost = e.weight;
	                    e.isMstEdge = true;
	                	parent[v] = e.v;
	                	layer[v] = layer[e.v] + 1;
	                	weight[v] = e.weight;
	                    break;
	                } else if (!visited[e.v]) {
	                    v = e.v;
	                    minCost = e.weight;
	                    e.isMstEdge = true;
	                	parent[v] = e.u;
	                	layer[v] = layer[e.u] + 1;
	                	weight[v] = e.weight;
	                    break;
	                }
	            }
            }
        }

        return sumMinCost;
    }

	private static int maxWeightEdgeAlongPath(int[] parent, int[] layer, int[] weight, int u, int v) {
		int w;
		int maxWeight = Integer.MIN_VALUE;

		if (layer[u] < layer[v]) {
			u ^= v;	//swap
			v ^= u;
			u ^= v;
		}
		
		int layerV = layer[v];
		while (layer[u] > layerV) {
			w = weight[u];
			if (maxWeight < w) {
				maxWeight = w;
			}

			u = parent[u];
		}

		while (u != v) {
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
}
