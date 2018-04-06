import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class JAN16_9_DMCS_DaiMaChuShi {
/*
Example

Input:
1
1

Output:
272

 */
	private static PrintWriter pw;
	@SuppressWarnings("unused")
	private static int[] brickVerCountTable = {0, 0, 1, 5, 13, 25};

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);

		int testCaseNumber = 0, testCaseCount;
		String textLine;
		int answer;
		int n;

//		for (int d = 2; d <= 5; d++) {
//			pw.printf("%d\n", brickPossibleVerticalCount(d));
//		}
//		pw.flush();
		
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			n = Integer.parseInt(textLine);
			brickCount_memoization = new Integer[n + 1];
			answer = brickCount5D(n);
			pw.printf("%d\n", answer);
		}
		pw.close();
	}
	
	private static Integer[] brickCount_memoization;
	@SuppressWarnings("unused")
	private static int brickCount2D(int n) {
		if (n == 0) {
			return 1;
		}
		
		if (brickCount_memoization[n] != null) {
			return brickCount_memoization[n];
		}
		
		long result = brickCount2D(n - 1);
		if (n > 1) {
			result += brickCount2D(n - 2);
		}
		brickCount_memoization[n] = (int)(result % 1000000007);
		return brickCount_memoization[n];
	}

	@SuppressWarnings("unused")
	private static int brickCount3D(int n) { // 2 * 2 * n
		if (n == 0) {
			return 1;
		}
		
		if (brickCount_memoization[n] != null) {
			return brickCount_memoization[n];
		}
		
		long result = 2L * brickCount3D(n - 1);
		if (n > 1) {
			result += 5L * brickCount3D(n - 2);
		}
		brickCount_memoization[n] = (int)(result % 1000000007);
		return brickCount_memoization[n];
	}
	
	private static int brickCount5D(int n) { // 2 * 2 * 2 * n
		if (n == 0) {
			return 1;
		}
		
		if (brickCount_memoization[n] != null) {
			return brickCount_memoization[n];
		}
		
		long result = 272 * brickCount5D(n - 1);
		if (n > 1) {
			result += 25L * brickCount5D(n - 2);
		}
		brickCount_memoization[n] = (int)(result % 1000000007);
		return brickCount_memoization[n];
	}

//	private static int brickHorizontalCount(int dimension, int i, int j) {
//		if (dimension <= 2 && i == 2 && j == 2) {
//			return 1;
//		}
//		
//		int result = brickHorizontalCount(dimension, i + 1, j);
//		result += brickHorizontalCount(dimension, i, j + 1);
//		
//		if (i < 2 && j < 2) {
//			result += dimension * brickHorizontalCount(dimension, i + 2, j);
//		}
//		
//		while (--dimension > 2) {
//			result += dimension * brickHorizontalCount(dimension, i, j);
//		}
//
//		return result;
//	}
//
//	private static int brickPossibleVerticalCount(int dimension /* 2 * 2 * n means 3-dimension*/) {
//		int count = 1, i;
//		
//		for (i = dimension - 1; i >= 2; i--) {
//			count += 4 * i - 4;
//		}
//		
//		return count;
//	}
}
