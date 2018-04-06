import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayDeque;

/*
INPUT:
5
4 2 1 3 5
Answer
3

Input
50
12 24 42 43 36 3 40 29 7 34 10 13 28 9 35 23 25 21 19 4 20 18 11 38 41 48 6 46 33 17 31 37 2 30 32 44 45 5 47 49 16 15 50 27 26 14 39 22 1 8
Output
7
Answer
13
 */
public class P340D_BubbleSortGraph_MIS_SLOW {
	private static BufferedReader br;
	private static PrintWriter pw;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String textLine = br.readLine();
		int n = Integer.parseInt(textLine);
		int[] a = new int[n];

		textLine = br.readLine();
		String[] splitted = textLine.split("\\s+");
		for (int i = 0; i < n; i++) {
			a[i] = Integer.parseInt(splitted[i]);
		}
		int[][] A = bubbleSortGraph(a);
		boolean[] banned = new boolean[n];		

		backupStack = new ArrayDeque<Integer>();
		int answer = maximumIndependentSetSize(A, banned, n - 1);
		pw.printf("%d\n", answer);
		pw.close();
	}

	private static int[][] bubbleSortGraph(int[] a) {
		boolean swapped;
		int n = a.length;
		int[][] adjacencyMatrix = new int[n][n];
		int lastI = n - 2;
		int u, v;
		do {
			swapped = false;
			for (int i = 0; i <= lastI; i++) {
				if (a[i] > a[i + 1]) {
					u = a[i];
					v = a[i + 1];
					adjacencyMatrix[u - 1][v - 1] = 1;
					adjacencyMatrix[v - 1][u - 1] = 1;
					a[i] = v;
					a[i + 1] = u;
					swapped = true;
				}
			}
		} while (swapped);
		
		return adjacencyMatrix;
	}
	
	private static ArrayDeque<Integer> backupStack;
	
	private static int maximumIndependentSetSize(int[][] A, boolean[] banned, int x) {
		if (x < 0) {
			return 0;
		}
		int result = 0;
		
		if (banned[x]) {
			result = maximumIndependentSetSize(A, banned, x - 1);
		} else {
			banned[x] = true;
			result = maximumIndependentSetSize(A, banned, x - 1);
			banned[x] = false;
		
			int pushCount = 0;
			for (int i = A.length - 1; i >= 0; i--) {
				if (!banned[i] && A[x][i] != 0) {
					banned[i] = true;
					backupStack.push(i);
					pushCount++;
				}
			}
			result = Math.max(result, 1 + maximumIndependentSetSize(A, banned, x - 1));
			while (pushCount-- > 0) {
				banned[backupStack.pop()] = false;
			}
		}
		
		return result;
	}	
}