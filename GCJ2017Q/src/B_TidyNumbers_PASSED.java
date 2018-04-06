import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class B_TidyNumbers_PASSED {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
//			inputFileName = "-small-attempt0.in";
//			inputFileName = "-small-1-attempt0.in";
//			inputFileName = "-small-2-attempt0.in";
			inputFileName = "-large.in";
			inputFileName = B_TidyNumbers_PASSED.class.getSimpleName().substring(0, 1) + inputFileName;
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
			B_TidyNumbers_PASSED solver = new B_TidyNumbers_PASSED();
			solver.solve(inputFile, outputWriter);
			outputWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		CommonUtils.postamble();
	}

	public void solve(final File aFile, PrintWriter w) throws FileNotFoundException, IOException {
		String textLine;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		
		br = new BufferedReader(new FileReader(aFile));
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
//			if (testCaseNumber == 168) {
//				System.out.printf("");
//			}
			//String[] splitted = textLine.split("\\s+");
			char[] s = textLine.toCharArray();
			
			System.out.printf("Case #%d: N=%s, answer=", testCaseNumber, textLine);
			w.printf("Case #%d:", testCaseNumber);

			int i, n = s.length;
			for (i = 1; i < n; i++) {
				if (s[i] < s[i - 1]) {
					s[i - 1]--;
					for (; i < n; i++) {
						s[i] = '9';
					}
					break;
				}
			}
			for (i = n - 1; i > 0; i--) {
				if (s[i] < s[i - 1]) {
					s[i - 1]--;
					s[i] = '9';
				}
			}
			
			StringBuilder answer = new StringBuilder();
			for (i = 0; i < n && s[i] == '0'; i++) {
			}

			for (; i < n; i++) {
				answer.append(s[i]);
			}

			System.out.printf(" %s\n", answer);
			w.printf(" %s\n", answer);
////////////////////////////////////////////////////////////////////////////////////
		}

		br.close();
	}
}