import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;

public class P1136_Parliament {
/*
SAMPLE ...

INPUT:
9
1
7
5
21
22
27
25
20
10

OUTPUT:
27
21
22
25
20
7
1
5
10

*/
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		String textLine;
//		String[] splitted;
		ArrayList<Integer> answer = new ArrayList<Integer>();
		
		textLine = br.readLine();
		int i, N = Integer.parseInt(textLine);
		int[] inputData = new int[N];
		
		for (i = 0; i < N; i++) {
			textLine = br.readLine();
			inputData[i] = Integer.parseInt(textLine);
		}
		
		SimpleBinaryTree tree = new SimpleBinaryTree();
		for (i = N-1; i >= 0; i--) {
			tree.insert(inputData[i]);
		}

		tree.traversePostOrderDescending(answer);
		
		for (Integer v : answer) {
			pw.println(v);
		}

		pw.close();
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

