import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class B_ReverseWords {
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
			inputFileName = B_ReverseWords.class.getSimpleName().substring(0, 1) + inputFileName;
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
			B_ReverseWords solver = new B_ReverseWords();
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
		String [] splitted;

		int i;
		int n;
		boolean suppressed;
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			
			while ((++testCaseNumber) <= testCaseCount && (textLine = br.readLine()) != null) {
/////////////////////////////////////////////////////////////////////////////////////
				String textLineToShow = textLine;
				if (textLineToShow.length() > 100) {
					textLineToShow = textLineToShow.substring(0, 100) + " ...";
				}

				splitted = textLine.split("\\s+");
				w.printf("Case #%d: ", testCaseNumber);
				System.out.printf("Case #%d: {%s}, answer={", testCaseNumber, textLineToShow);
				n = 0;
				suppressed = false;
				for (i = splitted.length - 1; i >= 0; i--) {
					w.printf("%s%s", splitted[i], i > 0 ? " " : "");
					
					if (i > 0) {
						if (!suppressed) {
							System.out.printf("%s ", splitted[i]);
							if (++n == 11 && i > 1) {
								System.out.printf("... ");
								suppressed = true;
							}
						}
					} else {
						System.out.printf("%s", splitted[i]);
					}
				
				}
				w.printf("\n");
				System.out.printf("}\n");
				
////////////////////////////////////////////////////////////////////////////////////
				
//				header = !header;
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