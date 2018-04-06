import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class B_Milkshakes {
	public class CustomerFavorite {
		public int flavor;
		public boolean malted;
		public CustomerFavorite(int initFlavor, boolean initMalted) {
			flavor = initFlavor;
			malted = initMalted;
		}
	}
	
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
			inputFileName = B_Milkshakes.class.getSimpleName().substring(0, 1) + inputFileName;
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
			B_Milkshakes solver = new B_Milkshakes();
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
		String [] splitted;
		String answer;

		boolean header = true;
		int N = 0;	 // # of flavors
		int M = 0;	// # of customers
		int T;
		CustomerFavorite [][] X; //customersLikes;
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			
			while ((testCaseNumber += header ? 1 : 0) <= testCaseCount && (textLine = br.readLine()) != null) {
				if (header) {
					N = Integer.parseInt(textLine);
				} else {
/////////////////////////////////////////////////////////////////////////////////////
					M = Integer.parseInt(textLine);
					X = new CustomerFavorite[M][];
					for (int i = 0; i < M; i++ ) {
						splitted = br.readLine().split("\\s+");
						T = Integer.parseInt(splitted[0]);
						X[i] = new CustomerFavorite[T];
						for (int j = 0; j < T; j++) {
							X[i][j] = new CustomerFavorite(Integer.parseInt(splitted[1 + 2 * j]) - 1, Integer.parseInt(splitted[2 + 2 * j]) != 0);
						}
					}

					X = sortedCustomerFavoriteArray(X, M);
					iterationCounter = 0;
					answer = formatMilkShakeBatch(optimalMilkShakeBatch(N, X, M), N);

					w.printf("Case #%d: %s\n", testCaseNumber, answer);
					System.out.printf("Case #%d: N=%d, M=%d, answer=%s, iterations=%d\n", testCaseNumber, N, M, CommonUtils.showString(answer, 40), iterationCounter);
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

	private static CustomerFavorite[][] sortedCustomerFavoriteArray(CustomerFavorite[][] X, int m) {
		CustomerFavorite[][] result = new CustomerFavorite[m][];
		AbstractBinaryHeap bh = new MinHeap(m);
		int i;
		
		for (i = 0; i < m; i++) {
			bh.insertItem(new KeyValuePair(i, X[i].length));
		}
		
		i = 0;
		while (bh.getItemCount() > 0) {
			KeyValuePair r = bh.popRoot();
			result[i++] = X[r.key];
		}
		
		return result;
	}

	private static String formatMilkShakeBatch(BigInteger b, int n) {
		if (b.compareTo(BigInteger.ZERO) < 0) {
			return "IMPOSSIBLE";
		}
		
		StringBuilder sb = new StringBuilder();
		int last = n - 1;
		for(int i = 0; i <= last; i++) {
			sb.append(b.testBit(i) ? '1' : '0');
			
			if (i < last) {
				sb.append(' ');
			}
		}
		return sb.toString();
	}

	private static BigInteger optimalMilkShakeBatch(int n, CustomerFavorite[][] X, int m) {
		boolean allSatisfied = false;
		BigInteger b = BigInteger.ZERO;
		BigInteger upperLimit = BigInteger.ONE.shiftLeft(n);
		for (int maltedCount = 0; maltedCount <= n && !allSatisfied; maltedCount++) {
			for (b = BigInteger.ZERO; b.compareTo(upperLimit) < 0; b = b.add(BigInteger.ONE)) {
				if (b.bitCount() == maltedCount) {
					allSatisfied = true;
					
					for (int i = 0; i < m; i++) {
						boolean individualSatisfied = false;
						for (CustomerFavorite cf : X[i]) {
							if (cf.malted == b.testBit(cf.flavor)) {
								individualSatisfied = true;
								break;
							}
						}
						if (!individualSatisfied) {
							allSatisfied = false;
							break;
						}
					}

					if (allSatisfied) {
						break;
					}
				}
			}
		}
		
		if (!allSatisfied) {
			b = BigInteger.ONE.negate();
		}
		
		return b;
	}
}