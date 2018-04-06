/* Problem A. Noisy Neighbors

You are a landlord who owns a building that is an R x C grid of apartments; each apartment is a unit square cell with four walls. 
You want to rent out N of these apartments to tenants, with exactly one tenant per apartment, and leave the others empty. 
Unfortunately, all of your potential tenants are noisy, so whenever any two occupied apartments share a wall (and not just a corner), 
this will add one point of unhappiness to the building. For example, a 2x2 building in which every apartment is occupied has four walls 
that are shared by neighboring tenants, and so the building's unhappiness score is 4.

If you place your N tenants optimally, what is the minimum unhappiness value for your building?

Input

The first line of the input gives the number of test cases, T. T lines follow; each contains three space-separated integers: R, C, and N.

Output

For each test case, output one line containing "Case #x: y", where x is the test case number (starting from 1) 
and y is the minimum possible unhappiness for the building.

Limits
1 ≤ T ≤ 1000.
0 ≤ N ≤ R*C.
Small dataset: 1 ≤ R*C ≤ 16.
Large dataset: 1 ≤ R*C ≤ 10000.

Sample

Input 
 	
4
2 3 6
4 1 2
3 3 8
5 2 0

Output 
 
Case #1: 7
Case #2: 0
Case #3: 8
Case #4: 0

In Case #1, every room is occupied by a tenant and all seven internal walls have tenants on either side.

In Case #2, there are various ways to place the two tenants so that they do not share a wall. One is illustrated below.

In Case #3, the optimal strategy is to place the eight tenants in a ring, leaving the middle apartment unoccupied.

Here are illustrations of sample cases 1-3. Each red wall adds a point of unhappiness.
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class B_NoisyNeighbors_TUNED {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
//			inputFileName = "-small-practice.in";
			inputFileName = "-large-practice.in";
			inputFileName = B_NoisyNeighbors_TUNED.class.getSimpleName().substring(0, 1) + inputFileName;
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
			B_NoisyNeighbors_TUNED solver = new B_NoisyNeighbors_TUNED();
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
	private int R;  //Rows
	private int C;	//Columns
	private byte [] degrees; // tenants graph degrees (virtually 2D)
	private int [] mappingDirection;
	private int APARTMENT_SIZE;
	private int maxUnhappinessValue;
//FAILED TO USE (BUGGY):	HashMap<IntValuePair, Integer> memoization = new HashMap<IntValuePair, Integer>(8192);

	public boolean solve(final File aFile, PrintWriter w) {
		String textLine;
		boolean result = false;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		String [] splitted;
		int answer;
		int N;

		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				splitted = textLine.split("\\s+");
				
				R = Integer.valueOf(splitted[0]);
				C = Integer.valueOf(splitted[1]);
				N = Integer.valueOf(splitted[2]);
				
				System.out.printf("Case #%d: R=%d, C=%d, N=%d, ", testCaseNumber, R, C, N);
				
				if (testCaseNumber == 7) {
					System.out.printf("");
				}
				iterationCounter = 0;
				degrees = initializedGraphDegrees(R, C, N);
				mappingDirection = new int [] {-1, 1, -C, C};
				APARTMENT_SIZE = R * C;
				maxUnhappinessValue = (R - 1) * C + R * (C - 1);
//BUGGY:				memoization.clear();
				answer = maxUnhappinessValue - maximizedHappiness(APARTMENT_SIZE - N, APARTMENT_SIZE - 1);
				System.out.printf("answer=%d, iterations=%d\n", answer, iterationCounter);

				w.printf("Case #%d: %d\n", testCaseNumber, answer);
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

	/**
	 * Dynamic Programming algorithm
	 * @param R : Rows
	 * @param C : Columns
	 * @param N : Number of tenants
	 * @param degrees (logical 2D array)
	 * @return Unhappiness caused by shared wall/floor/ceiling
	 */
	private int maximizedHappiness(int NE, int targetIndex) {
		if (NE == 0) {
			return 0;
		}

		if (targetIndex < 0) {
			return -1;
		}
// can't use memoization because the overlapped subproblems have dependencies with the prior iterations (neighborhood)		
//		IntValuePair memoKey = new IntValuePair(NE, targetIndex);
//		Integer shortcut = memoization.get(memoKey); 
//		if (shortcut != null) {
//			return shortcut;
//		}
		iterationCounter++;
		int branch1 = maximizedHappiness(NE, targetIndex - 1);
		
		int branch2 = deallocateCell(targetIndex) + maximizedHappiness(NE - 1, targetIndex - 1);
		allocateCell(targetIndex);

//		memoization.put(memoKey, Math.max(branch1, branch2));
		return Math.max(branch1, branch2);
	}

	private int deallocateCell(int targetIndex) {
		int col = targetIndex % C;
		int neighborCount = 0;
		degrees[targetIndex] = -1;
		
		for (int i = 0; i < 4; i++) {
			int k = targetIndex + mappingDirection[i];
			
			if (k >= 0 && k < APARTMENT_SIZE && !(col == 0 && i == 0 || col == C - 1 && i == 1)) { 
				int d = degrees[k];
				if (d > 0) {
					d--;
					degrees[k] = (byte)d;
					neighborCount++;
				}
			}
		}

		return neighborCount;
	}

	private int allocateCell(int targetIndex) {
		int col = targetIndex % C;
		int neighborCount = 0;
		
		for (int i = 0; i < 4; i++) {
			int k = targetIndex + mappingDirection[i];
			
			if (k >= 0 && k < APARTMENT_SIZE && !(col == 0 && i == 0 || col == C - 1 && i == 1)) { 
				int d = degrees[k];
				if (d >= 0) {
					d++;
					degrees[k] = (byte)d;
					neighborCount++;
				}
			}
		}

		degrees[targetIndex] = (byte)neighborCount;
		return neighborCount;
	}
	
	private static byte [] initializedGraphDegrees(int R, int C, int N) {
		int size = R * C;
		byte [] degrees = new byte[size];
		
		int index;
		int d;
		// -------- initialize data structure -----------
		for (int i = 0; i < R; i++) {
			for (int j = 0; j < C; j++) {
				index = i * C + j;
				d = 4;
				if (i == 0) {
					d--;
				}
				if (i == R - 1) {
					d--;
				}
				if (j == 0) {
					d--;
				}
				if (j == C - 1) {
					d--;
				}
				degrees[index] = (byte)d;
			}
		}
		
		return degrees;
	}
}