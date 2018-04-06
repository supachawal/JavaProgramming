import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class P620E_NewYearTree_SLOW {
	private static BufferedReader br;
	private static PrintWriter pw;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String textLine = br.readLine();
		String[] splitted = textLine.split("\\s+");
		int n = Integer.parseInt(splitted[0]);
		int m = Integer.parseInt(splitted[1]);
		int i;

		ArrayList<Node> tree = new ArrayList<Node>(n + 1);

		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		tree.add(null);
		for (i = 0; i < n; i++) {
			tree.add(new Node(i + 1, Integer.parseInt(splitted[i])));
		}
		for (i = 1; i < n; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			int u = Integer.valueOf(splitted[0]);
			int v = Integer.valueOf(splitted[1]);
			tree.get(u).adjList.add(tree.get(v));
			tree.get(v).adjList.add(tree.get(u));
		}
		
		Node root = tree.get(1);
		
		root.parent = root; 
		root.buildTree();

		for (i = 0; i < m; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			int v = Integer.parseInt(splitted[1]);
			if (splitted[0].charAt(0) == '1') {
				int newColor = Integer.parseInt(splitted[2]);
				tree.get(v).changeSubtreeColor(newColor, true);
			} else {
				pw.printf("%d\n", tree.get(v).subtreeColors.size());
			}
		}

		pw.close();
	}

	private static class FrequencyArray {
		public int[] freq;
		public int head;
		public int tail;
		public int[] next;
		public int[] prev;
		public int countDistinct;
		public FrequencyArray(int maxValue) {
			freq = new int[maxValue + 1];
			next = new int[maxValue + 1];
			prev = new int[maxValue + 1];
			head = 0;
			tail = 0;
			countDistinct = 0;
		}
		
		public void clear() {
			Arrays.fill(freq, 0);
			Arrays.fill(next, 0);
			Arrays.fill(prev, 0);
			countDistinct = 0;
			head = 0;
			tail = 0;
		}
		public void addFreq(int key, int addedFreq) {
			int oldFreq = freq[key];
			int newFreq = oldFreq + addedFreq; 
			freq[key] = newFreq;
			
			if (oldFreq == 0 && newFreq != 0) {
				countDistinct++;

				prev[key] = tail;
				next[key] = 0;
				if (tail > 0) {
					next[tail] = key;
				} 
				tail = key;
				if (head == 0) {
					head = tail;
				}
			} else if (oldFreq != 0 && newFreq == 0) {
				countDistinct--;

				if (key == head) {
					head = next[head];
					prev[head] = 0;
				} 
				
				if (key == tail) {
					tail = prev[tail];
					next[tail] = 0;
				} else if (key != head) {
					next[prev[key]] = next[key];
					prev[next[key]] = prev[key];
				}
			}
		}
		public int get(int key) {
			return freq[key];
		}
		
		public int size() {
			return countDistinct;
		}
	}
	private static class Node {
		public int id;
		public ArrayList<Node> adjList;
		private int color;
		public FrequencyArray subtreeColors;
		public int subtreeSize;
		public Node parent;
		public Node(int id, int initColor) {
			this.id = id; 
			adjList = new ArrayList<Node>();
			color = initColor;
			subtreeColors = new FrequencyArray(60);
			subtreeColors.addFreq(color, 1);
			subtreeSize = 1;
			parent = null;
		}
		
		@Override
		public String toString() {
			return String.format("Node(id=%d,parent=%d)", id, parent == null ? -1 : parent.id);
		}

		@Override
		public boolean equals(Object obj) {
			return id == ((Node)obj).id;
		}

		public void changeSubtreeColor(int newColor, boolean isSubtreeRoot) {
			if (color != newColor || subtreeColors.size() > 1) {
				if (isSubtreeRoot) {
					for (int key = subtreeColors.head; key > 0; key = subtreeColors.next[key]) {
						subtreeColors.freq[key] *= -1;
					}
					subtreeColors.addFreq(newColor, subtreeSize);
					Node q = this;
					Node p = q.parent;
					while (p != q) {
						p.addColorsFromChild(this);
						q = p;
						p = q.parent;
					} 
				}

				subtreeColors.clear();
				subtreeColors.addFreq(newColor, subtreeSize);
				color = newColor; 
			
				for (Node adjNode : adjList) {
					if (adjNode != parent) {
						adjNode.changeSubtreeColor(newColor, false);
					}
				}
			}
		}
		
		public void buildTree() {
			for (Node adjNode : adjList) {
				if (adjNode != parent) {
					adjNode.parent = this;
					adjNode.buildTree();
					subtreeSize += adjNode.subtreeSize;
					addColorsFromChild(adjNode);
				}
			}
		}

		private void addColorsFromChild(Node child) {
			for (int c = child.subtreeColors.head; c > 0 ; c = child.subtreeColors.next[c]) {
				int childFreq = child.subtreeColors.get(c); 
				if (childFreq != 0) {
					subtreeColors.addFreq(c, childFreq);
				}
			}
		}
	}
}