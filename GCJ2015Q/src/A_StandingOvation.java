import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class A_StandingOvation {
	public static void main(String[] args) {
		CommonUtils.preamble();
		String inputFileName, outputFileName;

		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "A-sample1.in";
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
			A_StandingOvation solver = new A_StandingOvation();
			solver.solve(inputFile, outputWriter);
			outputWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		CommonUtils.postamble();
	}

	public boolean solve(final File aFile, PrintWriter w) {
		String textLine;
		boolean result = false;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		long neededS0;
		String [] splitted;
		int sMax = 0;
		byte [] s;
		char [] sList;
		int i;
		int n;
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			
			while (testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				neededS0 = 0;
				splitted = textLine.split("\\s+", -1);
				sMax = Integer.parseInt(splitted[0]);
				if (splitted.length > 1) {
					sList = splitted[1].toCharArray();
					n = Math.min(sMax + 1, sList.length);
					s = new byte[n];
					for (i = 0; i < n; i++) {
						s[i] = (byte)(sList[i] - '0');
					}
					
					i = n - 1;
					neededS0 = i;
					for (i = n - 2; i >= 0; i--) {
						neededS0 -= s[i];
						if (neededS0 < i)
							neededS0 = i;
					}
				}
				w.printf("Case #%d: %d", ++testCaseNumber, neededS0);
				if (testCaseNumber < testCaseCount)
					w.printf("\n");
//				4
//				4 11111
//				1 09
//				5 110011
//				0 1				
					

//				Case #1: 0
//				Case #2: 1
//				Case #3: 2
//				Case #4: 0				
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
}