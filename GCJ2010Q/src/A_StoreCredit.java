import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class A_StoreCredit {
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
			inputFileName = A_StoreCredit.class.getSimpleName().substring(0, 1) + inputFileName;
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
			A_StoreCredit solver = new A_StoreCredit();
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
		int [] answer;
		String [] splitted;

		boolean header = true;
		int i;
		int C = 0;
		int N = 0;
		int [] P;
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			
			while ((testCaseNumber += header ? 1 : 0) <= testCaseCount && (textLine = br.readLine()) != null) {
				if (header) {
					C = Integer.parseInt(textLine);
					N = Integer.parseInt(br.readLine());
				} else {
/////////////////////////////////////////////////////////////////////////////////////
					splitted = textLine.split("\\s+");
					N = Math.min(N, splitted.length);
					P = new int[N];
					for (i = 0; i < N; i++ ) {
						P[i] = Integer.valueOf(splitted[i]);
					}

					iterationCounter = 0;
					answer = shoppingByCreditTwoItems(C, P);

					w.printf("Case #%d: %d %d\n", testCaseNumber, answer[0], answer[1]);
					String textLineToShow = textLine;
					if (textLineToShow.length() > 100) {
						textLineToShow = textLineToShow.substring(0, 100) + " ...";
					}
					System.out.printf("Case #%d: {%s}, C=%d, answer={%d, %d}, iterations=%d\n", testCaseNumber, textLineToShow, C, answer[0], answer[1], iterationCounter);
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

	private int [] shoppingByCreditTwoItems(int C, int [] P) {
		int [] ret = new int[] {0, 0};
		int n = P.length;
		int maxSum = 0;
		for (int i = 0; i < n; i++) {
			if (P[i] < C) {
				for (int j = i + 1; j < n; j++) {
					iterationCounter++;
					int sum = P[i] + P[j];
					if (sum <= C && maxSum < sum) {
						maxSum = sum;
						ret[0] = i + 1;
						ret[1] = j + 1;
						
						if (sum == C) {
							return ret;
						}
					}
				}
			}
		}
		
		return ret;
	}
}