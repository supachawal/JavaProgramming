import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class P103_StackingBoxes {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		String textLine;
		String[] splitted;
		int answer;
		int i, j;
		
		int k;	// # of boxes
		int n;  // # of dimensions
		ArrayList<IntValuePack> boxes = new ArrayList<IntValuePack>();

		while ((textLine = br.readLine()) != null) {
			if (textLine.length() == 0)
				break;

			splitted = textLine.split("\\s+");
			k = Integer.parseInt(splitted[0]);
			n = Integer.parseInt(splitted[1]);
			boxes.clear();
			for (i = 0; i < k; i++) {
				textLine = br.readLine();
				splitted = textLine.split("\\s+");
				IntValuePack abox = new IntValuePack(splitted);
				abox.id = i + 1;
				abox.descendingSort();
				boxes.add(abox);
			}
			
			Collections.sort(boxes);
			maxNesting_cachedResult = new int[k];
			answer = 1 + maxNesting(boxes, k - 1);
			pw.printf("%d\n", answer);
		}
		
		pw.close();
	}
	static int[] maxNesting_cachedResult;
	
	static int maxNesting(ArrayList<IntValuePack> boxes, int current) {
		if (current <= 0) {
			return 0;
		}
		
		int result1 = maxNesting(boxes, current - 1);
		int result2 = 0;
		int i;
		for (i = current - 1; i >= 0; i--) {
			if (boxes.get(i).canNest(boxes.get(current))) {
				result2 = 1 + maxNesting(boxes, i);
				break;
			}
		}
		
		return Math.max(result1, result2);
	}
	
	static class IntValuePack implements Comparable<IntValuePack> {
		public int id;
		public int [] values;
		public IntValuePack(final int... initValues) {
			values = initValues.clone();
		}
		public IntValuePack(final String[] initValues) {
			int n = initValues.length;
			values = new int[n];
			for (int i = 0; i < n; i++) {
				values[i] = Integer.parseInt(initValues[i]);
			}
		}
		public void descendingSort() {
			int n = values.length;
			for (int i = 0; i < n; i++) {
				values[i] = -values[i];
			}
		
			Arrays.sort(values);

			for (int i = 0; i < n; i++) {
				values[i] = -values[i];
			}
		}
		public boolean canNest(IntValuePack o) {
			// smaller can nest in bigger
			boolean can = true;
			for (int i = 0, n = values.length; i < n; i++) {
				if (this.values[i] >= o.values[i]) {
					can = false;
					break;
				}
			}
		
			return can;
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
		public int compareTo(IntValuePack o) {
			int ret = 0;
			for (int i = 0, n = values.length; i < n; i++) {
				int diff = this.values[i] - o.values[i];
				if (diff != 0) {
					ret = diff;
					break;
				}
			}
			return ret;
		}
		
	    public int hashCode() {
	    	return Arrays.hashCode(values);
	    }

	    public boolean equals(Object o) {
	        if (o instanceof IntValuePack) {
	            return this.compareTo((IntValuePack)o) == 0;
	        }
	        return false;
	    }
	}
}
