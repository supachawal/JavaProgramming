import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class A_OversizedPancakeFlipper_PASSED {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-small-attempt0.in";
			inputFileName = "-large.in";
			inputFileName = A_OversizedPancakeFlipper_PASSED.class.getSimpleName().substring(0, 1) + inputFileName;
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
			A_OversizedPancakeFlipper_PASSED solver = new A_OversizedPancakeFlipper_PASSED();
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
			int answer = 0;
			String[] splitted = textLine.split("\\s+");
			char[] S = splitted[0].toCharArray();
			int K = Integer.parseInt(splitted[1]);
			
			System.out.printf("Case #%d: S=%s, K=%d, answer=", testCaseNumber, splitted[0], K);
			w.printf("Case #%d:", testCaseNumber);
			int i, len = S.length - K + 1;
			for (i = 0; i < len; i++) {
				if (S[i] == '-') {
					for (int k = 0; k < K; k++) {
						S[i + k] = (S[i + k] == '+' ? '-' : '+');
					}
					answer++;
				}
			}
			
			boolean impossible = false;
			for (i = S.length - 1; i >= len; i--) {
				if (S[i] == '-') {
					impossible = true;
					break;
				}
			}
			
			if (impossible) {
				System.out.printf("IMPOSSIBLE\n");
				w.printf(" IMPOSSIBLE\n");
			} else {
				System.out.printf("%d\n", answer);
				w.printf(" %d\n", answer);
			}
////////////////////////////////////////////////////////////////////////////////////
		}

		br.close();
	}

}