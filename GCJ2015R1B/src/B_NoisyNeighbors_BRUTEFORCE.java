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
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class B_NoisyNeighbors_BRUTEFORCE {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
			inputFileName = "-small-practice.in";
//			inputFileName = "-large-practice.in";
			inputFileName = B_NoisyNeighbors_BRUTEFORCE.class.getSimpleName().substring(0, 1) + inputFileName;
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
			B_NoisyNeighbors_BRUTEFORCE solver = new B_NoisyNeighbors_BRUTEFORCE();
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
		int answer;
		int R, C, N;

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
				
				if (testCaseNumber == 83) {
					System.out.printf("");
				}
				iterationCounter = 0;
				answer = minimizedUnhappiness_BRUTEFORCE(R, C, N);
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

	@SuppressWarnings("unused")
	private static int minimizedUnhappiness_WRONG(int R, int C, int N) {
		int size = R * C;
		if (N > size) {
			return (R - 1) * C + R * (C - 1);
		}
		byte [] degrees = new byte[size];
		PriorityQueue<IntPairDesc> maxHeap = new PriorityQueue<IntPairDesc>();
		
		int i, j;
		int index;
		int d;
		// -------- initialize data structure -----------
		for (i = 0; i < R; i++) {
			for (j = 0; j < C; j++) {
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
				if (d > 0) {
					maxHeap.add(new IntPairDesc(d, index));
				}
			}
		}
		
		// ------- maximize degree removal ------------
		int count = size - N;
		System.out.printf("polling(%d)=<", count);
		int [] neighborMapping = {-1, 1, -C, C};
		while (count-- > 0 && !maxHeap.isEmpty()) {
			IntPairDesc p = maxHeap.poll();
			index = p.v2;
			int col = index % C;
			degrees[index] = 0;
			
			System.out.printf("%s,", p);

			for (i = 0; i < 4; i++) {
				int k = index + neighborMapping[i];
				
				if (k >= 0 && k < size && !(col == 0 && i == 0 || col == C - 1 && i == 1)) { 
					d = degrees[k];
					if (d > 0) {
						maxHeap.remove(new IntPairDesc(d, k));
						d--;
						if (d > 0) {
							maxHeap.add(new IntPairDesc(d, k));
						}
						degrees[k] = (byte)d;
					}
				}
			}
		}
		System.out.printf(">\n");
		
		return countUnhappiness(degrees, R, C);
	}

	private static int minimizedUnhappiness_BRUTEFORCE(int R, int C, int N) {
		int size = R * C;
		if (N > size) {
			return (R - 1) * C + R * (C - 1);
		}
		byte [] degrees = new byte[size];
		
		int i, j;
		int index;
		int d;
		// ------- try all combinations of removal ------------
		int count = size - N;
		List<TreeSet<Integer>> trialList = CommonUtils.genIndexCombination(size, count);
		int [] neighborMapping = {-1, 1, -C, C};
		int ret = Integer.MAX_VALUE;
		
		for (TreeSet<Integer> t : trialList) {
			// -------- initialize data structure -----------
			for (i = 0; i < R; i++) {
				for (j = 0; j < C; j++) {
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

			for (Integer I : t) {
				index = I.intValue();
				int col = index % C;
				degrees[index] = 0;
				
				for (i = 0; i < 4; i++) {
					int k = index + neighborMapping[i];
					
					if (k >= 0 && k < size && !(col == 0 && i == 0 || col == C - 1 && i == 1)) { 
						d = degrees[k];
						if (d > 0) {
							d--;
							degrees[k] = (byte)d;
						}
					}
				}
			}
			ret = Math.min(ret, countUnhappiness(degrees, R, C));
		}
		
		return ret;
	}
	
	private static int countUnhappiness(byte [] degrees, int R, int C) {
		int count = 0;
		int i, j, index;
		// count wall
		for (i = 0; i < R; i++) {
			for (j = 1; j < C; j++) {
				index = i * C + j;
				if (degrees[index - 1] > 0 && degrees[index] > 0) {
					count++;
				}
			}
		}
		
		// count floor/ceiling
		for (i = 1; i < R; i++) {
			for (j = 0; j < C; j++) {
				index = i * C + j;
				if (degrees[index - C] > 0 && degrees[index] > 0) {
					count++;
				}
			}
		}
		return count;
	}
}