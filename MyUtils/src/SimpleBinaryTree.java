import java.util.ArrayList;
import java.util.Comparator;

public class SimpleBinaryTree {

	public static void main(String[] args) {
		SimpleBinaryTree test = new SimpleBinaryTree();
		test.insert(10);
		test.insert(20);
		test.insert(25);
		test.insert(27);
		test.insert(22);
		test.insert(21);
		test.insert(5);
		test.insert(7);
		test.insert(1);
		ArrayList<Integer> outputList = new ArrayList<Integer>();
		test.traversePostOrderDescending(outputList);
		System.out.println(outputList);
	}

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
