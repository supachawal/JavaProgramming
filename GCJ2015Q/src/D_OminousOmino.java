import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class D_OminousOmino {
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
			inputFileName = D_OminousOmino.class.getSimpleName().substring(0, 1) + inputFileName;
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
			D_OminousOmino solver = new D_OminousOmino();
			solver.solve(inputFile, outputWriter);
			outputWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CommonUtils.postamble();
	}

	public boolean solve(final File aFile, PrintWriter w) {
		String textLine;
		boolean result = false;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		xOmino.Player answer = xOmino.Player.UNKNOWN;
		String [] splitted;

		xOmino o;
		int X, R, C;			// R x C
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
/////////////////////////////////////////////////////////////////////////////////////
				splitted = textLine.split("\\s+");
				X = Integer.parseInt(splitted[0]);
				R = Integer.parseInt(splitted[1]);
				C = Integer.parseInt(splitted[2]);
				o = new xOmino(X, R, C);
				System.out.printf("Case #%d: {X=%d, L=%d, S=%d}, ", testCaseNumber, X, o.L, o.S);
				System.out.flush();

				answer = o.winner();

				w.printf("Case #%d: %s\n", testCaseNumber, answer == xOmino.Player.RICHARD ? "RICHARD": answer == xOmino.Player.GABRIEL ? "GABRIEL" : "UNKNOWN");
				w.flush();
				
				System.out.printf("answer=%s\n", answer == xOmino.Player.RICHARD ? "RICHARD": answer == xOmino.Player.GABRIEL ? "GABRIEL" : "UNKNOWN");
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
}