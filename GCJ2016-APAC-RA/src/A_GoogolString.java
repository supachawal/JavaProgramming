/* Problem A. Googol String

A "0/1 string" is a string in which every character is either 0 or 1. There are two operations that can be performed on a 0/1 string: 
•switch: Every 0 becomes 1 and every 1 becomes 0. For example, "100" becomes "011".
•reverse: The string is reversed. For example, "100" becomes "001".


Consider this infinite sequence of 0/1 strings:

 S0 = ""

 S1 = "0"

 S2 = "001"

 S3 = "0010011"

 S4 = "001001100011011"

 ...

 SN = SN-1 + "0" + switch(reverse(SN-1)). 

You need to figure out the Kth character of Sgoogol, where googol = 10100. 

Input
The first line of the input gives the number of test cases, T. Each of the next T lines contains a number K. 

Output
For each test case, output one line containing "Case #x: y", where x is the test case number (starting from 1) and y is the Kth character of Sgoogol. 

Limits
1 ≤ T ≤ 100.

Small dataset
1 ≤ K ≤ 105.

Large dataset
1 ≤ K ≤ 1018.

Sample
Input 
4
1
2
3
10

Output 
Case #1: 0
Case #2: 0
Case #3: 1
Case #4: 0

*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class A_GoogolString {
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
			inputFileName = A_GoogolString.class.getSimpleName().substring(0, 1) + inputFileName;
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
			A_GoogolString solver = new A_GoogolString();
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
		int k;
		StringBuilder gS = googolString(100000L);
		
//		test();
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				if (testCaseNumber == 168) {
					System.out.printf("");
				}
				k = Integer.parseInt(textLine);

				System.out.printf("Case #%d: k=%d, ", testCaseNumber, k);
				iterationCounter = 0;
				
				answer = gS.charAt(k - 1);
				
				System.out.printf("answer=%c (iterations=%d)\n", answer, iterationCounter);

				w.printf("Case #%d: %c\n", testCaseNumber, answer);
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

	private static StringBuilder googolString(long length) {
		StringBuilder s = new StringBuilder();
		
		do {
			StringBuilder newS = switchedReversedString(s);
			s.append("0").append(newS);
		} while (s.length() < length);
		
		return s;
	}
	
	public static void test() {
		StringBuilder s = new StringBuilder();
		long i = 0;
		
		do {
			StringBuilder newS = switchedReversedString(s);
			s.append("0").append(newS);
			System.out.printf("S(%d) = \"%s\"\n", ++i, s);
		} while (s.length() < 100000L);
	}

	public static StringBuilder switchedReversedString(CharSequence s) {
		StringBuilder p = (new StringBuilder(s)).reverse();
		for (int i = p.length() - 1; i >= 0; i--) {
			char c = p.charAt(i);
			p.setCharAt(i, c == '0' ? '1' : '0');
		}
		
		return p;
	}
}