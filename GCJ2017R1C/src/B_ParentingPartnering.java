import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;

public class B_ParentingPartnering {
	public static void main(String[] args) {
		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
			 inputFileName = "-sample1.in";
//			 inputFileName = "-sample2.in";
//			 inputFileName = "-small-practice.in";
//			 inputFileName = "-small-attempt0.in";
//			inputFileName = "-large-practice.in";
//			 inputFileName = "-large.in";
			inputFileName = B_ParentingPartnering.class.getSimpleName().substring(0, 1) + inputFileName;
		}

		if (args.length > 1) {
			outputFileName = args[1];
		} else {
			outputFileName = inputFileName.split("[.]in")[0].concat(".out");
		}

		System.out.printf("============ START %s --> %s ===========\n", inputFileName, outputFileName);

		File inputFile = new File(inputFileName);
		File outputFile = new File(outputFileName);

		try {
			PrintWriter outputWriter = new PrintWriter(outputFile, "ISO-8859-1");
			B_ParentingPartnering solver = new B_ParentingPartnering();
			solver.solve(inputFile, outputWriter);
			outputWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.printf("============ END ============\n");
	}

	public void solve(final File aFile, PrintWriter w) throws FileNotFoundException, IOException {
		String textLine;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;

		br = new BufferedReader(new FileReader(aFile));
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			String[] splitted = textLine.split("\\s+");
			int[] nA = new int[2];
			nA[0] = Integer.parseInt(splitted[0]);	//Cameron
			nA[1] = Integer.parseInt(splitted[1]); 	//Jamie
			
			System.out.printf("Case #%d: Ac=%d, Aj=%d\n", testCaseNumber, nA[0], nA[1]);
			w.printf("Case #%d:", testCaseNumber);
			
			// load data
			int[] parentingTable = new int[1440];
			Arrays.fill(parentingTable, -1);
			int[] parenting = new int[2];
			
			for (int k = 0; k < 2; k++) {
				for (int i = 0; i < nA[k]; i++) {
					int theOther = k ^ 1;
					textLine = br.readLine();
					splitted = textLine.split("\\s+");
					int from = Integer.parseInt(splitted[0]);
					int to = Integer.parseInt(splitted[1]) - 1;
					parenting[theOther] += to - from + 1;
					for (int j = from; j <= to; j++) {
						parentingTable[j] = theOther;
					}
				}
			}	
			System.out.printf("Cameron parents %d minutes, Jamie parents %d minutes\n", parenting[0], parenting[1]);

//			if (testCaseNumber == 53) {
//				System.err.printf("Breaked Test\n");
//			}

			// compute data
			memo = new HashMap<IntPack, Integer>();
			int answer = minShifts(parentingTable, 720 - parenting[0], 720 - parenting[1], 1339, -1);
			
			System.out.printf("=> answer=%d\n", answer);
			w.printf(" %d\n", answer);

			////////////////////////////////////////////////////////////////////////////////////
		}
		
		br.close();
	}

	private static HashMap<IntPack, Integer> memo;
	
	private static int minShifts(int[] parentingTable, int free0, int free1, int k, int lastParent) {
		if (k < 0) {
			return 0;
		}

		IntPack key = new IntPack(free0, free1, k, lastParent);
		if (memo.containsKey(key)) {
			return memo.get(key);
		}
		
		int ret = 1441;
		
		int parent = parentingTable[k]; 
		if (parent >= 0) {
			ret = (lastParent == parent ? 0 : 1) + minShifts(parentingTable, free0, free1, k - 1, parent);
		} else {
			if (free0 > 0) {
				ret = (lastParent == 0 ? 0 : 1) + minShifts(parentingTable, free0 - 1, free1, k - 1, 0);
			}

			if (free1 > 0) {
				ret = Math.min(ret, (lastParent == 1 ? 0 : 1) + minShifts(parentingTable, free0, free1 - 1, k - 1, 1));
			}
		}

		memo.put(key, ret);
		return ret;
	}
	
}