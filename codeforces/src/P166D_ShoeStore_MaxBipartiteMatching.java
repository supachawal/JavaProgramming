import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

public class P166D_ShoeStore_MaxBipartiteMatching {

	private static class Node {
		public int id;
		public int value;
		public int size;
		public Node(int id, int value, int size) {
			this.id = id;
			this.value = value;
			this.size = size;
		}
	}
/*TESTCASE:
input:
3
10 1
30 2
20 3
2
20 1
20 2

output:
30
2
2 3
1 1
 */
	private static BufferedReader br;
	private static PrintWriter pw;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String textLine;
		String[] splitted;
		int i;

		textLine = br.readLine();
		int nShoes = Integer.parseInt(textLine);
		Node[] shoes = new Node[nShoes];
		for (i = 0; i < nShoes; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			shoes[i] = new Node(i + 1, Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]));
		}

		textLine = br.readLine();
		int nCustomers = Integer.parseInt(textLine);
		Node[] customers = new Node[nCustomers];
		for (i = 0; i < nCustomers; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			customers[i] = new Node(i + 1, Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]));
		}
		int n = nShoes + nCustomers;
		HashMap<Integer, ArrayList<Integer>> directedEdgeMap = new HashMap<Integer, ArrayList<Integer>>(n);
		buildDirectedEdgeMap(directedEdgeMap, shoes, customers);
		
		int[] matching = new int[n + 1];
		Integer[] vertices = new Integer[n];
		for (i = 0; i < n; i++) {
			vertices[i] = i + 1;
		}
		
		Arrays.sort(vertices, nCustomers, n, (a, b) -> (shoes[b - nCustomers - 1].value - shoes[a - nCustomers - 1].value));
		hungarianAlgorithm(vertices, directedEdgeMap, matching);
		
		long sumCost = 0;
		int nPairsSold = 0;
		ArrayList<Integer> matchedShoes = new ArrayList<Integer>();
		ArrayList<Integer> matchedCustomers = new ArrayList<Integer>();
		if (nCustomers > nShoes) {
			for (i = nCustomers + 1; i <= n; i++) {
				int custId = matching[i];
				if (custId > 0) {
					sumCost += shoes[i - nCustomers - 1].value;
					matchedShoes.add(i - nCustomers);
					matchedCustomers.add(custId);
				}
			}
		} else {
			for (i = 1; i <= nCustomers; i++) {
				int shoesId = matching[i];
				if (shoesId > 0) {
					sumCost += shoes[shoesId - nCustomers - 1].value;
					matchedShoes.add(shoesId - nCustomers);
					matchedCustomers.add(i);
				}
			}
		}
		nPairsSold = matchedShoes.size();
		pw.printf("%d\n%d\n", sumCost, nPairsSold);
		for (i = 0; i < nPairsSold; i++) {
			pw.printf("%d %d\n", matchedCustomers.get(i), matchedShoes.get(i));
		}
		
		pw.close();
	}

	private static void buildDirectedEdgeMap(HashMap<Integer, ArrayList<Integer>> directedEdgeMap, Node[] shoes, Node[] customers) {
		int nCustomers = customers.length;
		
		TreeMap<Integer, TreeMap<Integer, ArrayList<Node>>> shoesValueMap = new TreeMap<Integer, TreeMap<Integer, ArrayList<Node>>>((o1, o2) -> (o2 - o1)); 
		for (Node shoe : shoes) {
			TreeMap<Integer, ArrayList<Node>> shoesSizeMap = shoesValueMap.get(shoe.value);
			if (shoesSizeMap == null) {
				shoesSizeMap = new TreeMap<Integer, ArrayList<Node>>();
				shoesValueMap.put(shoe.value, shoesSizeMap);
			}
			ArrayList<Node> shoesOfSameSize = shoesSizeMap.get(shoe.size);
			if (shoesOfSameSize == null) {
				shoesOfSameSize = new ArrayList<Node>();
				shoesSizeMap.put(shoe.size, shoesOfSameSize);
			}
			shoesOfSameSize.add(shoe);
		}
		
		TreeMap<Integer, TreeMap<Integer, ArrayList<Node>>> customerValueMap = new TreeMap<Integer, TreeMap<Integer, ArrayList<Node>>>(); 
		for (Node cust : customers) {
			TreeMap<Integer, ArrayList<Node>> customerSizeMap = customerValueMap.get(cust.value);
			if (customerSizeMap == null) {
				customerSizeMap = new TreeMap<Integer, ArrayList<Node>>();
				customerValueMap.put(cust.value, customerSizeMap);
			}
			ArrayList<Node> customersOfSameSize = customerSizeMap.get(cust.size);
			if (customersOfSameSize == null) {
				customersOfSameSize = new ArrayList<Node>();
				customerSizeMap.put(cust.size, customersOfSameSize);
			}
			customersOfSameSize.add(cust);
		}

		for (Node cust : customers) {
			ArrayList<Integer> choices = new ArrayList<Integer>();
			for (TreeMap<Integer, ArrayList<Node>> shoesSizeMap : shoesValueMap.tailMap(cust.value).values()) {
				for (ArrayList<Node> shoesCanFit : shoesSizeMap.subMap(cust.size, cust.size + 2).values()) {
					for (Node shoe : shoesCanFit) {
						choices.add(shoe.id + nCustomers);
					}
				}
			}
			directedEdgeMap.put(cust.id, choices);
		}
		for (Node shoe : shoes) {
			ArrayList<Integer> choices = new ArrayList<Integer>();
			for (TreeMap<Integer, ArrayList<Node>> customerSizeMap : customerValueMap.tailMap(shoe.value).values()) {
				for (ArrayList<Node> customersCanFit : customerSizeMap.subMap(shoe.size - 1, shoe.size + 1).values()) {
					for (Node cust : customersCanFit) {
						choices.add(cust.id);
					}
				}
			}
			directedEdgeMap.put(shoe.id + nCustomers, choices);
		}
	}
	private static void hungarianAlgorithm(Integer[] vertices, HashMap<Integer, ArrayList<Integer>> directedEdgeMap, int[] matching) {
		boolean[] visited = new boolean[vertices.length + 1];
		
		for (Integer u : vertices) {
			Arrays.fill(visited, false);
			dfsAugmentingPath(directedEdgeMap, matching, visited, u);
		}
	}

	private static boolean dfsAugmentingPath(HashMap<Integer, ArrayList<Integer>> directedEdgeMap, int[] matching, boolean[] visited, int u) {
		ArrayList<Integer> adjacentVertices = directedEdgeMap.get(u);
		if (adjacentVertices != null) {
			for (Integer v : adjacentVertices) {
				if (!visited[v]) {
					visited[v] = true;
					if (matching[v] == 0 || dfsAugmentingPath(directedEdgeMap, matching, visited, matching[v])) {
						matching[v] = u;
						return true;
					}
				}
			}
		}

		return false;
	}
	
}
