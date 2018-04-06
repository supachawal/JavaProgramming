import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class A_AlienLanguage {
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
			A_AlienLanguage solver = new A_AlienLanguage();
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
		int answer;
		String [] splitted;
		ArrayList<String> T;

		int i;
		int L, D;
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			L = Integer.parseInt(splitted[0]);
			D = Integer.parseInt(splitted[1]);
			testCaseCount = Integer.parseInt(splitted[2]);
			T = new ArrayList<String>();
			
			for (i = 0; i < D && (textLine = br.readLine()) != null; i++) {
				T.add(textLine);
			}
				
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
/////////////////////////////////////////////////////////////////////////////////////
				iterationCounter = 0;
				answer = patternMatchCount(textLine, T);

				w.printf("Case #%d: %d\n", testCaseNumber, answer);
				System.out.printf("Case #%d: L=%d, D=%d, pattern={%s}, answer=%d, iterations=%d\n", testCaseNumber, L, D, CommonUtils.showString(textLine, 40), answer, iterationCounter);
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

	private static int patternMatchCount(String patternString, ArrayList<String> T) {
		int ret = 0;
		String regexPatternString = patternString.replace(')', ']').replace('(', '[');
		
		for (String s : T) {
			if (s.matches(regexPatternString)) {
				ret++;
			}
		}
		
		return ret;
	}

}