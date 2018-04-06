import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class B_StableNeighbors_CANSOLVESMALLONLY {
	public static void main(String[] args) {
//		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
			inputFileName = "-sample3.in";
//			inputFileName = "-small-practice.in";
//			inputFileName = "-small-attempt0.in";
//			inputFileName = "-large-practice.in";
//			inputFileName = "-large.in";
			inputFileName = B_StableNeighbors_CANSOLVESMALLONLY.class.getSimpleName().substring(0, 1) + inputFileName;
		}
		
		if (args.length > 1) {
			outputFileName = args[1];
		} else {
			outputFileName = inputFileName.split("[.]in")[0].concat(".out");
		}

		System.out.printf("============ START %s --> %s ===========\n", inputFileName, outputFileName);
		
		File inputFile = new File(inputFileName);
		File outputFile = new File(outputFileName);
		
		try {
			PrintWriter outputWriter = new PrintWriter(outputFile, "ISO-8859-1");
			B_StableNeighbors_CANSOLVESMALLONLY solver = new B_StableNeighbors_CANSOLVESMALLONLY();
			solver.solve(inputFile, outputWriter);
			outputWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.printf("============ END ============\n");
//		CommonUtils.postamble();
	}

	public void solve(final File aFile, PrintWriter w) throws FileNotFoundException, IOException {
		String textLine;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		
		br = new BufferedReader(new FileReader(aFile));
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			String[] splitted = textLine.split("\\s+");
			System.out.printf("Case #%d: N=%s, R=%s, O=%s, Y=%s, G=%s, B=%s, V=%s\n", testCaseNumber, splitted[0], splitted[1], splitted[2], splitted[3], splitted[4], splitted[5], splitted[6]);
			w.printf("Case #%d:", testCaseNumber);

			int N = Integer.parseInt(splitted[0]);
			nodes = new int[N];
			int k = 0;
			for (int i = 0; i < 6; i++) {
				int nodeFreq = Integer.parseInt(splitted[i + 1]);
				for (int j = 0; j < nodeFreq; j++) {
					nodes[k++] = i;
				}
			}

			// maximum bipartite matching
			match = new int[N];
			Arrays.fill(match, -1);
			
			visited = new boolean[N];
			int nMatchings = 0;
			
			for (int u = 0; u < N; u++) {
				Arrays.fill(visited, false);
				if (dfsMaximizeMatching(u))		
					nMatchings++;
			}
			StringBuilder answer = new StringBuilder();
			if (nMatchings < N) {
				answer.append("IMPOSSIBLE");
			} else {
// BUG:				
//				int u = 0;
//				answer.append(colorNames[nodes[u]]);
//				for (u = match[u]; u > 0; u = match[u]) {
//					answer.append(colorNames[nodes[u]]);
//				}
				Arrays.fill(visited, false);
				int lastColorBits = 0;
				for (int u = 0; u < N; u++) {
					if (!visited[u]) {
						ArrayList<Integer> list = new ArrayList<Integer>();
						visited[u] = true;
						list.add(u);
						int v = u;
						while (!visited[match[v]]) {
							v = match[v];
							visited[v] = true;
							list.add(v);
						}

						int uColor = nodes[u];
						int vColor = nodes[v];
						if ((colorBits[uColor] & lastColorBits) == 0) {
//							answer.append('|');
							int n = list.size();
							for (k = 0; k < n; k++) {
								answer.append(colorNames[nodes[list.get(k)]]);
							}
							lastColorBits = colorBits[nodes[list.get(n - 1)]];
						} else if ((colorBits[vColor] & lastColorBits) == 0) {
//							answer.append('|');
							for (k = list.size() - 1; k >= 0; k--) {
								answer.append(colorNames[nodes[list.get(k)]]);
							}
							lastColorBits = colorBits[nodes[list.get(0)]];
						} else {
//							answer.append("|*");
							for (Integer nodeIndex : list) {
								answer.append(colorNames[nodes[nodeIndex]]);
							}
							lastColorBits = colorBits[vColor];
						}
					}
				}
			}
			
			System.out.printf("=> answer=%s\n", answer);
			w.printf(" %s\n", answer);
			
////////////////////////////////////////////////////////////////////////////////////
		}

		br.close();
	}

	char[] colorNames = {'R', 'O', 'Y', 'G', 'B', 'V' };
	int[] colorBits = {1, 3, 2, 6, 4, 5};
	int[] nodes;
	int[] match;
	boolean[] visited;

	public boolean dfsMaximizeMatching(int u) {
		int uColorBits = colorBits[nodes[u]];
		int n = nodes.length;
		int v;
		for (v = 0; v < n; v++) {
			if (!visited[v] && (colorBits[nodes[v]] & uColorBits) == 0) {
				visited[v] = true;
				if (match[v] < 0 || dfsMaximizeMatching(match[v])) {
					match[v] = u;
					return true;
				}
			}
		}
		return false;
	}
}