public abstract class AbstractDataStructure {
	protected long _statComputationCount;

	public AbstractDataStructure() {
		reset();
	}

	public void reset() {
		_statComputationCount = 0;
	}
	
	public long getComputationCount() {
		return _statComputationCount;
	}
	
}
