import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.HashMap;

public class A_AllYourBase {
	public static void main(String[] args) {
		CommonUtils.preamble();
		String inputFileName, outputFileName;
		
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "A-sample1.in";
//			inputFileName = "A-sample2.in";
//			inputFileName = "A-small-practice.in";
			inputFileName = "A-large-practice.in";
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
			A_AllYourBase solver = new A_AllYourBase();
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
		BigInteger answer;

		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
				
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				
				iterationCounter = 0;
				answer = alienMinBase(textLine);

				w.printf("Case #%d: %s\n", testCaseNumber, answer);
				System.out.printf("Case #%d: {%s}, answer=%s (%d bits), iterations=%d\n", testCaseNumber, CommonUtils.showString(textLine, 80), answer.toString(), answer.bitLength(), iterationCounter);
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

	private static BigInteger alienMinBase(String s) {
		int i;
		char [] a = s.toCharArray();
		int n = a.length;
		BigInteger result = BigInteger.ZERO;
		int digit = -1;
		HashMap<Character, Integer> digitMap = new HashMap<Character, Integer>(1024);
		
		for (i = 0; i < n; i++) {
			if (!digitMap.containsKey(a[i])) {
				++digit;
				if (digit == 0)
					digitMap.put(a[i], 1);
				else if (digit == 1)
					digitMap.put(a[i], 0);
				else
					digitMap.put(a[i], digit);
			}
		}
		
		BigInteger nBase = new BigInteger(String.valueOf(Math.max(digit + 1, 2)));  
		BigInteger posVal = BigInteger.ONE;

		for (i = n - 1; i >= 0; i--) {
			digit = digitMap.get(a[i]);
			result = result.add(posVal.multiply(new BigInteger(String.valueOf(digit))));
			posVal = posVal.multiply(nBase); 
		}

		return result;
	}
}