//-- Heap   definition A heap is a binary tree but not a binary search tree that its each node has value less than or equal to its children values. 
public class MinHeap extends AbstractBinaryHeap {

	public MinHeap(int initialSize) {
		super(initialSize);
	}

	@Override
	public double getValueByKey(int key) {
		int index = getIndexOfKey(key);
		
		if (index < 0)
			return Double.POSITIVE_INFINITY;
		
		return _item[index].value;
	}
	
	/**
	 * heapifyDown : mostly used on deletion or update at root node
	 */
	@Override
	protected void heapifyDown(int startIndex, final KeyValuePair entry) {
		int i = startIndex;
		int k = 2 * i + 1;	// child
		
		while (k < _count) {
			// choose min(right child, left child)
			if (k + 1 < _count && _item[k + 1].value < _item[k].value)
				k++;
			
			if (entry.value <= _item[k].value)
				break;
			
			_item[i] = _item[k];
			_indexOfKey[_item[i].key] = i;
			i = k;
			k = 2 * i + 1;
		}
		
		_item[i] = entry;
		_indexOfKey[entry.key] = i;
	}

	@Override
	protected void heapifyUp(int startIndex, final KeyValuePair entry) {
		int i = startIndex;
		int p = (i - 1) >> 1;   // p = parent
		
		while (p >= 0) {
			if (entry.value >= _item[p].value)
				break;

			_item[i] = _item[p];
			_indexOfKey[_item[i].key] = i;
			i = p;
			p = (i - 1) >> 1;
		}
		
		_item[i] = entry;
		_indexOfKey[entry.key] = i;
	}
}
