/* Problem B. Ugly Numbers
 
 Once upon a time in a strange situation, people called a number ugly if it was divisible by any of the 
 one-digit primes (2, 3, 5 or 7). Thus, 14 is ugly, but 13 is fine. 39 is ugly, but 121 is not. Note that 0 is ugly. 
 Also note that negative numbers can also be ugly; -14 and -39 are examples of such numbers.

One day on your free time, you are gazing at a string of digits, something like:

123456

You are amused by how many possibilities there are if you are allowed to insert plus or minus signs between the digits. 
For example you can make

1 + 234 - 5 + 6 = 236

which is ugly. Or

123 + 4 - 56 = 71

which is not ugly.

It is easy to count the number of different ways you can play with the digits: Between each two adjacent digits you may 
choose put a plus sign, a minus sign, or nothing. Therefore, if you start with D digits there are 3D-1 expressions you can make.

Note that it is fine to have leading zeros for a number. If the string is "01023", then "01023", "0+1-02+3" and "01-023" are legal expressions.

Your task is simple: Among the 3^(D-1) expressions, count how many of them evaluate to an ugly number.

Input

The first line of the input file contains the number of cases, N. Each test case will be a single line containing a non-empty string of decimal digits.

Output

For each test case, you should output a line

Case #X: Y

where X is the case number, starting from 1, and Y is the number of expressions that evaluate to an ugly number.

Limits

0 <= N <= 100.

The string in each test case will be non-empty and will contain only characters '0' through '9'.

Small dataset: Each string is no more than 13 characters long.

Large dataset: Each string is no more than 40 characters long.

Sample

Input
4
1
9
011
12345
  	
Output
Case #1: 0
Case #2: 1
Case #3: 6
Case #4: 64

*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.HashMap;


public class B_UglyNumbers {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
			inputFileName = "-small-practice.in";
//			inputFileName = "-large-practice.in";
			inputFileName = B_UglyNumbers.class.getSimpleName().substring(0, 1) + inputFileName;
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
			B_UglyNumbers solver = new B_UglyNumbers();
			solver.solve(inputFile, outputWriter);
			outputWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		CommonUtils.postamble();
	}

	private long iterationCounter;

	public boolean solve(final File aFile, PrintWriter w) {
		String textLine;
		boolean result = false;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		long answer;
		String digits;

		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				
				digits = textLine;
				
				System.out.printf("Case #%d: digits=%s, ", testCaseNumber, digits);

				if (testCaseNumber == 83) {
					System.out.printf("");
				}
				iterationCounter = 0;
				answer = countUglyNumbers(BigInteger.ZERO, "", digits, digits.length(), 0);
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
	
	private long countUglyNumbers(BigInteger accumValue, String output, String digits, int digitLength, int charIndex) {
		iterationCounter++;
		if (charIndex >= digitLength) {
			accumValue = accumValue.add(new BigInteger(output));
			return isAnUglyNumber(accumValue) ? 1 : 0;
		}
		
		String digit = digits.substring(charIndex, charIndex + 1);
		
		long result = countUglyNumbers(accumValue, output + digit, digits, digitLength, charIndex + 1);
		if (output.length() > 0) {
			BigInteger outputValue = new BigInteger(output);
			accumValue = accumValue.add(outputValue);
			result += countUglyNumbers(accumValue, digit, digits, digitLength, charIndex + 1);
			result += countUglyNumbers(accumValue, "-" + digit, digits, digitLength, charIndex + 1);
		}
		
		return result;
	}
	
	private static HashMap<BigInteger, Boolean> memoization = new HashMap<BigInteger, Boolean>();
	private static boolean isAnUglyNumber(BigInteger num) {
		Boolean result = !num.testBit(0);
		
		if (!result.booleanValue()) {
			result = memoization.get(num);
			if (result == null) {
				result = num.remainder(CommonUtils.BIGINT_THREE).equals(BigInteger.ZERO)
					|| num.remainder(CommonUtils.BIGINT_FIVE).equals(BigInteger.ZERO)
					|| num.remainder(CommonUtils.BIGINT_SEVEN).equals(BigInteger.ZERO);
				memoization.put(num, result);
			}
		}
		return result.booleanValue();
	}
}