import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map.Entry;
import java.util.TreeMap;

public class C_BathroomStalls_PASSED {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
//			inputFileName = "-small-attempt0.in";
//			inputFileName = "-small-1-attempt0.in";
//			inputFileName = "-small-2-attempt0.in";
			inputFileName = "-large.in";
			inputFileName = C_BathroomStalls_PASSED.class.getSimpleName().substring(0, 1) + inputFileName;
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
			C_BathroomStalls_PASSED solver = new C_BathroomStalls_PASSED();
			solver.solve(inputFile, outputWriter);
			outputWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		CommonUtils.postamble();
	}

	public void solve(final File aFile, PrintWriter w) throws FileNotFoundException, IOException {
		String textLine;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		
		br = new BufferedReader(new FileReader(aFile));
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			if (testCaseNumber == 168) {
				System.out.printf("");
			}
			String[] splitted = textLine.split("\\s+");
			long N = Long.parseLong(splitted[0]);
			long K = Long.parseLong(splitted[1]);
			
			System.out.printf("Case #%d: N=%s, K=%d, answer=", testCaseNumber, N, K);
			w.printf("Case #%d:", testCaseNumber);

			TreeMap<Long, Long> mapEmptyStalls = new TreeMap<Long, Long>();
			mapEmptyStalls.put(N, 1L);
			long nL = 0, nR = 0, count;

			do {
				Entry<Long, Long> e = mapEmptyStalls.lastEntry();
				N = e.getKey();
				count = e.getValue();
				K -= count;
				
				mapEmptyStalls.remove(N);

				if (N > 1) {
					if ((N & 1) != 0) {
						nL = N/2;
						nR = nL;
						mapEmptyStalls.put(nL, count * 2 + mapEmptyStalls.getOrDefault(nL, 0L));
					} else {
						nL = (N - 1) / 2;
						nR = N - nL - 1;
						mapEmptyStalls.put(nL, count + mapEmptyStalls.getOrDefault(nL, 0L));
						mapEmptyStalls.put(nR, count + mapEmptyStalls.getOrDefault(nR, 0L));
					}
				} else {
					nL = 0;
					nR = 0;
				}
			} while (K > 0);
			
			System.out.printf(" %d %d\n", Math.max(nL, nR), Math.min(nL, nR));
			w.printf(" %d %d\n", Math.max(nL, nR), Math.min(nL, nR));
////////////////////////////////////////////////////////////////////////////////////
		}

		br.close();
	}
}