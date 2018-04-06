import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class C_Dijkstra {
	public static void main(String[] args) {
		CommonUtils.preamble();
		String inputFileName, outputFileName;

		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "C-sample1.in";
//			inputFileName = "C-sample2.in";
//			inputFileName = "C-small-practice.in";
			inputFileName = "C-large-practice.in";
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
			C_Dijkstra solver = new C_Dijkstra();
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
		boolean answer;
		String [] splitted;

		boolean header = true;
		Quaternion Q;
		int inputLength = 0;
		long repeatTimes = 0;
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			
			while ((testCaseNumber += header ? 1 : 0) <= testCaseCount && (textLine = br.readLine()) != null) {
				if (header) {
					splitted = textLine.split("\\s+");
					inputLength = Integer.parseInt(splitted[0]);
					repeatTimes = Long.parseLong(splitted[1]);
				} else {
/////////////////////////////////////////////////////////////////////////////////////
					String textLineToShow = (textLine.length() > 50 ? textLine.substring(0, 50) + "..." : textLine);
					System.out.printf("Case #%d: %d*{%s (%d chars)}, ", testCaseNumber, repeatTimes, textLineToShow, inputLength);
					System.out.flush();
					Q = new Quaternion(inputLength, repeatTimes, textLine);
					answer = Q.canBeReducedToIJK();

					w.printf("Case #%d: %s\n", testCaseNumber, answer ? "YES":"NO");
					w.flush();
					
					System.out.printf("answer=%s\n", answer ? "YES":"NO");
////////////////////////////////////////////////////////////////////////////////////
				}
				
				header = !header;
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