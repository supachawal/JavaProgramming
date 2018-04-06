import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class B_StableNeighbors_BUG_MAXMATCHING {
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
			inputFileName = B_StableNeighbors_BUG_MAXMATCHING.class.getSimpleName().substring(0, 1) + inputFileName;
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
			B_StableNeighbors_BUG_MAXMATCHING solver = new B_StableNeighbors_BUG_MAXMATCHING();
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

	private char[] colorNames = {'R', 'O', 'Y', 'G', 'B', 'V' };
	private int[] colorBits = {1, 3, 2, 6, 4, 5};
	private ArrayList<ArrayList<Integer>> nodes;
	private int[] match;
	private boolean[] visited;

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
			nodes = new ArrayList<ArrayList<Integer>>();
			for (int i = 0; i < 6; i++) {
				int nodeFreq = Integer.parseInt(splitted[i + 1]);
				for (int j = 0; j < nodeFreq; j++) {
					ArrayList<Integer> newNode = new ArrayList<Integer>();
					newNode.add(i);
					nodes.add(newNode);
				}
			}

			// maximum bipartite matching
			boolean completeMatching = true;
			while ((N = nodes.size()) > 1 && completeMatching) {
				match = new int[N];
				Arrays.fill(match, -1);
				visited = new boolean[N];

				completeMatching = false;
				for (int u = 0; u < N; u++) {
					Arrays.fill(visited, false);
					if (match[u] < 0) {
						visited[u] = true;
						if (dfsMaximizeMatching(u))	{
							completeMatching = true;
						}
					}
				}
				
				if (completeMatching) {
					ArrayList<ArrayList<Integer>> newNodes = new ArrayList<ArrayList<Integer>>();
					Arrays.fill(visited, false);
					for (int u = 0; u < N; u++) {
						if (!visited[u]) {
							visited[u] = true;
							ArrayList<Integer> newNode = new ArrayList<Integer>();
							newNodes.add(newNode);
							newNode.addAll(nodes.get(u));
							int v = match[u];
							if (v >= 0) {
								visited[v] = true;
								ArrayList<Integer> uColors = nodes.get(u);
								int uLeftCBits = colorBits[uColors.get(0)];
								int uRightCBits = colorBits[uColors.get(uColors.size() - 1)];
								ArrayList<Integer> vColors = nodes.get(v);
								int vLeftCBits = colorBits[vColors.get(0)];
								int vRightCBits = colorBits[vColors.get(vColors.size() - 1)];
								if (!((uRightCBits & vLeftCBits) == 0 && (vRightCBits & uLeftCBits) == 0)
										&& (uLeftCBits & vLeftCBits) == 0 && (vRightCBits & uRightCBits) == 0) {
									Collections.reverse(vColors);
								}
								
								newNode.addAll(vColors);
							}
						}
					}
					
					nodes = newNodes;
				}
			} 

			StringBuilder answer = new StringBuilder();
			if (!completeMatching) {
				answer.append("IMPOSSIBLE");
			} else {
				for (Integer c : nodes.get(0)) {
					answer.append(colorNames[c]);
				}
			}
			
			System.out.printf("=> answer=%s\n", answer);
			w.printf(" %s\n", answer);
			
////////////////////////////////////////////////////////////////////////////////////
		}

		br.close();
	}

	public boolean dfsMaximizeMatching(int u) {
		ArrayList<Integer> uColors = nodes.get(u);
		int uLeftCBits = colorBits[uColors.get(0)];
		int uRightCBits = colorBits[uColors.get(uColors.size() - 1)];
		int n = nodes.size();

		for (int v = 0 ; v < n; v++) {
			if (v != u && !visited[v]) {
				ArrayList<Integer> vColors = nodes.get(v);
				int vLeftCBits = colorBits[vColors.get(0)];
				int vRightCBits = colorBits[vColors.get(vColors.size() - 1)];
				if ((uRightCBits & vLeftCBits) == 0 && (vRightCBits & uLeftCBits) == 0
						|| (uLeftCBits & vLeftCBits) == 0 && (vRightCBits & uRightCBits) == 0) {
					visited[v] = true;
					if (match[v] < 0 || dfsMaximizeMatching(match[v])) {
						match[v] = u;
						match[u] = v;
						return true;
					}
				}
			}
		}
		return false;
	}
}