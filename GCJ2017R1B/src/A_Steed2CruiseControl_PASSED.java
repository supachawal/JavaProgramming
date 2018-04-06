import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class A_Steed2CruiseControl_PASSED {
	public static void main(String[] args) {
//		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
//			inputFileName = "-small-practice.in";
//			inputFileName = "-small-attempt0.in";
//			inputFileName = "-large-practice.in";
			inputFileName = "-large.in";
			inputFileName = A_Steed2CruiseControl_PASSED.class.getSimpleName().substring(0, 1) + inputFileName;
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
			A_Steed2CruiseControl_PASSED solver = new A_Steed2CruiseControl_PASSED();
			solver.solve(inputFile, outputWriter);
			outputWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

//		CommonUtils.postamble();
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
			int D = Integer.parseInt(splitted[0]);
			int N = Integer.parseInt(splitted[1]);

			System.out.printf("Case #%d: D=%d, N=%d\n", testCaseNumber, D, N);

			double maxDuration = 0;
			for (int i = 0; i < N; i++) {
				textLine = br.readLine();
				splitted = textLine.split("\\s+");
				maxDuration = Math.max(maxDuration, ((double)D - Integer.parseInt(splitted[0])) / Integer.parseInt(splitted[1]));
			}

			double answer = D / maxDuration;
			
			System.out.printf("=> answer=%.6f\n", answer);
			w.printf("Case #%d: %.6f\n", testCaseNumber, answer);
////////////////////////////////////////////////////////////////////////////////////
		}

		br.close();
	}
}