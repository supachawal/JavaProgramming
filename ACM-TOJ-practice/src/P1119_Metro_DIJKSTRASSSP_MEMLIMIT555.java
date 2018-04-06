import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class P1119_Metro_DIJKSTRASSSP_MEMLIMIT555 {
/*
SAMPLE ...

INPUT:
3 2
3
1 1
3 2
1 2

OUTPUT:
383

*/
	private static final float INF = Float.POSITIVE_INFINITY;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		String textLine;
		String[] splitted;
//NOT USED:		int answer;
		
		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		
		int N = Integer.parseInt(splitted[0]);
		int M = Integer.parseInt(splitted[1]);
		int n = (M + 1) * (N + 1);
		float[][] A = new float[n][n];
		makeGridAdjMatrix(A, M + 1, N + 1, 100.0F);

		final float diagonalLength = 141.421F;
		textLine = br.readLine();
		int K = Integer.parseInt(textLine);

		for (int i = 0; i < K; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			int diagI = Integer.parseInt(splitted[1]);
			int diagJ = Integer.parseInt(splitted[0]);
			A[(diagI - 1) * (N + 1) + (diagJ - 1)][diagI * (N + 1) + diagJ] = diagonalLength;
		}
		
		pw.printf("%d", Math.round(dijkstraSSSP_Diagonal(A, n, N + 1)));
		pw.close();
	}

	private static void makeGridAdjMatrix(float[][] A, int m, int n, float unitLength) {
		int nn = m * n;
		for (int k = 0; k < nn; k++) {
			Arrays.fill(A[k], INF);
		}
		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (i * n + j + 1 < nn) {
					A[i * n + j][i * n + j + 1] = unitLength;
				}
				if ((i + 1) * n + j < nn) {
					A[i * n + j][(i + 1) * n + j] = unitLength;
				}
			}
		}
	}

	public static class BinaryHeap<V> {
		public static class KeyedValue<V> implements Comparable<KeyedValue<V>> {
			private int key;
			private V value;
			public static Comparator<Object> valueComparator;
			public KeyedValue(final int initKey, final V initValue) {
				key = initKey;
				value = initValue;
			}
			public String toString() {
				return String.format("key=%d, value=%s", key, value);
			}
			public int getKey() {
				return key;
			}
			public V getValue() {
				return value;
			}
			public V setValue(V value) {
				this.value = value;
				return value;
			}
			@SuppressWarnings("unchecked")
			@Override
			public int compareTo(KeyedValue<V> o) {
				if (valueComparator != null)
					return valueComparator.compare(value, o.value);
				
				return ((Comparable<V>)value).compareTo(o.value);
			}
		}

		public BinaryHeap(int initialSize) {
			init(initialSize);
		}

		protected ArrayList<KeyedValue<V>> _item;
		protected int _indexOfKey[];
		protected int _count = 0;
		
		public final void init(int initialSize) {
			_item = new ArrayList<KeyedValue<V>>(initialSize);
			_indexOfKey = new int[initialSize];
			_count = 0;
			for (int i = 0; i < initialSize; i++) {
				_item.add(null);
				_indexOfKey[i] = -1;
			}
		}
		
		public final KeyedValue<V> getRoot() {
			return _item.get(0);
		}
		public final KeyedValue<V> popRoot() {
			KeyedValue<V> rootItem = getRoot();
			deleteRoot();
			return rootItem;
		}
		public final int getItemCount() {
			return _count;
		}
		
		protected final int getIndexOfKey(int key) {
			int index;
			try {
				index = _indexOfKey[key];
			} catch (Exception e) {
				index = -1;
			}
			return index;
		}
		
		public final void deleteItem(int key) {
			if (_count > 0) {
				int index = getIndexOfKey(key);
				if (index >= 0) {
					_count--;
					
					if (index < _count) {
						heapifyDown(index, _item.get(_count));
					}
					
					_indexOfKey[key] = -1;
					_item.set(_count, null);
				}
			}
		}

		private final void deleteRoot() {
			if (_count > 0) {
				int key = getRoot().getKey();
				KeyedValue<V> lastItem = _item.get(--_count);
				heapifyDown(0, lastItem);
				_indexOfKey[key] = -1;
				_item.set(_count, null);
			}
		}

		public final void insertItem(KeyedValue<V> entry) {
			heapifyUp(_count++, entry);
		}
		
		public final void updateItem(int key, V newValue) {
			int index = getIndexOfKey(key);
			if (index >= 0) {
				KeyedValue<V> entry = _item.get(index);
				entry.setValue(newValue);

				if (index == 0) {
					heapifyDown(0, entry);
				} else {
					heapifyUp(index, entry);
				}
			}
		}
		
		public final ArrayList<KeyedValue<V>> getItems() {
			return _item;
		}

		public V getValueByKey(int key) {
			int index = getIndexOfKey(key);
			
			if (index < 0)
				return null;
			
			return _item.get(index).getValue();
		}
		
		/**
		 * heapifyDown : mostly used on deletion or update at root node
		 */
		protected void heapifyDown(int startIndex, final KeyedValue<V> entry) {
			int i = startIndex;
			int k = 2 * i + 1;	// child
			
			while (k < _count) {
				// choose minimum of (left and right)
				if (k + 1 < _count && _item.get(k + 1).compareTo(_item.get(k)) < 0)
					k++;
				
				if (entry.compareTo(_item.get(k)) <= 0)
					break;
				
				_item.set(i, _item.get(k));
				_indexOfKey[_item.get(i).getKey()] = i;
				i = k;
				k = 2 * i + 1;
			}
			
			_item.set(i, entry);
			_indexOfKey[entry.getKey()] = i;
		}

		/**
		 * heapifyUp : bottom-to-root mostly used on insertion or update at non-root node
		 */
		protected void heapifyUp(int startIndex, final KeyedValue<V> entry) {
			int i = startIndex;
			int p = (i - 1) >> 1;   // p = parent
			
			while (p >= 0) {
				if (entry.compareTo(_item.get(p)) >= 0)
					break;

				_item.set(i, _item.get(p));
				_indexOfKey[_item.get(i).getKey()] = i;
				i = p;
				p = (i - 1) >> 1;
			}
			
			_item.set(i, entry);
			_indexOfKey[entry.getKey()] = i;
		}
	}
	
	private static float dijkstraSSSP_Diagonal(float[][] A, int n, int columns) {
		BinaryHeap.KeyedValue.valueComparator = new Comparator<Object>() {
			public int compare(Object a, Object b) {
				return ((Float)a).compareTo((Float)b);
			}
		};
		ArrayList<BinaryHeap.KeyedValue<Float>> dist = new ArrayList<BinaryHeap.KeyedValue<Float>>(n);
		BinaryHeap<Float> PQ = new BinaryHeap<Float>(n);
		ArrayList<Integer> neighborList = new ArrayList<Integer>();
		
		dist.add(new BinaryHeap.KeyedValue<Float>(0, 0.0F));
		PQ.insertItem(dist.get(0));
		for (int k = 1; k < n; k++) {
			dist.add(new BinaryHeap.KeyedValue<Float>(k, INF));
			PQ.insertItem(dist.get(k));
		}

		while (PQ.getItemCount() > 0) {
			BinaryHeap.KeyedValue<Float> u = PQ.popRoot();
			
			if (u.key == n - 1) {	// reached goal
				break;
			}

			neighborList.clear();
			if (u.key + 1 < n)
				neighborList.add(u.key + 1);
			if (u.key + columns < n)
				neighborList.add(u.key + columns);
			if (u.key + columns + 1 < n)
				neighborList.add(u.key + columns + 1);
			
			for (Integer v : neighborList) {
				float alt = dist.get(u.key).getValue() + A[u.key][v];
				if (alt < dist.get(v).getValue()) {
					PQ.updateItem(v, alt);
				}
			}
		}
		
		return dist.get(n - 1).getValue();
	}
}

