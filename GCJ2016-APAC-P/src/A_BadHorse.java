/* Problem A. Bad Horse

As the leader of the Evil League of Evil, Bad Horse has a lot of problems to deal with. Most recently, there 
have been far too many arguments and far too much backstabbing in the League, so much so that Bad Horse has decided to 
split the league into two departments in order to separate troublesome members. Being the Thoroughbred of Sin, Bad Horse 
isn't about to spend his valuable time figuring out how to split the League members by himself. That what he's got you 
-- his loyal henchman -- for. 

Input
The first line of the input gives the number of test cases, T. T test cases follow. Each test case starts with a positive integer M on a line by itself -- the number of troublesome pairs of League members. The next M lines each contain a pair of names, separated by a single space. 

Output
For each test case, output one line containing "Case #x: y", where x is the case number (starting from 1) and y is 
either "Yes" or "No", depending on whether the League members mentioned in the input can be split into two groups 
with neither of the groups containing a troublesome pair. 

Limits
1 ≤ T ≤ 100.
Each member name will consist of only letters and the underscore character.
Names are case-sensitive.
No pair will appear more than once in the same test case.
Each pair will contain two distinct League members. 

Small dataset
1 ≤ M ≤ 10. 

Large dataset
1 ≤ M ≤ 100. 

Sample
Input 
2
1
Dead_Bowie Fake_Thomas_Jefferson
3
Dead_Bowie Fake_Thomas_Jefferson
Fake_Thomas_Jefferson Fury_Leika
Fury_Leika Dead_Bowie 
   
Output 
Case #1: Yes
Case #2: No

*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;

public class A_BadHorse {
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
			inputFileName = "-small-practice-2.in";
//			inputFileName = "-large-practice.in";
			inputFileName = A_BadHorse.class.getSimpleName().substring(0, 1) + inputFileName;
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
			A_BadHorse solver = new A_BadHorse();
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
		boolean answer;
		int i, m;
		HashMap<String, HashSet<String>> namePairs;
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				if (testCaseNumber == 2) {
					System.out.printf("");
				}
				m = Integer.parseInt(textLine);
				// this assumes that the graph is connected and non-empty.
				namePairs = new HashMap<String, HashSet<String>>();
				for (i = 0; i < m; i++) {
					textLine = br.readLine();
					splitted = textLine.split("\\s+");
					HashSet<String> s;
					s = namePairs.get(splitted[0]);
					if (s == null) {
						s = new HashSet<String>();
						namePairs.put(splitted[0], s);
					}
					s.add(splitted[1]);
					s = namePairs.get(splitted[1]);
					if (s == null) {
						s = new HashSet<String>();
						namePairs.put(splitted[1], s);
					}
					s.add(splitted[0]);
				}

				System.out.printf("Case #%d: M=%d, ", testCaseNumber, m);
				iterationCounter = 0;
				answer = isBipartiteGraph(namePairs);
				
				System.out.printf("answer=%s (iterations=%d)\n", answer ? "Yes":"No", iterationCounter);

				w.printf("Case #%d: %s\n", testCaseNumber, answer ? "Yes":"No");
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
	
	private boolean isBipartiteGraph(HashMap<String, HashSet<String>> namePairs) {
		boolean result = true;
		HashMap<String, Integer> nameSetMap = new HashMap<String, Integer>();
		ArrayDeque<String> Q = new ArrayDeque<String>();
		Q.addAll(namePairs.keySet());

		while (result && !Q.isEmpty()) {
			iterationCounter++;
			String name = Q.pollFirst();
			Integer set1 = nameSetMap.get(name);
			if (set1 == null) {
				set1 = 0;
				nameSetMap.put(name, set1);
			}
			Integer set2e = set1.intValue() ^ 1;
			for (String p : namePairs.get(name)) {
				iterationCounter++;
				Integer set2 = nameSetMap.get(p); 
				if (set2 == null) {
					set2 = set2e;
					nameSetMap.put(p, set2);
					Q.removeFirstOccurrence(p);
					Q.addFirst(p);
				}
					
				if (!set2.equals(set2e)) {
					result = false;
					break;
				}
			}
		}
		
		return result;
	}
}