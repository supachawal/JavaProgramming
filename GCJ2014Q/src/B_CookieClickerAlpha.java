/* Problem B. Cookie Clicker Alpha

Introduction

Cookie Clicker is a Javascript game by Orteil, where players click on a picture of a giant cookie. Clicking on the giant cookie gives them cookies. They can spend those cookies to buy buildings. Those buildings help them get even more cookies. Like this problem, the game is very cookie-focused. This problem has a similar idea, but it does not assume you have played Cookie Clicker. Please don't go play it now: it might be a long time before you come back. 

Problem

In this problem, you start with 0 cookies. You gain cookies at a rate of 2 cookies per second, by clicking on a giant cookie. Any time you have at least C cookies, you can buy a cookie farm. Every time you buy a cookie farm, it costs you C cookies and gives you an extra F cookies per second. 

Once you have X cookies that you haven't spent on farms, you win! Figure out how long it will take you to win if you use the best possible strategy. 

Example

Suppose C=500.0, F=4.0 and X=2000.0. Here's how the best possible strategy plays out: 
1.You start with 0 cookies, but producing 2 cookies per second.
2.After 250 seconds, you will have C=500 cookies and can buy a farm that produces F=4 cookies per second.
3.After buying the farm, you have 0 cookies, and your total cookie production is 6 cookies per second.
4.The next farm will cost 500 cookies, which you can buy after about 83.3333333 seconds.
5.After buying your second farm, you have 0 cookies, and your total cookie production is 10 cookies per second.
6.Another farm will cost 500 cookies, which you can buy after 50 seconds.
7.After buying your third farm, you have 0 cookies, and your total cookie production is 14 cookies per second.
8.Another farm would cost 500 cookies, but it actually makes sense not to buy it: instead you can just wait until you have X=2000 cookies, which takes about 142.8571429 seconds.
Total time: 250 + 83.3333333 + 50 + 142.8571429 = 526.1904762 seconds. 

Notice that you get cookies continuously: so 0.1 seconds after the game starts you'll have 0.2 cookies, and π seconds after the game starts you'll have 2π cookies. 

Input

The first line of the input gives the number of test cases, T. T lines follow. Each line contains three space-separated real-valued numbers: C, F and X, whose meanings are described earlier in the problem statement. 

C, F and X will each consist of at least 1 digit followed by 1 decimal point followed by from 1 to 5 digits. There will be no leading zeroes. 

Output

For each test case, output one line containing "Case #x: y", where x is the test case number (starting from 1) and y is the minimum number of seconds it takes before you can have X delicious cookies. 

We recommend outputting y to 7 decimal places, but it is not required. y will be considered correct if it is close enough to the correct number: within an absolute or relative error of 10-6. See the FAQ for an explanation of what that means, and what formats of real numbers we accept. 

Limits
1 ≤ T ≤ 100. 

Small dataset
1 ≤ C ≤ 500.
1 ≤ F ≤ 4.
1 ≤ X ≤ 2000.

Large dataset
1 ≤ C ≤ 10000.
1 ≤ F ≤ 100.
1 ≤ X ≤ 100000.


Sample
Input 
4
30.0 1.0 2.0
30.0 2.0 100.0
30.50000 3.14159 1999.19990
500.0 4.0 2000.0
   
Output 
Case #1: 1.0000000
Case #2: 39.1666667
Case #3: 63.9680013
Case #4: 526.1904762

Note

Cookie Clicker was created by Orteil. Orteil does not endorse and has no involvement with Google Code Jam. 
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class B_CookieClickerAlpha {
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
			inputFileName = B_CookieClickerAlpha.class.getSimpleName().substring(0, 1) + inputFileName;
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
			B_CookieClickerAlpha solver = new B_CookieClickerAlpha();
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
		double answer;
		double C, F, X;

		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				splitted = textLine.split("\\s+");
				C = Double.parseDouble(splitted[0]);
				F = Double.parseDouble(splitted[1]);
				X = Double.parseDouble(splitted[2]);
				
//				if (testCaseNumber == 101) {
//				System.out.printf("");
//				}
				iterationCounter = 0;
				answer = minTotalSeconds(C, F, X);
				
				System.out.printf("Case #%d: answer=%.7f (iterations=%d)\n", testCaseNumber, answer, iterationCounter);

				w.printf("Case #%d: %.7f\n", testCaseNumber, answer);
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
	
	private double minTotalSeconds(double farmCost, double farmRateGain, double targetDeal) {
		double bestSoFar = Double.POSITIVE_INFINITY;
		double farmRate = 2.0;
		double accumOverheadTime = 0.0;
		double totalTime = targetDeal / farmRate;
		
		while (totalTime < bestSoFar) {
			iterationCounter++;
			bestSoFar = totalTime;
			accumOverheadTime += farmCost / farmRate;
			farmRate += farmRateGain;
			totalTime = targetDeal / farmRate + accumOverheadTime;
		}
		
		return bestSoFar;
	}
}