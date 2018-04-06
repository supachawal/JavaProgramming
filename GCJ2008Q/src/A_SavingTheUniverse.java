/* Problem A. Saving the Universe (collision avoidance)
The urban legend goes that if you go to the Google homepage and search for "Google", the universe will implode. We have a secret to share... 
It is true! Please don't try it, or tell anyone. All right, maybe not. We are just kidding.

The same is not true for a universe far far away. In that universe, if you search on any search engine for that search engine's name, 
the universe does implode!
  
To combat this, people came up with an interesting solution. All queries are pooled together. 
They are passed to a central system that decides which query goes to which search engine. 
The central system sends a series of queries to one search engine, and can switch to another at any time. 
Queries must be processed in the order they're received. 
The central system must never send a query to a search engine whose name matches the query. 
In order to reduce costs, the number of switches should be minimized.

Your task is to tell us how many times the central system will have to switch between search engines, assuming that we program it optimally.

Input
The first line of the input file contains the number of cases, N. N test cases follow.

Each case starts with the number S -- the number of search engines. The next S lines each contain the name of a search engine.
Each search engine name is no more than one hundred characters long and contains only uppercase letters, lowercase letters, spaces, and numbers. 
There will not be two search engines with the same name.

The following line contains a number Q -- the number of incoming queries. 
The next Q lines will each contain a query. Each query will be the name of a search engine in the case.

Output
For each input case, you should output:

Case #X: Y

where X is the number of the test case and Y is the number of search engine switches. Do not count the initial choice of a search engine as a switch.

Limits: 0 < N ≤ 20
Small dataset: 2 ≤ S ≤ 10, 0 ≤ Q ≤ 100
Large dataset: 2 ≤ S ≤ 100, 0 ≤ Q ≤ 1000

Sample
Input
2
5
Yeehaw
NSM
Dont Ask
B9
Googol
10
so
Googol
5
Yeehaw
NSM
Dont Ask
B9
Googol
7
Googol
Dont Ask
NSM
NSM
Yeehaw
Yeehaw
Googol
	
Output
Case #1: 1
Case #2: 0

In the first case, one possible solution is to start by using Dont Ask, and switch to NSM after query number 8.
For the second case, you can use B9, and not need to make any switches.
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;

public class A_SavingTheUniverse {
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
			inputFileName = A_SavingTheUniverse.class.getSimpleName().substring(0, 1) + inputFileName;
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
			A_SavingTheUniverse solver = new A_SavingTheUniverse();
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
		@SuppressWarnings("unused")
		String [] splitted;
		int answer;

		int nS, nQ;
		int i;
		String [] S, Q;
		StringBuilder sbS, sbQ;

		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				sbS = new StringBuilder();
				sbQ = new StringBuilder();

				nS = Integer.parseInt(textLine);
				S = new String[nS];
				for (i = 0; i < nS; i++) {
					textLine = br.readLine().trim();
					S[i] = textLine;
					sbS.append("\"").append(textLine).append("\"");
					if (i < nS - 1) {
						sbS.append(",");
					}
				}

				nQ = Integer.parseInt(br.readLine());
				Q = new String[nQ];
				for (i = 0; i < nQ; i++) {
					textLine = br.readLine().trim();
					Q[i] = textLine;
					sbQ.append("\"").append(textLine).append("\"");
					if (i < nQ - 1) {
						sbQ.append(",");
					}
				}
				
				System.out.printf("Case #%d: nS=%d, nQ=%d, S=[%s], Q=[%s], "
								, testCaseNumber, nS, nQ
								, CommonUtils.showString(sbS.toString(), 100)
								, CommonUtils.showString(sbQ.toString(), 300));
				
				if (testCaseNumber == 9) {
					System.out.printf("");
				}
				iterationCounter = 0;
				answer = minSwitching_GREEDY(S, Q);
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
	 * minSwitching_GREEDY  Yes. Its algorithm is greedy. 
	 * 
	 * @param S, server names
	 * @param Q, query terms
	 * @return
	 */
	private static int minSwitching_GREEDY(String [] S, String [] Q) {
		HashSet<String> hS = new HashSet<String>();
		for (String s : S) {
			hS.add(s);
		}
		
		HashSet<String> hQ = new HashSet<String>();
		int count = 0;
		int nS = hS.size();
		System.out.printf("SolutionSteps=<");

		for (String q : Q) {
			if (hS.contains(q) && !hQ.contains(q)) {
				if (hQ.size() == nS - 1) {
					hQ.clear();

					// log the intermediate steps
					if (count > 0) {
						System.out.printf(",");
					}
					System.out.printf("\"%s\"", q);

					count++;
				}
				hQ.add(q);
			}
		}

		// log the remaining steps
		if (hQ.size() > 0) {
			hS.removeAll(hQ);
			if (count > 0) {
				System.out.printf(",");
			}
			System.out.printf("\"%s\"", hS.iterator().next());
		}
		
		System.out.printf(">, ");
		
		return count;
	}

	/**
	 * minSwitching try all combinations by recursion with memoization cache technique
	 * @param S, server names
	 * @param Q, query terms
	 * @return
	 */
	@SuppressWarnings("unused")
	private int minSwitching(String [] S, String [] Q) {
		int ret = Integer.MAX_VALUE;
		HashMap<Long, Integer> cache = new HashMap<Long, Integer>();
		
		for (int i = 0, n = S.length; i < n; i++) {
			ret = Math.min(ret, minSwitching(S, i, Q, 0, cache));
		}
		
		return ret;
	}

	private int minSwitching(String [] S, int iS, String [] Q, int iQ, HashMap<Long, Integer> cache) {
		while (iQ < Q.length && !Q[iQ].equals(S[iS])) {
			iQ++;
		}
		
		if (iQ == Q.length) {
			return 0;
		} 

		iterationCounter++;
		
		if (cache.containsKey((long)iS<<32 | iQ)) {
			return cache.get((long)iS<<32 | iQ);
		}

		int ret = Integer.MAX_VALUE;
		
		for (int i = S.length - 1; i > 0; i--) {
			ret = Math.min(ret, minSwitching(S, (iS + i) % S.length, Q, iQ, cache));
		}

		cache.put((long)iS<<32 | iQ, ret + 1);
		
		return ret + 1;
	}
}