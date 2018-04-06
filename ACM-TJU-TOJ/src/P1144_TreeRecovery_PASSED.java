import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

class P1144_TreeRecovery_PASSED {
/*
Sample Input

DBACEGF ABCDEFG
BCAD CBAD

Sample Output

ACBFGED
CDAB

*/
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		ArrayList<Integer> answer = new ArrayList<Integer>();
		String textLine;
		String[] splitted;
		
		int i;
		ValueKeyMapComparator comparator = new ValueKeyMapComparator(); 

		while ((textLine = br.readLine()) != null) {
			if (textLine.length() == 0) {
				break;
			}
			splitted = textLine.split("\\s+");
			
			i = 0;
			for (char c : splitted[1].toCharArray()) {
				comparator.valueKeyMap.put((int) c, i++);
			}
			
			SimpleBinaryTree tree = new SimpleBinaryTree(comparator);
			
			for (char c : splitted[0].toCharArray()) {
				tree.insert(c);
			}
			answer.clear();
			tree.traversePostOrder(answer);
			
			for (Integer v : answer) {
				pw.print((char)v.intValue());
			}
			
			pw.println();
		}
		pw.close();
	}

	public static class ValueKeyMapComparator implements Comparator<Integer> {
		public HashMap<Integer, Integer> valueKeyMap = new HashMap<Integer, Integer>();
		public int compare(Integer o1, Integer o2) {
			return valueKeyMap.get(o1).compareTo(valueKeyMap.get(o2));
		}
	}

	public static class SimpleBinaryTree {
		private Node root;
		private Comparator<Integer> comparator;

		protected class Node {
			int value;
			Node left, right;
			
			public Node(int value) {
				this.value = value;
			}
			
			public void insert(Node newNode) {
				if (comparator == null) {
					if (newNode.value < value) {
						if (left != null) {
							left.insert(newNode);
						} else {
							left = newNode;
						}
					} else {
						if (right != null) {
							right.insert(newNode);
						} else {
							right = newNode;
						}
					}
				} else {
					if (comparator.compare(newNode.value, value) < 0) {
						if (left != null) {
							left.insert(newNode);
						} else {
							left = newNode;
						}
					} else {
						if (right != null) {
							right.insert(newNode);
						} else {
							right = newNode;
						}
					}
				}
			}
			public void traversePostOrder(ArrayList<Integer> outputList) {
				if (left != null)
					left.traversePostOrder(outputList);
				if (right != null)
					right.traversePostOrder(outputList);
				outputList.add(value);
			}
			public void traversePostOrderDescending(ArrayList<Integer> outputList) {
				if (right != null)
					right.traversePostOrderDescending(outputList);
				if (left != null)
					left.traversePostOrderDescending(outputList);
				outputList.add(value);
			}

			public void delete() {
				if (left != null) {
					left.delete();
					left = null;
				}
				if (right != null) {
					right.delete();
					right = null;
				}
			}
			
			@Override
			protected void finalize() throws Throwable {
				delete();
				super.finalize();
			}
		}

		public SimpleBinaryTree() {
			this.comparator = null;
			this.root = null;
		}

		public SimpleBinaryTree(Comparator<Integer> comparator) {
			this.comparator = comparator;
			this.root = null;
		}
		
		public void insert(int value) {
			if (root == null) {
				root = new Node(value);
			} else {
				root.insert(new Node(value));
			}
		}

		public void deleteAll() {
			if (root != null) {
				root.delete();
				root = null;
			}
		}
		
		public void traversePostOrder(ArrayList<Integer> outputList) {
			if (root != null) {
				root.traversePostOrder(outputList);
			}
		}
		public void traversePostOrderDescending(ArrayList<Integer> outputList) {
			outputList.clear();
			if (root != null) {
				root.traversePostOrderDescending(outputList);
			}
		}

		@Override
		protected void finalize() throws Throwable {
			deleteAll();
			super.finalize();
		}
	}
}

