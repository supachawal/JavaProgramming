public class LongIntPair implements Comparable<LongIntPair> {
	public long v1;
	public long v2;
	public LongIntPair(final long initV1, final long initV2) {
		v1 = initV1;
		v2 = initV2;
	}
	public LongIntPair() {
		this(0, 0);
	}
	public String toString() {
		return String.format("v1=%d, v2=%d", v1, v2);
	}
	@Override
	public int compareTo(LongIntPair o) {
		return (int)(this.v1 == o.v1 ? (this.v2 - o.v2) : (this.v1 - o.v1));
	}
	
    public int hashCode() {
    	long u = v1 << 16;
        return (int)((u + v2) ^ ((u + v2) >>> 32));
    }

    public boolean equals(Object o) {
        if (o instanceof LongIntPair) {
            return this.compareTo((LongIntPair)o) == 0;
        }
        return false;
    }
}
