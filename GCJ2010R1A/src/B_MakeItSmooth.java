import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;

public class B_MakeItSmooth {
	private class Clue {
		public int problemIndex;
		public int problemValue;
		public int previousValue;
		public int nextValue;
		public Clue(final int initProblemIndex, final int initProblemValue, final int initPreviousValue, final int initNextValue) {
			problemIndex = initProblemIndex;
			problemValue = initProblemValue;
			previousValue = initPreviousValue;
			nextValue = initNextValue;
		}
		public String toString() {
			return String.format("problemIndex=%d, problemValue=%d, prev=%d, next=%d", problemIndex, problemValue, previousValue, nextValue);
		}
	}
	
	public static void main(String[] args) {
		CommonUtils.preamble();
		String inputFileName, outputFileName;

		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "B-sample1.in";
//			inputFileName = "B-sample2.in";
//			inputFileName = "B-small-practice.in";
			inputFileName = "B-large-practice.in";
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
			B_MakeItSmooth solver = new B_MakeItSmooth();
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
	private int D = 0;  // cost of deletion
	private int I = 0;  // cost of insertion
	private int M = 0;  // smoothness criteria
	private LinkedList<Integer> pixelList;
	private final int LARGE_COST = Integer.MAX_VALUE / 2;

	public boolean solve(final File aFile, PrintWriter w) {
		String textLine;
		boolean result = false;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		int answer;
		String [] splitted;
		boolean header = true;
		
		int N = 0;  // pixel count

		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
				
			while ((testCaseNumber += header ? 1 : 0) <= testCaseCount && (textLine = br.readLine()) != null) {
				if (header) {
					splitted = textLine.split("\\s+");
					D = Integer.parseInt(splitted[0]);
					I = Integer.parseInt(splitted[1]);
					M = Integer.parseInt(splitted[2]);
					N = Integer.parseInt(splitted[3]);
/////////////////////////////////////////////////////////////////////////////////////
				} else {
					splitted = textLine.split("\\s+");
					N = Math.min(N, splitted.length);
					pixelList = new LinkedList<Integer>() ;
					
					for (int i = 0; i < N; i++) {
						pixelList.addLast(Integer.parseInt(splitted[i]));
					}

					System.out.printf("Case #%d: D=%d, I=%d, M=%d, N=%d, pixels=%s,", testCaseNumber, D, I, M, N, CommonUtils.showString(pixelList.toString(), 50));
					iterationCounter = 0;
					answer = minimumSmoothingCost();
	
					w.printf("Case #%d: %d\n", testCaseNumber, answer);
					System.out.printf(" answer=%d, iterations=%d\n", answer, iterationCounter);
////////////////////////////////////////////////////////////////////////////////////
	/*
	 * In Case #1, decreasing the 7 to 3 costs 4 and is the cheapest solution. 
	 * In Case #2, deleting is extremely expensive; 
	 * 		it's cheaper to insert elements so your final array looks like [1, 6, 11, 16, 21, 26, 31, 36, 41, 46, 50, 45, 40, 35, 30, 25, 20, 15, 10, 7]. 
	 */
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

	private Clue smoothingClue() {
		Iterator<Integer> it = pixelList.iterator();
		int index = 0;
		int prev = -1;
		if (it.hasNext()) {
			prev = it.next();
//			if (!it.hasNext()) {
//				return new Clue(0, prev, -1, -1);	///////// SHORT CUT
//			}
		}
		
		while (it.hasNext()) {
			index++;
			int v = it.next();
			int diff = v - prev; 

			if (diff < -M || diff > M) {
				int nextValue = -1; 
				if (it.hasNext()) {
					nextValue = it.next();
				}
				return new Clue(index, v, prev, nextValue);	///////// SHORT CUT
			}
			prev = v;
		}
		
		return null;
	}

	private int minimumSmoothingCost() {
		int ret = 0;
		Clue newClue;
		iterationCounter++;
		if ((newClue = smoothingClue()) != null) {
			ret = min(minCostIfDelete(newClue)
					, minCostIfInsert(newClue)
					, minCostIfModify(newClue)
					);
			if (newClue.problemIndex == 1) {
				Clue clue2 = new Clue(0, newClue.previousValue, newClue.problemValue, newClue.problemValue);
				
				ret = min(ret
						, minCostIfDelete(clue2)
						, minCostIfModify(clue2)
						);
			}
		}
		
		return ret;
	}

	private int minCostIfDelete(final Clue clue) {
		int ret;
		// perform change with backup before
		Integer backup = pixelList.remove(clue.problemIndex);
		// recurse
		ret = D + minimumSmoothingCost();
		// restore (undo)
		pixelList.add(clue.problemIndex, backup);
		
		return ret;
	}

	private int minCostIfInsert(final Clue clue) {
		if (clue.problemIndex == 0 && clue.previousValue < 0 && clue.nextValue < 0) { // single
			return I;
		}
		if (M == 0) {
			return LARGE_COST;
		}
		
		int ret = LARGE_COST;

		// perform change with backup before
		if (clue.previousValue + M <= 255  &&  Math.abs(clue.previousValue + M - clue.problemValue) < Math.abs(clue.previousValue - clue.problemValue)) {
			pixelList.add(clue.problemIndex, clue.previousValue + M);
			// recurse
			ret = I + minimumSmoothingCost();
		}

		// perform change more
		if (clue.previousValue - M >= 0  &&  Math.abs(clue.previousValue - M - clue.problemValue) < Math.abs(clue.previousValue - clue.problemValue)) {
			pixelList.add(clue.problemIndex, clue.previousValue - M);
			// recurse
			ret = Math.min(ret, I + minimumSmoothingCost());
		}
		
		// restore (undo)
		if (ret < LARGE_COST) {
			pixelList.remove(clue.problemIndex);
		}
		
		return ret;
	}

	private int minCostIfModify(final Clue clue) {
		if (M == 0 || clue.previousValue < 0 && clue.nextValue < 0) { // strict criteria or single
			return LARGE_COST;
		}

		int ret = LARGE_COST;

		// perform change with backup before
		if (clue.previousValue + M <= 255  
				&&  (clue.nextValue < 0 || Math.abs(clue.previousValue + M - clue.nextValue) <= M || Math.abs(clue.previousValue + M - clue.nextValue) < Math.abs(clue.problemValue - clue.nextValue))
			) {
			pixelList.set(clue.problemIndex, clue.previousValue + M);
			// recurse
			ret = Math.abs(clue.previousValue + M - clue.problemValue) + minimumSmoothingCost();
		}

		// perform change more
		if (clue.previousValue - M >= 0
				&&  (clue.nextValue < 0 || Math.abs(clue.previousValue - M - clue.nextValue) <= M || Math.abs(clue.previousValue - M - clue.nextValue) < Math.abs(clue.problemValue - clue.nextValue))
			) {
			pixelList.set(clue.problemIndex, clue.previousValue - M);
			// recurse
			ret = Math.min(ret, Math.abs(clue.previousValue - M - clue.problemValue) + minimumSmoothingCost());
		}
		
		// restore (undo)
		pixelList.set(clue.problemIndex, clue.problemValue);
		
		return ret;
	}
	
	static int min(int a, int b, int c) {
		return Math.min(a, Math.min(b, c));
	}
	
}