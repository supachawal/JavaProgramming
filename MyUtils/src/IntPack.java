import java.util.Arrays;

public class IntPack implements Comparable<IntPack> {
	public int [] values;
	public IntPack(final int... initValues) {
		values = initValues.clone();
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		
		for (int i = 0, n = values.length; i < n; i++) {
			sb.append(String.format("%s%d", i > 0 ? "," : "", values[i]));
		}
		sb.append(')');
		return sb.toString();
	}
	@Override
	public int compareTo(IntPack o) {
		int ret = 0;
		int n = Math.min(values.length, o.values.length);
		
		for (int i = 0; i < n; i++) {
			int diff = this.values[i] - o.values[i];
			if (diff != 0) {
				ret = diff;
				break;
			}
		}
		
		if (ret == 0) {
			ret = values.length - o.values.length;
		}
		return ret;
	}
	
    public int hashCode() {
    	return Arrays.hashCode(values);
    }

    public boolean equals(Object o) {
        if (o instanceof IntPack) {
            return Arrays.equals(values, ((IntPack)o).values);
        }
        return false;
    }
}
