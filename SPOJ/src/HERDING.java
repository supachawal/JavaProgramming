import java.io.PrintWriter;
import java.util.Scanner;

class HERDING {
	/*
Input:
3 4
SWWW
SEWN
EEEN

Output:
2

	*/
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		PrintWriter pw = new PrintWriter(System.out);
		int answer;
		int m = input.nextInt();
		int n = input.nextInt();
		QuickUnionFind quf = new QuickUnionFind(m * n);
		
		for (int i = 0; i < m; i++) {
			String s = input.next();
			
			for (int j = 0; j < n; j++) {
				char c = s.charAt(j);
				int u = i*n + j;
				
				switch (c) {
					case 'N':
						if (i > 0) {
							quf.unite(u, u - n);
						}
						break;
					case 'S':
						if (i + 1 < m) {
							quf.unite(u, u + n);
						}
						break;
					case 'E':
						if (j + 1 < n) {
							quf.unite(u, u + 1);
						}
						break;
					case 'W':
						if (j > 0) {
							quf.unite(u, u - 1);
						}
						break;
				}
			}
		}

		answer = quf.nConnectedComponents;
		pw.println(answer);

		input.close();
		pw.close();
	}

    private static class QuickUnionFind {
		private int[] cp; // compressed parents
		public int[] connectedComponentSize;
		public int nConnectedComponents;

		public QuickUnionFind(int nVertices) {
			cp = new int[nVertices];
			connectedComponentSize = new int[nVertices];
			for (int i = 0; i < nVertices; i++) {
				cp[i] = i;
				connectedComponentSize[i] = 1;
			}
			nConnectedComponents = nVertices;
		}

		public int root(int i) {
			while (i != cp[i]) {
				cp[i] = cp[cp[i]];	// path compression improvement
				i = cp[i];
			}
			return i;
		}

		public boolean unite(int nodeIndex1, int nodeIndex2) {
			int r1 = root(nodeIndex1);
			int r2 = root(nodeIndex2);

			if (r1 != r2) {
				int ccSize1 = connectedComponentSize[r1];
				int ccSize2 = connectedComponentSize[r2];
				int ccUnitedSize = ccSize1 + ccSize2;

				if (ccSize1 <= ccSize2) {	// weighted tree size improvement
					cp[r1] = r2;
					connectedComponentSize[r2] = ccUnitedSize;
				} else {
					cp[r2] = r1;
					connectedComponentSize[r1] = ccUnitedSize;
				}

				nConnectedComponents--;
				return true;
			}
			
			return false;
		}
	}
}
