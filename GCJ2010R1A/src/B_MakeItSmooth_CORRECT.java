import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class B_MakeItSmooth_CORRECT {
	public static void main(String[] args) {
		CommonUtils.preamble();
		String inputFileName, outputFileName;

		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "B-sample1.in";
//			inputFileName = "B-sample2.in";
			inputFileName = "B-small-practice.in";
//			inputFileName = "B-large-practice.in";
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
			B_MakeItSmooth_CORRECT solver = new B_MakeItSmooth_CORRECT();
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
	private int [] pixels;
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
					pixels = new int[N];
					
					for (int i = 0; i < N; i++) {
						pixels[i] = Integer.parseInt(splitted[i]);
					}

					System.out.printf("Case #%d: D=%d, I=%d, M=%d, N=%d, pixels=[%s],", testCaseNumber, D, I, M, N, CommonUtils.showString(textLine, 50));
					iterationCounter = 0;
					answer = minimumSmoothingCost(N, pixels[N - 1]);
	
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

	/*
	 * 
   int Solve(pixels[], final_value) {
    if (pixels is empty) return 0

    // Try deleting
    best = Solve(pixels[1 to N-1], final_value) + D

    // Try all values for the previous pixel value
    for (all prev_value) {
      prev_cost = Solve(pixels[1 to N-1], prev_value)
      move_cost = |final_value - pixels[N]|
      num_inserts = (|final_value - prev_value| - 1) / M
      insert_cost = num_inserts * I
      best = min(best, prev_cost + move_cost + insert_cost)
    }
    return best
	 */
	int minimumSmoothingCost(int n, int final_value) {
	    if (n <= 0)
	    	return 0;

	    // Try deleting
	    int best = minimumSmoothingCost(n - 1, final_value) + D;
		int move_cost = Math.abs(final_value - pixels[n - 1]);

	    // Try all values for the previous pixel value
	    for (int i = n - 2; i >= 0; i--) {
			int prev_value = pixels[i];
			int prev_cost = minimumSmoothingCost(n - 1, prev_value);
			int insert_cost = LARGE_COST;
			if (M != 0) {
				int num_inserts = (Math.abs(final_value - prev_value) - 1) / M;
				insert_cost = num_inserts * I;
			}
			  
			best = Math.min(best, prev_cost + move_cost + insert_cost);
	    }
	    return best;
	}
}