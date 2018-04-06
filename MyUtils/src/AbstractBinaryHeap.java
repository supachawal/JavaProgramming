public abstract class AbstractBinaryHeap {
	public AbstractBinaryHeap(int initialSize) {
		init(initialSize);
	}

	protected KeyValuePair _item[];
	protected int _indexOfKey[];
	protected int _count = 0;
	
	public final void init(int initialSize) {
		_item = new KeyValuePair[initialSize];
		_indexOfKey = new int[initialSize];
		_count = 0;
		for (int i = 0; i < initialSize; i++) {
			_item[i] = null;
			_indexOfKey[i] = -1;
		}
	}
	
	public final KeyValuePair getRoot() {
		return _item[0];
	}
	public final KeyValuePair popRoot() {
		KeyValuePair rootItem = _item[0];
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
				KeyValuePair lastItem = _item[--_count];
				
				if (index < _count) {
					heapifyDown(index, lastItem);
				}
				
				_indexOfKey[key] = -1;
				_item[_count] = null;
			}
		}
	}

	private final void deleteRoot() {
		if (_count > 0) {
			int key = _item[0].key;
			KeyValuePair lastItem = _item[--_count];
			heapifyDown(0, lastItem);
			_indexOfKey[key] = -1;
			_item[_count] = null;
		}
	}

	public final void insertItem(KeyValuePair entry) {
		heapifyUp(_count++, entry);
	}
	
	public final void updateItem(KeyValuePair entry, double newValue) {
		if (entry.value != newValue) { 
			int index = getIndexOfKey(entry.key);
			if (index >= 0) {
				entry.value = newValue;

				if (index == 0) {
					heapifyDown(0, entry);
				} else {
					heapifyUp(index, entry);
				}
			}
		}
	}
	
	public final KeyValuePair[] getItems() {
		return _item;
	}

	abstract protected void heapifyDown(int startIndex, final KeyValuePair entry);
	abstract protected void heapifyUp(int startIndex, final KeyValuePair entry);
	abstract public double getValueByKey(int key);
}
