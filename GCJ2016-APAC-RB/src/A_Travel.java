/* Problem A. Travel

There are N cities in Chelsea's state (numbered starting from 1, which is Chelsea's city), and M bidirectional roads directly connect them.
 (A pair of cities may even be directly connected by more than one road.) Because of changes in traffic patterns, 
 it may take different amounts of time to use a road at different times of day, depending on when the journey starts. 
 (However, the direction traveled on the road does not matter -- traffic is always equally bad in both directions!) 
 All trips on a road start (and end) exactly on the hour, and a trip on one road can be started instantaneously after finishing a trip on another road.

Chelsea loves to travel and is deciding where to go for her winter holiday trip. She wonders how quickly she can get from her city to 
various other destination cities, depending on what time she leaves her city. (Her route to her destination may include other intermediate cities on the way.) 
Can you answer all of her questions?

Input

The first line of the input gives the number of test cases, T. T test cases follow.

The first line of each test case contains three integers: the number N of cities, the number M of roads, and the number K of Chelsea's questions.

2M lines -- M pairs of two lines -- follow. 
In each pair, the first line contains two different integers x and y that describe one bidirectional road between the x-th city and the y-th city. 
The second line contains 24 integers Cost[t] (0 ≤ t ≤ 23) that indicate the time cost, in hours, to use the road when departing at t o'clock on that road. 
It is guaranteed that Cost[t] ≤ Cost[t+1]+1 (0 ≤ t ≤ 22) and Cost[23] ≤ Cost[0]+1.

Then, an additional K lines follow. Each contains two integers D and S that comprise a question: 
what is the fewest number of hours it will take to get from city 1 to city D, if Chelsea departs city 1 at S o'clock?

Output

For each test case, output one line containing "Case #x: ", where x is the case number (starting from 1), 
followed by K distinct space-separated integers that are the answers to the questions, in order. 
If Chelsea cannot reach the destination city for a question, no matter which roads she takes, then output -1 for that question.

Limits

1 ≤ x, y ≤ N.
1 ≤ all Cost values ≤ 50.
1 ≤ D ≤ N.
0 ≤ S ≤ 23.
Small dataset

1 ≤ T ≤ 100.
2 ≤ N ≤ 20.
1 ≤ M ≤ 100.
1 ≤ K ≤ 100.
Large dataset

1 ≤ T ≤ 5.
2 ≤ N ≤ 500.
1 ≤ M ≤ 2000.
1 ≤ K ≤ 5000.
Sample


Input 
 	
3
3 3 2
1 2
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1
1 3
3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 
2 3
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1
2 1
3 3
3 1 2
1 2
1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1
2 2
3 4
3 3 3
1 2
7 23 23 25 26 26 25 24 23 22 21 20 19 18 17 16 15 14 13 12 11 10 9 8
1 3
10 11 15 26 30 29 28 27 26 25 24 23 22 21 20 19 18 17 16 15 14 13 12 11
2 3
7 29 28 27 26 26 25 24 23 22 21 20 19 18 17 16 15 14 13 12 11 10 9 8
2 14
3 3
3 21


Output 
 
Case #1: 1 2
Case #2: 1 -1
Case #3: 17 26 13

*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class A_Travel {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
//			inputFileName = "-small-practice.in";
//			inputFileName = "-small-practice-1.in";
//			inputFileName = "-small-practice-2.in";
			inputFileName = "-large-practice.in";
			inputFileName = A_Travel.class.getSimpleName().substring(0, 1) + inputFileName;
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
			A_Travel solver = new A_Travel();
			solver.solve(inputFile, outputWriter);
			outputWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		CommonUtils.postamble();
	}

	@SuppressWarnings("unused")
	private long iterationCounter = 0;
	private static class TimeDependentWeightedRoad {
		public int anotherCity;
		public int[] costTable;
		public TimeDependentWeightedRoad(int anotherCity, int[] costTable) {
			this.anotherCity = anotherCity;
			this.costTable = costTable;
		}
	}
	
	public boolean solve(final File aFile, PrintWriter w) {
		String textLine;
		boolean result = false;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		String [] splitted;
		int nCities, nRoads, nQuestions, i, j, dest, startTime;
		int answer;
		ArrayList<ArrayList<TimeDependentWeightedRoad>> adjList;
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				if (testCaseNumber == 168) {
					System.out.printf("");
				}
				splitted = textLine.split("\\s+");
				nCities = Integer.parseInt(splitted[0]);
				nRoads = Integer.parseInt(splitted[1]);
				nQuestions = Integer.parseInt(splitted[2]);

				System.out.printf("Case #%d: #cities=%d, #roads=%d, #questions=%d, answer=", testCaseNumber, nCities, nRoads, nQuestions);
				
				adjList = new ArrayList<ArrayList<TimeDependentWeightedRoad>>(nCities);
				for (i = 0; i < nCities; i++) {
					adjList.add(new ArrayList<TimeDependentWeightedRoad>());
				}
				
				for (i = 0; i < nRoads; i++) {
					textLine = br.readLine();
					splitted = textLine.split("\\s+");
					int u = Integer.parseInt(splitted[0]) - 1;
					int v = Integer.parseInt(splitted[1]) - 1;
					textLine = br.readLine();
					splitted = textLine.split("\\s+");
					int[] costTable = new int[24];
					for (j = 0; j < 24; j++) {
						costTable[j] = Integer.valueOf(splitted[j]);
					}
					adjList.get(u).add(new TimeDependentWeightedRoad(v, costTable));
					adjList.get(v).add(new TimeDependentWeightedRoad(u, costTable));
				}
				
				w.printf("Case #%d:", testCaseNumber);
				for (i = 0; i < nQuestions; i++) {
					textLine = br.readLine();
					splitted = textLine.split("\\s+");
					dest = Integer.parseInt(splitted[0]) - 1;
					startTime = Integer.parseInt(splitted[1]);

//					iterationCounter = 0;
					answer = fewestNumberOfHours(nCities, adjList, dest, startTime);
					System.out.printf(" %d", answer);
					w.printf(" %d", answer);
				}

				System.out.println();
				w.println();
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

	private static final int INF = 999999999;
	
	private static int fewestNumberOfHours(int n, ArrayList<ArrayList<TimeDependentWeightedRoad>> adjList, int dest, int startTime) {
		// Modified Dijkstra's algorithm
		BinaryHeap.KeyedValue.valueComparator = (a, b) -> ((Integer)a).compareTo((Integer)b);
		ArrayList<BinaryHeap.KeyedValue<Integer>> dist = new ArrayList<BinaryHeap.KeyedValue<Integer>>(n);
		BinaryHeap<Integer> PQ = new BinaryHeap<Integer>(n);
		
		dist.add(new BinaryHeap.KeyedValue<Integer>(0, 0));
		PQ.insertItem(dist.get(0));
		for (int k = 1; k < n; k++) {
			dist.add(new BinaryHeap.KeyedValue<Integer>(k, INF));
			PQ.insertItem(dist.get(k));
		}

		while (PQ.getItemCount() > 0) {
			BinaryHeap.KeyedValue<Integer> u = PQ.popRoot();
			int u_key = u.getKey(); 
			if (u_key == dest) {	// reached goal
				break;
			}

			for (TimeDependentWeightedRoad v : adjList.get(u_key)) {
				int minCostToU_SoFar = dist.get(u_key).getValue();
				int alt = minCostToU_SoFar + v.costTable[CommonUtils.positiveMod(startTime + minCostToU_SoFar, 24)];
				if (alt < dist.get(v.anotherCity).getValue()) {
					PQ.updateItem(v.anotherCity, alt);
				}
			}
		}

		int ret = dist.get(dest).getValue();
		return ret == INF ? -1 : ret;
	}
}