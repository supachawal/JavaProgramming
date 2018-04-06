import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class C_T9Spelling {
	public static void main(String[] args) {
		CommonUtils.preamble();
		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
			inputFileName = "-small-practice.in";
//			inputFileName = "-large-practice.in";
			inputFileName = C_T9Spelling.class.getSimpleName().substring(0, 1) + inputFileName;
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
			C_T9Spelling solver = new C_T9Spelling();
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

		String answer;
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			
			while ((++testCaseNumber) <= testCaseCount && (textLine = br.readLine()) != null) {
/////////////////////////////////////////////////////////////////////////////////////
				System.out.printf("Case #%d: {%s}", testCaseNumber, CommonUtils.showString(textLine, 80));
				iterationCounter = 0;
				answer = T9(textLine);
				w.printf("Case #%d: %s\n", testCaseNumber, answer);
				System.out.printf(", answer={%s}, iterations=%d\n", CommonUtils.showString(answer, 80), iterationCounter);
				
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
	
	private static char [] T9EndGroup = {'a', 'd', 'g', 'j', 'm', 'p', 't', 'w'};
	private String T9(char c) {
		StringBuilder sb = new StringBuilder();
		int i = 0, j;
		int r = 0;
		if (c == ' ') {
			iterationCounter++;
			sb.append("0");
		} else if (c >= 'a' && c <= 'z') {
			i = 9;
			for (j = T9EndGroup.length - 1; j >= 0; j--) {
				iterationCounter++;
				if (c < T9EndGroup[j]) {
					i--;
				} else {
					break;
				}
			}
			r = c - T9EndGroup[j] + 1;
		}
		
		while (r-- > 0) {
			sb.append(i);
		}
		
		return sb.toString();
	}

	private String T9(String s) {
		char [] cs = s.toCharArray();
		int n = cs.length;
		StringBuilder sb = new StringBuilder();
		String prev, curr = null;
		
		for (int i = 0; i < n; i++) {
			prev = curr;
			curr = T9(cs[i]);
			if (prev != null && prev.startsWith(curr.substring(0, 1))) {
				sb.append(" ");
			}
			sb.append(curr);
		}
		return sb.toString();
	}
}