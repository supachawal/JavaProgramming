import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/*
 Z -> 0
 W -> 2
 X -> 6
 G -> 8
 H -> 3
 U -> 4
 F -> 5
 I -> 9
 S -> 7
 O -> 1
   
 1 E    NO
 
 3 EE H   R T
 4   F   OR  U
 5 E F I      V
 9 E   IN
 7 EE   N  S  V
 */
public class A_Getting_the_Digits {
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
			inputFileName = A_Getting_the_Digits.class.getSimpleName().substring(0, 1) + inputFileName;
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
			A_Getting_the_Digits solver = new A_Getting_the_Digits();
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
		int[] digitSeq = {0, 2, 6, 8, 3, 4, 5, 9, 7, 1};
		char[][] extractor = { {'Z', 'E', 'R', 'O' }, { 'W', 'T', 'O' }, { 'X', 'I', 'S' }
				, { 'G', 'E', 'I', 'H', 'T' }, { 'H', 'T', 'R', 'E', 'E' }, { 'U', 'F', 'O', 'R' }
				, { 'F', 'I', 'V', 'E' }, { 'I', 'N', 'N', 'E' }, { 'S', 'E', 'V', 'E', 'N' }
				, { 'O', 'N', 'E' }
		};
		
		br = new BufferedReader(new FileReader(aFile));
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			//String[] splitted = textLine.split("\\s+");
			System.out.printf("Case #%d: S=%s\n", testCaseNumber, textLine);
			char[] S = textLine.toCharArray();
			int[] charFreq = new int[26];
			int[] digitFreq = new int[10];
			
			int i, j;
			
			for (char c : S) {
				charFreq[c - 'A']++;
			}
			
			for (i = 0; i < 10; i++) {
				while (charFreq[extractor[i][0] - 'A'] > 0) {
					for (char c : extractor[i]) {
						charFreq[c - 'A']--;
					}
					
					digitFreq[digitSeq[i]]++;
				}
			}
			
			StringBuilder answer = new StringBuilder();
			for (i = 0; i < 10; i++) {
				for (j = digitFreq[i]; j > 0; j--) {
					answer.append(i);
				}
			}			
			
			System.out.printf("=> answer=%s\n", answer);
			w.printf("Case #%d: %s\n", testCaseNumber, answer);
////////////////////////////////////////////////////////////////////////////////////
		}

		br.close();
	}
}