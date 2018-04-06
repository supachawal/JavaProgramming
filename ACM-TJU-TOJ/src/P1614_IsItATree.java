import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;

class P1614_IsItATree {
/*
Sample Input
6 8 5 3 5 2 6 4
5 6 0 0

8 1 7 3 6 2 8 9 7 5
7 4 7 8 7 6 0 0

3 8 6 8 6 4
5 3 5 6 5 2 0 0
-1 -1

Sample Output
Case 1 is a tree.
Case 2 is a tree.
Case 3 is not a tree.
*/
	private static PrintWriter pw = null;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		int testCaseNumber = 0;
		Integer u, v;
		boolean endInput = false;
		HashMap<Integer, Integer> inDeg = new HashMap<Integer, Integer>();
		HashSet<Integer> distinctNodeId = new HashSet<Integer>();
		while (!endInput) {
			testCaseNumber++;
			boolean isATree = true;
			boolean endTestCase = false;
			
			inDeg.clear();
			distinctNodeId.clear();
			while (!endTestCase) {
				u = getInt(br, -1);
				v = getInt(br, -1);
					
				if (u <= 0 && v <= 0) {
					if (u < 0 && v < 0) {
						endInput = true;
					} else {
						endTestCase = true;
					}
					break;
				}
					
				if (u > 0) {
					distinctNodeId.add(u);
				}
				if (v > 0) {
					distinctNodeId.add(v);
				}

				if (v.equals(u)) {
					isATree = false;
				}
				
				if (isATree) {
					if (u > 0) {
						Integer vParent = inDeg.put(v, u);
						if (vParent != null && !vParent.equals(u)) {
							isATree = false;
						}
					}
				}
			}
			
			if (endTestCase) {
				if (isATree) {
					isATree = (inDeg.size() == distinctNodeId.size() - 1);
				}
	
				pw.printf("Case %d is%s a tree.\n", testCaseNumber, isATree ? "" : " not");
//				pw.printf("Case %d is%s a tree. (m=%d, n=%d)\n", testCaseNumber, isATree ? "" : " not", inDeg.size(), distinctNodeId.size());
			}
		}
		pw.close();
	}
	
	private static int getInt(BufferedReader br, int defaultValue) {
		int ret = defaultValue;
		StringBuilder sb = new StringBuilder();
		
		int c, prev_c = 0;
		
		do {
			try {
				c = br.read();
			} catch (IOException e) {
				break;
			}
			
			if (c < 0)
				break;

			if (Character.isDigit(c) || (sb.length() == 0 && (c == '+' || c == '-'))) {
				sb.appendCodePoint(c);
				prev_c = c;
			} else if (sb.length() > 0 && !(prev_c == '+' || prev_c == '-')) {
				break;
			}
					
		} while (true);
		
		if (sb.length() > 0) {
			ret = Integer.parseInt(sb.toString());
		}
		
		return ret;
	}
}

