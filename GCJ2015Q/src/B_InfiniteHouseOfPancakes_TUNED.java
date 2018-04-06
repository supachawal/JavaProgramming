import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;

public class B_InfiniteHouseOfPancakes_TUNED {
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
			inputFileName = B_InfiniteHouseOfPancakes_TUNED.class.getSimpleName().substring(0, 1) + inputFileName;
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
			B_InfiniteHouseOfPancakes_TUNED solver = new B_InfiniteHouseOfPancakes_TUNED();
			solver.solve(inputFile, outputWriter);
			outputWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		CommonUtils.postamble();
	}

	private int D = 0;
	private int [] P;
	private ArrayDeque<Integer> [] chainP;
	private long iterationCounter = 0;

	@SuppressWarnings("unchecked")
	public boolean solve(final File aFile, PrintWriter w) {
		String textLine;
		boolean result = false;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		long answer;
		String [] splitted;

		boolean header = true;
		int totalPancakeCount;
		int i;
		int Pi; 
		int maxPi;
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			
			while ((testCaseNumber += header ? 1 : 0) <= testCaseCount && (textLine = br.readLine()) != null) {
				if (header) {
					D = Integer.parseInt(textLine);
				} else {
/////////////////////////////////////////////////////////////////////////////////////
					answer = 0;
					totalPancakeCount = 0;
					
					splitted = textLine.split("\\s+");
					D = Math.min(D, splitted.length);
					maxPi = -1;
					for (i = 0; i < D; i++ ) {
						Pi = Integer.valueOf(splitted[i]);
						if (Pi > maxPi) {
							maxPi = Pi;
						}
						totalPancakeCount += Pi;
					}

					P = new int[totalPancakeCount];
					chainP = new ArrayDeque[maxPi + 1];
					for (i = 0; i <= maxPi; i++) {
						chainP[i] = new ArrayDeque<Integer>();
					}
					for (i = 0; i < D; i++ ) {
						P[i] = Integer.valueOf(splitted[i]);
						chainP[P[i]].addLast(i);
					}
					for (i = D; i < totalPancakeCount; i++) {
						P[i] = 0;
						chainP[0].addLast(i);
					}
					
					iterationCounter = 0;
					answer = diningRecursive(maxPi);

					w.printf("Case #%d: %d", testCaseNumber, answer);
					if (testCaseNumber < testCaseCount)
						w.printf("\n");
					
					System.out.printf("Case #%d: {%s}, maxPi=%d, answer=%d, iterations=%d\n", testCaseNumber, textLine, maxPi, answer, iterationCounter);
//					System.out.printf("\tO={");
//					for (i = 1; i <= maxPi; i++) {
//						if (O[i] > 0) 
//							System.out.printf("%d:%d,", i, O[i]);
//					}
//					System.out.printf("}\n");
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

	private int diningRecursive(int maxPi) {
		if (maxPi <= 3)
			return maxPi;

		return Math.min(maxPi, _diningSpecialMinute(maxPi));
	}

	private int _diningSpecialMinute(int maxPi) {
		if (maxPi <= 3)
			return maxPi;
		
		int freq = chainP[maxPi].size();
		int minIterationCount = maxPi;
		boolean finished = false;
//		int loopCount = 0;

		for (int i = (maxPi + 1) >> 1; i < maxPi - freq /*&& shouldSplit(i, maxPi)*/ && !finished /*&& loopCount < 2*/; i++) {
			iterationCounter++;
//			loopCount++;
			
//			if (optimalSplitter != null) {
//				i = optimalSplitter;
//				improving = false;
//			}
			
			final int TARGET = 0; 
			Integer j = chainP[TARGET].removeFirst();
			P[j] += i;
			chainP[TARGET + i].addLast(j);
			
			Integer k = chainP[maxPi].removeFirst();
			P[k] -= i;
			chainP[maxPi - i].addLast(k);

			int newMaxPi = maxPi;
			while (chainP[newMaxPi].isEmpty()) {
				newMaxPi--;
			}
			
			int iterationCount = 1 + _diningSpecialMinute(newMaxPi);
			if (minIterationCount > iterationCount) {
				minIterationCount = iterationCount;
				if ((maxPi & 1) == 0) {
					finished = true;		//Case #11: {1000}, maxPi=1000, answer=63, visits=222887140
				}
			} else {
				finished = true;			//first worse after getting the miniumum
			}
			
			chainP[maxPi - i].removeLast();
			P[k] += i;
			chainP[maxPi].addFirst(k);

			chainP[TARGET + i].removeLast();
			P[j] -= i;
			chainP[TARGET].addFirst(j);
		} // end main for
		
		return minIterationCount;
	}
}