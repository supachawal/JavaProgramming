import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class B_InfiniteHouseOfPancakes_CORRECTED {
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
			inputFileName = B_InfiniteHouseOfPancakes_CORRECTED.class.getSimpleName().substring(0, 1) + inputFileName;
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
			B_InfiniteHouseOfPancakes_CORRECTED solver = new B_InfiniteHouseOfPancakes_CORRECTED();
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
		long answer;
		String [] splitted;

		boolean header = true;
		int i;
		int maxPi;
		int D = 0;
		int [] P;
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			
			while ((testCaseNumber += header ? 1 : 0) <= testCaseCount && (textLine = br.readLine()) != null) {
				if (header) {
					D = Integer.parseInt(textLine);
				} else {
/////////////////////////////////////////////////////////////////////////////////////
					splitted = textLine.split("\\s+");
					D = Math.min(D, splitted.length);
					maxPi = -1;
					P = new int[D];
					for (i = 0; i < D; i++ ) {
						P[i] = Integer.valueOf(splitted[i]);
						if (P[i] > maxPi) {
							maxPi = P[i];
						}
					}

					iterationCounter = 0;
					answer = diningDuration(P, D, maxPi);

					w.printf("Case #%d: %d\n", testCaseNumber, answer);
					String textLineToShow = textLine;
					if (textLineToShow.length() > 100) {
						textLineToShow = textLineToShow.substring(0, 100) + " ...";
					}
					System.out.printf("Case #%d: {%s}, maxPi=%d, answer=%d, iterations=%d\n", testCaseNumber, textLineToShow, maxPi, answer, iterationCounter);
////////////////////////////////////////////////////////////////////////////////////
				}
				
				header = !header;
//				3
//				1		Case #1: 3
//				3		
//				4		Case #2: 2
//				1 2 1 2
//				1		Case #3: 3
//				4					
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

	private int diningDuration(int [] P, int D, int maxPi) {
		/*"If you expect the customer to finish their pancakes x minutes after you stop moving pancakes, 
		 * any strategy that satisfies this will be equivalent to repeatedly moving at most x pancakes from every initially 
		 * non-empty plate to an empty plate until the number of pancakes on the initially non-empty plate becomes no more than x."
		 */
		
		int ret = maxPi;								// worst case
		
		for (int x = 1; x < maxPi; x++) {
			int totalMoves = 0;
			for (int i = 0; i < D; i++) {
				totalMoves += (P[i] - 1) / x;			// (P[i] - 1) / x   is the Ceiling(P[i] / x) - 1
				iterationCounter++;
			}
			
			ret = Math.min(ret, x + totalMoves);
		}
		
		return ret;
	}
}