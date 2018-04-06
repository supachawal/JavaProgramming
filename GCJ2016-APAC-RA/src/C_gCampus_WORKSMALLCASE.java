/* Problem C. gCampus (all-pairs shortest path)

Company G has a main campus with N offices (numbered from 0 to N - 1) and M bidirectional roads 
(numbered from 0 to M - 1). The ith road connects a pair of offices (Ui, Vi), and it takes Ci minutes to travel on it 
(in either direction). 

A path between two offices X and Y is a series of one or more roads that starts at X and ends at Y. The time taken to 
travel a path is the sum of the times needed to travel each of the roads that make up the path. (It's guaranteed that 
there is at least one path connecting any two offices.) 

Company G specializes in efficient transport solutions, but the CEO has just realized that, embarrassingly enough, 
its own road network may be suboptimal! She wants to know which roads in the campus are inefficient. A road is 
inefficient if and only if it is not included in any shortest paths between any offices. 

Given the graph of offices and roads, can you help the CEO find all of the inefficient roads? 

Input
The first line of the input gives the number of test cases, T. T test cases follow. Each case begins with one line with 
two integers N and M, indicating the number of offices and roads. This is followed by M lines containing three integers
each: Ui, Vi and Ci, indicating the ith road is between office Ui and office Vi, and it takes Ci minutes to travel on it. 

Output
For each test case, output one line containing "Case #x:", where x is the test case number (starting from 1). 
Then output the road numbers of all of the inefficient roads, in increasing order, each on its own line. (Note that road 
0 refers to the first road listed in a test case, road 1 refers to the second road, etc.) 

Limits
0 < Ci ≤ 1000000. 

Small dataset
1 ≤ T ≤ 10.
1 ≤ N = M ≤ 100.

Large dataset
1 ≤ T ≤ 3.
1 ≤ N ≤ 100.
1 ≤ M ≤ 10000.

Sample
Input 
2
3 3
0 1 10
1 2 3
2 0 3
3 3
0 1 10
1 2 3
2 1 3

Output 
Case #1:
0
Case #2:

*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class C_gCampus_WORKSMALLCASE {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
			inputFileName = "-small-practice.in";
//			inputFileName = "-small-practice-1.in";
//			inputFileName = "-small-practice-2.in";
//			inputFileName = "-large-practice.in";
			inputFileName = C_gCampus_WORKSMALLCASE.class.getSimpleName().substring(0, 1) + inputFileName;
		}
		
		if (args.length > 1) {
			outputFileName = args[1];
		} else {
			outputFileName = inputFileName.split("[.]in")[0].concat(".out");
		}

		System.out.printf("%s --> %s\n", inputFileName, outputFileName);
		
		File inputFile = new File(inputFileName);
		File outputFile = new File(outputFileName);
		
		try {
			PrintWriter outputWriter = new PrintWriter(outputFile, "ISO-8859-1");
			C_gCampus_WORKSMALLCASE solver = new C_gCampus_WORKSMALLCASE();
			solver.solve(inputFile, outputWriter);
			outputWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		CommonUtils.postamble();
	}

	private long iterationCounter = 0;
	
	public boolean solve(final File aFile, PrintWriter w) {
		String textLine;
		boolean result = false;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		String [] splitted;
		StringBuilder answer;
		int officeCount, roadCount;
		long [][] adjMatrix;
		int i, j;
		ArrayList<IntPack> roadList = new ArrayList<IntPack>();
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				if (testCaseNumber == 10) {
					System.out.printf("");
				}
				splitted = textLine.split("\\s+");
				officeCount = Integer.parseInt(splitted[0]);
				roadCount = Integer.parseInt(splitted[1]);
				adjMatrix = new long[officeCount][officeCount];
				for (i = 0; i < officeCount; i++) {
					for (j = 0; j < officeCount; j++) {
						adjMatrix[i][j] = (i == j ? 0L : CommonUtils.LONG_INFINITY);
					}					
				}
				
				roadList.clear();
				for (i = 0; i < roadCount; i++) {
					textLine = br.readLine();
					splitted = textLine.split("\\s+");
					int u = Integer.parseInt(splitted[0]);
					int v = Integer.parseInt(splitted[1]);
					int c = Integer.parseInt(splitted[2]);
//					if (u == v && c > 0) {
//						br.close();
//						throw new AssertionError();
//					}
					adjMatrix[u][v] = c;
					adjMatrix[v][u] = c;
					roadList.add(new IntPack(u, v, c));
				}

				System.out.printf("Case #%d: #offices=%d, #roads=%d, ", testCaseNumber, officeCount, roadCount);
				iterationCounter = 0;

				answer = inefficientRoadList_FloydWarshall(adjMatrix, officeCount, roadList);
				
				System.out.printf("answer=%s (iterations=%d)\n", answer, iterationCounter);

				w.printf("Case #%d: %s\n", testCaseNumber, answer);
////////////////////////////////////////////////////////////////////////////////////
			}

			br.close();
			result = true;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public StringBuilder inefficientRoadList_FloydWarshall(long[][] a, int n, ArrayList<IntPack> roadList) {
		StringBuilder result = new StringBuilder();
		int i, j, k;

		for (k = 0; k < n; k++) {
			for (i = 0; i < n; i++) {
				for (j = 0; j < n; j++) {
					iterationCounter++;
					long newD = (i == j ? 0L : a[i][k] + a[k][j]);					
					if (newD < a[i][j]) {
						a[i][j] = newD;
					}
				}
			}
		}

		n = roadList.size();
		for (k = 0; k < n; k++) {
			int [] v = roadList.get(k).values;
			if (a[v[0]][v[1]] < v[2]) {
				result.append('\n').append(k);
			}
		}
		return result;
	}
}