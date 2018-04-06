import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.PriorityQueue;

class CHRL4_ChefandWay {
	// https://www.codechef.com/problems/CHRL4
	// Note: Minimization of total cost that making decision of how long in each step (similar to ELECTRICITY of TOI but difference is SUM and MULTIPLY)
/*
Example

Input:
4 2
1 2 3 4

Output:
8
 */
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
			answer = minimizedCumulativeProductFromStartToEnd(A, N, K, 1000000007);
			
			pw.printf("%d\n", answer);
		}
		pw.close();
		br.close();
	}

	private static class SubOptimum implements Comparable<SubOptimum>{
		public double sumLog;
		public long product;
		public int index;
		
		public SubOptimum(double sumLog, long intProduct, int index) {
			this.sumLog = sumLog;
			this.product = intProduct;
			this.index = index;
		}
		@Override
		public int compareTo(SubOptimum o) {
			double diff = sumLog - o.sumLog;

			int result;
			if (diff > 1e-10) {
				result = 1;
			} else if (diff < -1e-10) {
				result = -1;
			} else {
				result = index - o.index;
			}

			return result;
		}
	}
	
	public static long minimizedCumulativeProductFromStartToEnd(int[] A, int n, int k, final int MOD) {
		PriorityQueue<SubOptimum> minHeap = new PriorityQueue<SubOptimum>();
		long intProduct = A[0];
		double sumLog = Math.log(intProduct);
		
		for (int i = 1; i < n; i++) {
			minHeap.add(new SubOptimum(sumLog, intProduct, i - 1));

			while (i - minHeap.peek().index > k)
				minHeap.poll();

			sumLog = minHeap.peek().sumLog + Math.log(A[i]);
			intProduct = (minHeap.peek().product * A[i]) % MOD;
		}
		
		return intProduct;
	}
}
