public class IntPairDesc extends IntPair {
	public IntPairDesc(final int initV1, final int initV2) {
		v1 = initV1;
		v2 = initV2;
	}
	public IntPairDesc() {
		this(0, 0);
	}
	@Override
	public int compareTo(IntPair o) {
		return this.v1 == o.v1 ? (o.v2 - this.v2) : (o.v1 - this.v1);
	}
}
