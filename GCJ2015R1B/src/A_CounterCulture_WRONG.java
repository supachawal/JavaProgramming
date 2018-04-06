/* Problem A. Counter Culture
In the Counting Poetry Slam, a performer takes the microphone, chooses a number N, and counts aloud from 1 to N. 
That is, she starts by saying 1, and then repeatedly says the number that is 1 greater than the previous number 
she said, stopping after she has said N.

It's your turn to perform, but you find this process tedious, and you want to add a twist to speed it up: 
sometimes, instead of adding 1 to the previous number, you might reverse the digits of the number 
(removing any leading zeroes that this creates). For example, after saying "16", you could next say either "17" 
or "61"; after saying "2300", you could next say either "2301" or "32". 
You may reverse as many times as you want (or not at all) within a performance.

The first number you say must be 1; what is the fewest number of numbers you will need to say in order to reach the number N? 1 and N count toward this total. If you say the same number multiple times, each of those times counts separately.

Input
The first line of the input gives the number of test cases, T. T lines follow. Each has one integer N, the number you must reach.

Output
For each test case, output one line containing "Case #x: y", where x is the test case number (starting from 1) 
and y is the minimum number of numbers you need to say.

Limits: 1 ≤ T ≤ 100.
Small dataset: 1 ≤ N ≤ 10^6.
Large dataset: 1 ≤ N ≤ 10^14.

Sample
Input
3
1
19
23

Output
Case #1: 1
Case #2: 19
Case #3: 15

In Case #2, flipping does not help and the optimal strategy is to just count up to 19.

In Case #3, the optimal strategy is to count up to 12, flip to 21, and then continue counting up to 23. 
That is, the numbers you will say are 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 21, 22, 23. 
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class A_CounterCulture_WRONG {
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
			inputFileName = A_CounterCulture_WRONG.class.getSimpleName().substring(0, 1) + inputFileName;
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
			A_CounterCulture_WRONG solver = new A_CounterCulture_WRONG();
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

	HashMap<Long, Long> memoization = new HashMap<Long, Long>(8192);
	long N;
	
	public boolean solve(final File aFile, PrintWriter w) {
		String textLine;
		boolean result = false;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		@SuppressWarnings("unused")
		String [] splitted;
		long answer;

		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				N = Long.valueOf(textLine);
				
				System.out.printf("Case #%d: N=%d, ", testCaseNumber, N);
				
				if (testCaseNumber == 9) {
					System.out.printf("");
				}
				iterationCounter = 0;
				//memoization.clear();
				answer = minNumCount(N);
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

	private static long decrementNum(long num) {
		long ret = num - 1;
		
		if (num % 10 == 1) {
			String strNum = String.valueOf(num);
			long r = Long.valueOf((new StringBuilder(strNum)).reverse().toString());
			if (r < num) {
				ret = r;
			}
		}
		
		return ret;
	}

	private static long minNumCount(long to) {
		long i;
		long count = 0;
		for (i = to; i >= 1; i = decrementNum(i)) {
			count++;
		}
		
		return count;
	}
}