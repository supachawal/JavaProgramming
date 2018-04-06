public class IntPair implements Comparable<IntPair> {
	public int v1;
	public int v2;
	public IntPair(final int initV1, final int initV2) {
		v1 = initV1;
		v2 = initV2;
	}
	public IntPair() {
		this(0, 0);
	}
	public String toString() {
		return String.format("(%d,%d)", v1, v2);
	}
	@Override
	public int compareTo(IntPair o) {
		return this.v1 == o.v1 ? (this.v2 - o.v2) : (this.v1 - o.v1);
	}
	
    public int hashCode() {
    	long value = (long)v1 << 32 | v2; 
    	return (int)(value ^ (value >>> 32));
    }

    public boolean equals(Object o) {
        if (o instanceof IntPair) {
            return this.compareTo((IntPair)o) == 0;
        }
        return false;
    }
}
