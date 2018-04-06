import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class A_AlphabetCake_PASSED {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
//			inputFileName = "-small-practice.in";
//			inputFileName = "-small-attempt0.in";
			inputFileName = "-large-practice.in";
//			inputFileName = "-large.in";
			inputFileName = A_AlphabetCake_PASSED.class.getSimpleName().substring(0, 1) + inputFileName;
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
			A_AlphabetCake_PASSED solver = new A_AlphabetCake_PASSED();
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
			String[] splitted = textLine.split("\\s+");
			int R = Integer.parseInt(splitted[0]);
			int C = Integer.parseInt(splitted[1]);
			char[][] cake = new char[R][];
			int i, j;
			
			for (i = 0; i < R; i++) {
				textLine = br.readLine();
				cake[i] = textLine.toCharArray();
			}

			System.out.printf("Case #%d: R=%d, C=%d, given=\n", testCaseNumber, R, C);
			for (i = 0; i < R; i++) {
				for (j = 0; j < C; j++) {
					System.out.printf("%c", cake[i][j]);
				}
				System.out.printf("\n");
			}
			
			fillRect(cake, R, C);
			
			System.out.printf("=> answer=\n", testCaseNumber, R, C);
			w.printf("Case #%d:\n", testCaseNumber);
			for (i = 0; i < R; i++) {
				for (j = 0; j < C; j++) {
					System.out.printf("%c", cake[i][j]);
					w.printf("%c", cake[i][j]);
				}
				System.out.printf("\n");
				w.printf("\n");
			}
			
////////////////////////////////////////////////////////////////////////////////////
		}

		br.close();
	}

	private static void fillRect(char[][] cake, int R, int C) {
		int i, j, k;
		for (i = 0; i < R; i++) {
			for (j = 0; j < C; j++) {
				char c = cake[i][j];
				if (c != '?') {
					for (k = j - 1; k >= 0 && cake[i][k] == '?'; k--) {
						cake[i][k] = c;
					}
					for (k = j + 1; k < C && cake[i][k] == '?'; k++) {
						cake[i][k] = c;
					}
					j = k - 1;
				}
			}
		}

		for (j = 0; j < C; j++) {
			for (i = 0; i < R; i++) {
				char c = cake[i][j];
				if (c != '?') {
					for (k = i - 1; k >= 0 && cake[k][j] == '?'; k--) {
						cake[k][j] = c;
					}
					for (k = i + 1; k < R && cake[k][j] == '?'; k++) {
						cake[k][j] = c;
					}
					i = k - 1;
				}
			}
		}
	}
}