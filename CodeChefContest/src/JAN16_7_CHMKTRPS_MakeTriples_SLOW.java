import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

class JAN16_7_CHMKTRPS_MakeTriples_SLOW {
/*
Example

Input:
4
3 7 2 12 1 11 8 9 4 6 5 10

Output:
3
5 6 11 3 4 1 10 2 9

Input:
4
3 2 49 100 1 51 999 999 999 10000 10000 10000

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
		Pair<Long, Integer> max = makingTriples_maxFreqSum(entries, n, Math.min(5, n));
		if (max.value1 != null) {
			pw.printf("%d\n", max.value2 / 3);
			makingTriples(entries, n, max.value1);
		}
		pw.println();
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

	private static class Pair<T1, T2> {
		public T1 value1;
		public T2 value2;
	}
	
	private static Pair<Long, Integer> makingTriples_maxFreqSum(Entry[] entries, int n, int shortcutFreq) {
		int i, j, k;
		int lastI = n - 3, lastJ = n - 2;
		HashMap<Long, HashSet<Integer>> freqMap = new HashMap<Long, HashSet<Integer>>();
		int maxFreq = 0;
		long maxFreqSum = 0;
		Pair<Long, Integer> result = new Pair<Long, Integer>();
		
		for (i = 0; i <= lastI; i++) {
			for (j = i + 1; j <= lastJ; j++) {
				long sumIJ = (long)entries[i].value + entries[j].value;
				for (k = j + 1; k < n; k++) {
					long sumIJK = sumIJ + entries[k].value;
					
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
						if (maxFreqSum >= shortcutFreq) {
							i = lastI;
							j = lastJ;
							break;
						}
							
					}
				}
			}
		}

		result.value1 = maxFreqSum;
		result.value2 = maxFreq;
//		pw.printf("maxfreq = %d (sum=%d)\n", maxFreq, result);
		return result;
	}

	private static void makingTriples(Entry[] entries, int n, long paramSum) {
		int i, j, k;
		int lastI = n - 3, lastJ = n - 2;
		boolean everPrint = false;
		boolean[] used = new boolean[n];
		
		for (i = 0; i <= lastI; i++) {
			for (j = i + 1; j <= lastJ && !used[i]; j++) {
				if (!used[j]) {
					long sumIJ = (long)entries[i].value + entries[j].value;

					for (k = j + 1; k < n; k++) {
						if (!used[k]) {
							long sumIJK = sumIJ + entries[k].value;
//							if (sumIJK >= paramSum) {
								if (sumIJK == paramSum) {
									if (everPrint) {
										pw.print(' ');
									}
									pw.printf("%d %d %d", entries[i].index, entries[j].index, entries[k].index);
	//DEBUG:								pw.printf("%d %d %d", entries[i].value, entries[j].value, entries[k].value);
									everPrint = true;
									used[i] = true;
									used[j] = true;
									used[k] = true;
									break;
								} 
//								else {
//									j = lastJ;	// trick to exit loop as soon as possible because entries was sorted
//									i = lastI;
//								}
									
							}
//						}
					}
				}
			}
		}
	}
}
