import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.TreeSet;

class JAN16_7_CHMKTRPS_MakeTriples_WRONG {
/*
Example

Input:
4
3 7 2 12 1 11 8 9 4 6 5 10

Output:
3
5 6 11 3 4 1 10 2 9

Input:
2
3 2 49 100 1 51

Output:
2
5 2 4 1 3 6

*/
	private static PrintWriter pw;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);

		String textLine;
		String[] splitted;
		
		textLine = br.readLine();
		int N = Integer.parseInt(textLine);
		int n = 3 * N;
		TreeSet<Entry> numSet = new TreeSet<Entry>(); 
		int i;
		
		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		for (i = 0; i < n; i++) {
			numSet.add(new Entry(Integer.parseInt(splitted[i]), i + 1));
		}
		Entry[] entries = numSet.toArray(new Entry[0]);
		numSet = null;
		n = entries.length;
		if (n >= 3) {
			long maxFreqSum = makingTriples_maxFreqSum_APPROX(entries, n);
			ArrayList<Integer> list = makingTriples(entries, n, maxFreqSum);
			int freq = list.size();
			pw.printf("%d\n%d", freq / 3, list.get(0));
			for (i = 1; i < freq; i++) {
				pw.printf(" %d", list.get(i));
			}
			pw.println();
		} else {
			pw.println("0");
		}
		
		pw.close();
	}

	private static class Entry implements Comparable<Entry>{
		public int value;
		public int index;
		public Entry(int value, int index) {
			this.value = value;
			this.index = index;
		}
		@Override
		public boolean equals(Object obj) {
			return value == ((Entry)obj).value;
		}
		@Override
		public int hashCode() {
			return value;
		}
		@Override
		public int compareTo(Entry o) {
			return value - o.value;
		}
		@Override
		public String toString() {
			return String.format("%d(%d)", value, index);
		}
	}

	private static long makingTriples_maxFreqSum_APPROX(Entry[] entries, int n) {
		int i, j, k;
		HashMap<Long, HashSet<Integer>> freqMap = new HashMap<Long, HashSet<Integer>>();
		long maxFreq = 0;
		long maxFreqSum = 0;
		Random rnd = new Random();
		long iterations = n * n;
		for (long r = 0; r < iterations; r++) {
			i = rnd.nextInt(n);

			j = rnd.nextInt(n);
			while (j == i)
				j = rnd.nextInt(n);

			k = rnd.nextInt(n);
			while (k == i || k == j)
				k = rnd.nextInt(n);
			
			long sumIJK = (long)entries[i].value + entries[j].value + entries[k].value;
			HashSet<Integer> indexSet = freqMap.get(sumIJK);
			if (indexSet == null) {
				indexSet = new HashSet<Integer>(n);
				indexSet.add(i);
				indexSet.add(j);
				indexSet.add(k);
				freqMap.put(sumIJK, indexSet);
			} else if (!indexSet.contains(i) && !indexSet.contains(j) && !indexSet.contains(k)) {
				indexSet.add(i);
				indexSet.add(j);
				indexSet.add(k);
			}

			if (maxFreq < indexSet.size()) {
				maxFreq = indexSet.size();
				maxFreqSum = sumIJK;
			}
		}
		
//		pw.printf("maxfreq = %d (sum=%d)\n", maxFreq, maxFreqSum);
		return maxFreqSum;
	}

	private static ArrayList<Integer> makingTriples(Entry[] entries, int n, long paramSum) {
		int i, j, k;
		int lastI = n - 3, lastJ = n - 2;
		boolean[] used = new boolean[n];
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		for (i = 0; i <= lastI; i++) {
			for (j = i + 1; j <= lastJ && !used[i]; j++) {
				if (!used[j]) {
					long sumIJ = (long)entries[i].value + entries[j].value;
					for (k = j + 1; k < n; k++) {
						if (!used[k]) {
							long sumIJK = sumIJ + entries[k].value;
							if (sumIJK == paramSum) {
								result.add(entries[i].index);
								result.add(entries[j].index);
								result.add(entries[k].index);
								used[i] = true;
								used[j] = true;
								used[k] = true;
								break;
							}
						}
					}
				}
			}
		}
		return result;
	}
}
