/* Problem B. Number Sets ----- Always incorrect. Probably, I don't understand the problem

You start with a sequence of consecutive integers. You want to group them into sets. 
You are given the interval, and an integer P. Initially, each number in the interval is in its own set. 
Then you consider each pair of integers in the interval. If the two integers share a prime factor which is at least P, 
then you merge the two sets to which the two integers belong. 

How many different sets there will be at the end of this process? 

Input

One line containing an integer C, the number of test cases in the input file. 
For each test case, there will be one line containing three single-space-separated integers A, B, and P. 
A and B are the first and last integers in the interval, and P is the number as described above. 

Output

For each test case, output one line containing the string "Case #X: Y" where X is the number of the test case, starting from 1, and Y is the number of sets. 

Limits

Small dataset

1 <= C <= 10 
1 <= A <= B <= 1000 
2 <= P <= B 

Large dataset

1 <= C <= 100 
1 <= A <= B <= 1012 
B <= A + 1000000 
2 <= P <= B 

Sample

Input 
   
 2
 10 20 5
 10 20 3

Output 
   
 Case #1: 9
 Case #2: 7
 
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

public class B_NumberSets {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
			inputFileName = "-sample1.in";
//			inputFileName = "-small-practice.in";
//			inputFileName = "-large-practice.in";
			inputFileName = B_NumberSets.class.getSimpleName().substring(0, 1) + inputFileName;
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
			B_NumberSets solver = new B_NumberSets();
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
		long answer;
		long A, B, P;

		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				splitted = textLine.split("\\s+");
				
				A = Long.valueOf(splitted[0]);
				B = Long.valueOf(splitted[1]);
				P = Long.valueOf(splitted[2]);
				
				System.out.printf("Case #%d: A=%d, B=%d, P=%d, ", testCaseNumber, A, B, P);
				
				if (testCaseNumber == 83) {
					System.out.printf("");
				}
				iterationCounter = 0;
				answer = numberOfSets(A, B, P);
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
	
	private static List<Long> genPrimeNumbers(long maxNum) {
		List<Long> result = new ArrayList<Long>();
		if (maxNum >= 2) {
			result.add(2L);
		}
		
		long p = 3;
		
		while (p <= maxNum) {
			boolean timesP = false;
			for (Long i : result) {
				if (p % i == 0) {
					timesP = true;
					break;
				}
			}
			if (!timesP) {
				result.add(p);
			}
			p += 2;
		}
		return result;
	}
	
	private static long numberOfSets(long A, long B, long P) {
		long maxPrimeNum = B;
		List<Long> primeNums = genPrimeNumbers(maxPrimeNum);
		HashMap<Long, HashSet<Long>> processed = new HashMap<Long, HashSet<Long>>();
		long countStandAlone = 0;
		boolean dirty;
		
		for (long i = A; i <= B; i++) {
			dirty = false;
			for (Long p : primeNums) {
				if (p >= P && i % p == 0) {
					HashSet<Long> s = processed.get(p);
					if (s == null) {
						s = new HashSet<Long>();
						processed.put(p, s);
					}
					s.add(i);
					dirty = true;
				}
			}
			
			if (!dirty) {
				countStandAlone++;
			}
		}

		do {
			dirty = false;
			Long keyToRemove = null;
			
			for (Entry<Long, HashSet<Long>> e : processed.entrySet()) {
				for (Entry<Long, HashSet<Long>> o : processed.entrySet()) {
					if (e != o) {
						@SuppressWarnings("unchecked")
						HashSet<Long> testSet = (HashSet<Long>) e.getValue().clone();
						testSet.retainAll(o.getValue());
						if (!testSet.isEmpty()) {
							keyToRemove = e.getKey();
							dirty = true;
							break;
						}
					}
				}
				if (dirty) {
					break;
				}
			}
				
			if (dirty) {
				processed.remove(keyToRemove);
			}
			
		} while (dirty);

		return processed.size() + countStandAlone;
	}
}