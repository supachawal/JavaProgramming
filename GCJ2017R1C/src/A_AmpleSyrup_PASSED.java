import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class A_AmpleSyrup_PASSED {
	public static void main(String[] args) {
		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			 inputFileName = "-sample1.in";
//			 inputFileName = "-sample2.in";
//			 inputFileName = "-small-practice.in";
//			 inputFileName = "-small-attempt0.in";
			inputFileName = "-large-practice.in";
//			 inputFileName = "-large.in";
			inputFileName = A_AmpleSyrup_PASSED.class.getSimpleName().substring(0, 1) + inputFileName;
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
			A_AmpleSyrup_PASSED solver = new A_AmpleSyrup_PASSED();
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
			int N = Integer.parseInt(splitted[0]);
			int K = Integer.parseInt(splitted[1]);
			
			System.out.printf("Case #%d: N=%d, K=%d\n", testCaseNumber, N, K);
			w.printf("Case #%d:", testCaseNumber);
			
			// load data
			int i;
			IntPair[] pancakes = new IntPair[N];
			
			for (i = 0; i < N; i++) {
				textLine = br.readLine();
				splitted = textLine.split("\\s+");
				pancakes[i] = new IntPair(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]));
			}
//			if (testCaseNumber == 53) {
//				System.err.printf("Breaked Test\n");
//			}

			// compute data
			Arrays.sort(pancakes);
			
			double answer = 0;
			memoCache = new double[N][K];
			for (i = K - 1; i < N; i++) {
				int r = pancakes[i].v1;
				int h = pancakes[i].v2;
				answer = Math.max(answer, Math.PI*r*r + 2*Math.PI*r*h + maxExposedArea(pancakes, i - 1, K - 1));
			}
					
			System.out.printf("=> answer=%s\n", removeTrailingZeros(String.format("%.9f", answer)));
			w.printf(" %s\n", removeTrailingZeros(String.format("%.9f", answer)));

			////////////////////////////////////////////////////////////////////////////////////
		}

		br.close();
	}

	private static double[][] memoCache;
	private static double maxExposedArea(IntPair[] pancakes, int i, int K) {
		if (i < 0 || K <= 0) {
			return 0;
		}
		
		double ret = memoCache[i][K];
		if (ret == 0) {
			ret = Math.max(maxExposedArea(pancakes, i - 1, K)
					, 2*Math.PI*pancakes[i].v1*pancakes[i].v2 + maxExposedArea(pancakes, i - 1, K - 1));
			memoCache[i][K] = ret;
		}
		
		return ret;
	}
	
	private static String removeTrailingZeros(String numStr) {
		int nAbsurdZeros = 0;
		int posPoint = numStr.indexOf('.') + 2;
		for (int i = numStr.length() - 1; i >= posPoint && numStr.charAt(i) == '0'; i--) {
			nAbsurdZeros++;	
		}
		
		return numStr.substring(0, numStr.length() - nAbsurdZeros);
	}

	private static class IntPair implements Comparable<IntPair> {
		public int v1;
		public int v2;
		public IntPair(final int initV1, final int initV2) {
			v1 = initV1;
			v2 = initV2;
		}
		public String toString() {
			return String.format("(%d,%d)", v1, v2);
		}
		@Override
		public int compareTo(IntPair o) {
			return this.v1 == o.v1 ? (this.v2 - o.v2) : (this.v1 - o.v1);
		}
		
	    public int hashCode() {
	    	long value = (long)v1 << 32 | v2; 
	    	return (int)(value ^ (value >>> 32));
	    }

	    public boolean equals(Object o) {
	        if (o instanceof IntPair) {
	            return this.compareTo((IntPair)o) == 0;
	        }
	        return false;
	    }
	}

}