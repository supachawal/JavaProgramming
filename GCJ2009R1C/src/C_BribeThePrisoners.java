/* Problem C. Bribe the Prisoners
In a kingdom there are prison cells (numbered 1 to P) built to form a straight line segment. 
Cells number i and i+1 are adjacent, and prisoners in adjacent cells are called "neighbours." 
A wall with a window separates adjacent cells, and neighbours can communicate through that window.

All prisoners live in peace until a prisoner is released. When that happens, 
the released prisoner's neighbours find out, and each communicates this to his other neighbour. 
That prisoner passes it on to his other neighbour, and so on until they reach a prisoner with no other neighbour 
(because he is in cell 1, or in cell P, or the other adjacent cell is empty). 
A prisoner who discovers that another prisoner has been released will angrily break everything in his cell, 
unless he is bribed with a gold coin. So, after releasing a prisoner in cell A, 
all prisoners housed on either side of cell A 
- until cell 1, cell P or an empty cell 
- need to be bribed.

Assume that each prison cell is initially occupied by exactly one prisoner, and that only one prisoner can be released per day. 
Given the list of Q prisoners to be released in Q days, find the minimum total number of gold coins needed as bribes 
if the prisoners may be released in any order.

Note that each bribe only has an effect for one day. If a prisoner who was bribed yesterday hears about another released prisoner today, 
then he needs to be bribed again.

Input
The first line of input gives the number of cases, N. N test cases follow. Each case consists of 2 lines. 
The first line is formatted as P Q where P is the number of prison cells and Q is the number of prisoners to be released.
This will be followed by a line with Q distinct cell numbers (of the prisoners to be released), space separated, sorted in ascending order.

Output
For each test case, output one line in the format Case #X: C where X is the case number, 
starting from 1, and C is the minimum number of gold coins needed as bribes.

Limits
1 ≤ N ≤ 100
Q ≤ P
Each cell number is between 1 and P, inclusive.

Small dataset
1 ≤ P ≤ 100
1 ≤ Q ≤ 5

Large dataset
1 ≤ P ≤ 10000
1 ≤ Q ≤ 100

Sample

Input
2
8 1
3
20 3
3 6 14
  	
Output
Case #1: 7
Case #2: 35

Note
In the second sample case, you first release the person in cell 14, then cell 6, then cell 3. 
The number of gold coins needed is 19 + 12 + 4 = 35.
If you instead release the person in cell 6 first, the cost will be 19 + 4 + 13 = 36.

8 => 2 4 6

2 4 6 = 7+5+3 = 15
6 4 2 = 7+4+2 = 13
4 6 2 = 7+3+2 = 12

23 => 6 10 18 23

18 10 6 23 = 22+16+8+4
10 6 18 23 = 22+8+12+4


30 => 13 16 18 25  (58)

*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class C_BribeThePrisoners {
	public static void main(String[] args) {
		CommonUtils.preamble();
		String inputFileName, outputFileName;

		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "C-sample1.in";
//			inputFileName = "C-sample2.in";
//			inputFileName = "C-small-practice.in";
			inputFileName = "C-large-practice.in";
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
			C_BribeThePrisoners solver = new C_BribeThePrisoners();
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

		int P, Q;
		int [] Qi;
		HashMap<Long, Integer> cache = new HashMap<Long, Integer>();
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				splitted = textLine.split("\\s+");
				P = Integer.parseInt(splitted[0]);
				Q = Integer.parseInt(splitted[1]);
				textLine = br.readLine();
				splitted = textLine.split("\\s+");
				Q = Math.min(Q, splitted.length);
				Qi = new int[Q];
				for (int i = 0; i < Q; i++) {
					Qi[i] = Integer.parseInt(splitted[i]);
				}

				System.out.printf("Case #%d: P=%d, Q=%d, Qi=[%s], ", testCaseNumber, P, Q, CommonUtils.showString(textLine, 80));
				
				if (testCaseNumber == 9) {
					System.out.printf("");
				}
				iterationCounter = 0;
//				answer = minBribeBruteForce(Qi, 0, Qi.length - 1, 1, P);
				cache.clear();
				answer = minBribe(Qi, 0, Qi.length - 1, 1, P, cache);
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
	
	private int minBribe(int [] Qi, int iStart, int iEnd, int P0, int P, HashMap<Long, Integer> cache) {
		if (P0 <= 0 || P0 >= P || iStart > iEnd) {
			return 0;
		}
		
		if (cache.containsKey((long)P<<32 | P0)) {
			return cache.get((long)P<<32 | P0);
		}
		iterationCounter++;
		int min = Integer.MAX_VALUE;
		for (int split = iStart; split <= iEnd; split++) {
			int b = minBribe(Qi, iStart, split - 1, P0, Qi[split] - 1, cache) 
					+ minBribe(Qi, split + 1, iEnd, Qi[split] + 1, P, cache);
			if (b < min) {
				min = b;
			}
		}
		
		cache.put((long)P<<32 | P0, (P - P0) + min);
		return (P - P0) + min;
	}
	
	@SuppressWarnings("unused")
	private static int minBribeBruteForce(int [] Qi, int iStart, int iEnd, int P0, int P) {
		if (P0 <= 0 || P0 >= P || iStart > iEnd) {
			return 0;
		}
		
		int min = Integer.MAX_VALUE;
		for (int split = iStart; split <= iEnd; split++) {
			int b = minBribeBruteForce(Qi, iStart, split - 1, P0, Qi[split] - 1) 
					+ minBribeBruteForce(Qi, split + 1, iEnd, Qi[split] + 1, P);
			if (b < min) {
				min = b;
			}
		}
		
		return (P - P0) + min;
	}

}