public class KeyValuePair {
	public int key;
	public double value;
	public KeyValuePair(final int initKey, final double initValue) {
		key = initKey;
		value = initValue;
	}
	public KeyValuePair() {
		this(0, 0.0);
	}
	public String toString() {
		return String.format("key=%d, value=%.2f", key, value);
	}
}
