import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.PriorityQueue;

class CHRL4_ChefandWay_SLOW {
	// https://www.codechef.com/problems/CHRL4
	// Note: Minimization of total cost that making decision of how long in each step (similar to ELECTRICITY of TOI)
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		String textLine;
		String[] splitted;
		long answer;
		int i;

		int N;
		int K;
		int[] A;
		
		if ((textLine = br.readLine()) != null) {
			splitted = textLine.split("\\s+");
			N = Integer.parseInt(splitted[0]);
			K = Integer.parseInt(splitted[1]);
			A = new int[N]; 
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			for (i = 0; i < N; i++) {
				A[i] = Integer.parseInt(splitted[i]);
			}

//			if (testCaseNumber == 6) {
//				System.gc();
//			}
			answer = minimizedCumulativeProductFromStartToEnd(A, N, K);
			
			pw.printf("%d\n", answer);
		}
		pw.close();
		br.close();
	}

	public static final BigInteger MOD = new BigInteger("1000000007");
//	public static final String MOD = "1000000007";
	
	private static class SubOptimum implements Comparable<SubOptimum>{
		public BigInteger key;
		public int index;
		
		public SubOptimum(BigInteger key, int index) {
			this.key = key;
			this.index = index;
		}
		@Override
		public int compareTo(SubOptimum o) {
			return this.key.compareTo(o.key);
		}
		
	}
	public static long minimizedCumulativeProductFromStartToEnd(int[] A, int n, int k) {
		SubOptimum minPreviousK;
		PriorityQueue<SubOptimum> minHeap = new PriorityQueue<SubOptimum>();
		BigInteger cum = new BigInteger(String.valueOf(A[0]));
		
		for (int i = 1; i < n; i++) {
			minHeap.add(new SubOptimum(cum, i - 1));
			minPreviousK = minHeap.poll();
			while (i - minPreviousK.index > k)
				minPreviousK = minHeap.poll();

			minHeap.add(minPreviousK);
			cum = minPreviousK.key.multiply(new BigInteger(String.valueOf(A[i])));
		}
		return cum.mod(MOD).longValue();
	}

}
