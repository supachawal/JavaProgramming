public class UnorderedIntPair {
	public int v1;
	public int v2;

	public UnorderedIntPair(final int initV1, final int initV2) {
		v1 = initV1;
		v2 = initV2;
	}

	public UnorderedIntPair() {
		this(0, 0);
	}
	
	public String toString() {
		return String.format("(%d,%d)", v1, v2);
	}

	public int hashCode() {
    	long value = (long)Math.min(v1, v2) << 32 | Math.max(v1, v2); 
    	return (int)(value ^ (value >>> 32));
    }

    public boolean equals(Object o) {
        if (o instanceof UnorderedIntPair) {
            return this.v1 + this.v2 == ((UnorderedIntPair)o).v1 + ((UnorderedIntPair)o).v2;
        }
        return false;
    }
}
