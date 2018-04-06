import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class A_MininumScalarProduct {
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
			inputFileName = A_MininumScalarProduct.class.getSimpleName().substring(0, 1) + inputFileName;
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
			A_MininumScalarProduct solver = new A_MininumScalarProduct();
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
		String textLine, textLine2;
		boolean result = false;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		long answer;
		String [] splitted;

		boolean header = true;
		int i;
		int n = 0;
		int [] X, Y;
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			
			while ((testCaseNumber += header ? 1 : 0) <= testCaseCount && (textLine = br.readLine()) != null) {
				if (header) {
					n = Integer.parseInt(textLine);
				} else {
/////////////////////////////////////////////////////////////////////////////////////
					splitted = textLine.split("\\s+");
					n = Math.min(n, splitted.length);
					X = new int[n];
					Y = new int[n];
					for (i = 0; i < n; i++ ) {
						X[i] = Integer.valueOf(splitted[i]);
					}
					textLine2 = br.readLine();
					splitted = textLine2.split("\\s+");
					for (i = 0; i < n; i++ ) {
						Y[i] = Integer.valueOf(splitted[i]);
					}

					iterationCounter = 0;
					answer = minDotProduct(X, Y, n);

					w.printf("Case #%d: %d\n", testCaseNumber, answer);
					System.out.printf("Case #%d: (%s).(%s), n=%d, answer=%d, iterations=%d\n", testCaseNumber, CommonUtils.showString(textLine, 30), CommonUtils.showString(textLine2, 30), n, answer, iterationCounter);
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

	private static int [] sortedIntArray(int [] A, int n, boolean descending) {
		AbstractBinaryHeap bh;
		int i;
		
		if (descending) {
			bh = new MaxHeap(n);
		} else {
			bh = new MinHeap(n);
		}
		
		for (i = 0; i < n; i++) {
			bh.insertItem(new KeyValuePair(i, A[i]));
		}
		
		i = 0;
		while (bh.getItemCount() > 0) {
			A[i++] = (int)(bh.popRoot().value);
		}
		
		return A;
	}

	private static long minDotProduct(int [] X, int [] Y, int n) {
		int [] sortedX = sortedIntArray(X, n, false);
		int [] sortedY = sortedIntArray(Y, n, true);
		long ret = 0;
		for (int i = 0; i < n; i++) {
//BUG:		ret += sortedX[i] * sortedY[i];
			ret += (long)sortedX[i] * sortedY[i];
		}
		
		return ret;
	}
}