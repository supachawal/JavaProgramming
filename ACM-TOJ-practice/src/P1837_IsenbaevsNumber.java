import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class P1837_IsenbaevsNumber {
/*
SAMPLE ...

INPUT:
7
Isenbaev Oparin Toropov
Ayzenshteyn Oparin Samsonov
Ayzenshteyn Chevdar Samsonov
Fominykh Isenbaev Oparin
Dublennykh Fominykh Ivankov
Burmistrov Dublennykh Kurpilyanskiy
Cormen Leiserson Rivest

OUTPUT:
Ayzenshteyn 2
Burmistrov 3
Chevdar 3
Cormen undefined
Dublennykh 2
Fominykh 1
Isenbaev 0
Ivankov 2
Kurpilyanskiy 3
Leiserson undefined
Oparin 1
Rivest undefined
Samsonov 2
Toropov 1
*/
	private static class Node implements Comparable<Object>{
		public String nodeName;
		public int depth;	// distance from root;
		public Set<Node> adjacentNeighbors;

		public Node(String nodeName) {
			super();
			this.nodeName = nodeName;
			this.depth = -1;
			this.adjacentNeighbors = new HashSet<Node>();
		}

		@Override
		public int hashCode() {
			return nodeName.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return compareTo(obj) == 0;
		}

		@Override
		public int compareTo(Object o) {
			String s = null;
			if (o instanceof Node) {
				s = ((Node)o).nodeName;
			} else if (o instanceof String) {
				s = (String)o;
			}
			
			return nodeName.compareTo(s);
		}
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		String textLine;
		String[] splitted;
//NOT USED:		int answer;
		
		textLine = br.readLine();
		int n = Integer.parseInt(textLine);
		TreeSet<Node> contestants = new TreeSet<Node>();
		Node Isenbaev = null, temp = new Node("");
		
		for (int i = 0; i < n; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			List<Node> teammates = new ArrayList<Node>();
			for (String s : splitted) {
				temp.nodeName = s;
				Node contestant = contestants.floor(temp);
				if (contestant == null || !contestant.equals(s)) {
					contestant = new Node(s);
					contestants.add(contestant);
				}
				
				teammates.add(contestant);
				if (Isenbaev == null && s.equals("Isenbaev")) {
					Isenbaev = contestant; 
				}
			}

			for (Node p : teammates) {
				for (Node q : teammates) {
					if (p != q) {
						p.adjacentNeighbors.add(q);
					}
				}
			}
		}
		
		// Perform BFS algorithm
		if (Isenbaev != null) {
			ArrayDeque<Node> Q = new ArrayDeque<Node>();
			Isenbaev.depth = 0;
			Q.add(Isenbaev);
			
			while (!Q.isEmpty()) {
				Node p = Q.poll();
				
				for (Node q : p.adjacentNeighbors) {
					if (q.depth < 0) {
						q.depth = p.depth + 1;
						Q.add(q);
					}
				}
			}
		}
		
		for (Node p : contestants) {
			pw.printf("%s %s\n", p.nodeName, p.depth < 0 ? "undefined" : String.valueOf(p.depth));
		}
		
		pw.close();
	}
}
