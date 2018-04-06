import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;

class JAN16_8_CYCLRACE_CyclistRace {
/*
Example

Input:
5 14
1 1 1 2
1 1 2 5
1 2 3 4
1 2 4 7
2 3
2 4
1 5 5 4
2 5
2 6
1 7 5 8
2 7
2 8
2 9
2 10

Output:
10
15
21
28
35
42
49
56
*/

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		String textLine;
		String[] splitted;
		long answer;
		
		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		int N = Integer.parseInt(splitted[0]);
		int Q = Integer.parseInt(splitted[1]);
		int i, t, cyclist, newSpeed;

		BinaryHeap.KeyedValue.valueComparator = new Comparator<Object>() {
			public int compare(Object a, Object b) {
				// descending
				int t = Math.max(((CyclistInfo)a).lastT, ((CyclistInfo)b).lastT);
				 
				int r = Long.compare(((CyclistInfo)b).getUpdatePosition(t), ((CyclistInfo)a).getUpdatePosition(t));
				if (r == 0) {
					r = ((CyclistInfo)b).lastSpeed - ((CyclistInfo)a).lastSpeed;
				}
				return r;
			}
		};
//		BinaryHeap.KeyedValue.valueComparator = new Comparator<Object>() {
//			public int compare(Object a, Object b) {
//				// descending
//				int t = Math.max(((CyclistInfo)a).lastT, ((CyclistInfo)b).lastT);
//				 
//				int r = Long.compare(((CyclistInfo)b).getUpdatePosition(t), ((CyclistInfo)a).getUpdatePosition(t));
//				if (r == 0) {
//					r = ((CyclistInfo)b).lastSpeed - ((CyclistInfo)a).lastSpeed;
//				}
//				return r;
//			}
//		};

		ArrayList<BinaryHeap.KeyedValue<CyclistInfo>> cyclistList = new ArrayList<BinaryHeap.KeyedValue<CyclistInfo>>(N);
		
		BinaryHeap<CyclistInfo> maxHeap = new BinaryHeap<CyclistInfo>(N);
		for (cyclist = 0; cyclist < N; cyclist++) {
			cyclistList.add(null);
		}
		
		for (i = 0; i < Q; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			t = Integer.parseInt(splitted[1]);
			char c = splitted[0].charAt(0); 
			if (c == '1') {
				cyclist = Integer.parseInt(splitted[2]) - 1;	// make it zero-based 
				newSpeed = Integer.parseInt(splitted[3]);
				BinaryHeap.KeyedValue<CyclistInfo> ci = cyclistList.get(cyclist);
				
				if (ci == null) {
					ci = new BinaryHeap.KeyedValue<CyclistInfo>(cyclist, new CyclistInfo());
					cyclistList.set(cyclist, ci);
					ci.value.changeSpeed(t, newSpeed);
					maxHeap.insertItem(ci);
				} else {
					ci.value.changeSpeed(t, newSpeed);
					maxHeap.updateItem(cyclist, ci.value);
				}
			} else if (c == '2') {
				answer = maxHeap.getRoot().value.getUpdatePosition(t);
				pw.printf("%d\n", answer);
			}
		
		}
		pw.close();
	}

	public static class CyclistInfo {
		long lastPosition;
		int lastT;
		int lastSpeed;
		
		public long getUpdatePosition(int t) {
			if (t > lastT) {
				lastPosition += (long)lastSpeed * (t - lastT);
				lastT = t;
			}
			return lastPosition;
		}
		public void changeSpeed(int t, int newSpeed) {
			lastPosition += (long)lastSpeed * (t - lastT);
			lastT = t;
			lastSpeed = newSpeed;
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
}
