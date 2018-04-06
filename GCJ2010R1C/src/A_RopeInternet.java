import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.TreeSet;

public class A_RopeInternet {
	public static void main(String[] args) {
		CommonUtils.preamble();
		String inputFileName, outputFileName;

		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "A-sample1.in";
//			inputFileName = "A-sample2.in";
//			inputFileName = "A-small-practice.in";
			inputFileName = "A-large-practice.in";
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
			A_RopeInternet solver = new A_RopeInternet();
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

		int i, N;
		TreeSet<String> A;
		TreeSet<String> B;
		int a, b;
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
				
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
/////////////////////////////////////////////////////////////////////////////////////
				N = Integer.parseInt(textLine);
				A = new TreeSet<String>();
				B = new TreeSet<String>();
				
				for (i = 0; i < N && (textLine = br.readLine()) != null; i++) {
					splitted = textLine.split("\\s+");
					a = Integer.parseInt(splitted[0]);
					b = Integer.parseInt(splitted[1]);
					A.add(String.format("%06d%06d%04d", a, b, i));
					B.add(String.format("%06d%06d%04d", b, a, i));
				}
				
				if (N != i) {
					System.err.printf("ERROR: Reach end-of-file before finish reading N records of data\n");
					System.exit(-1);
				}
				
				iterationCounter = 0;
				answer = intersectionPointCount(A, B, 6);

				w.printf("Case #%d: %d\n", testCaseNumber, answer);
				System.out.printf("Case #%d: N=%d, answer=%d, iterations=%d\n", testCaseNumber, N, answer, iterationCounter);
////////////////////////////////////////////////////////////////////////////////////
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

	private long intersectionPointCount(TreeSet<String> A, TreeSet<String> B, final int pointDigits) {
		long ret = 0;
		Iterator<String> it = A.descendingIterator();
		while (it.hasNext()) {
			iterationCounter++;
			String aPack = it.next();
			String a = aPack.substring(0, pointDigits);
			String b = aPack.substring(pointDigits, pointDigits * 2);
			String lineId = aPack.substring(pointDigits * 2);
			ret += B.tailSet(b).size() - 1;
			B.remove(b + a + lineId);
		}
		
		return ret;
	}
}