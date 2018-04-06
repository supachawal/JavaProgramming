import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class B_StableNeighbors_HAMILTONIAN_CIRCUIT_BF {
	public static void main(String[] args) {
//		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
//			inputFileName = "-sample3.in";
			inputFileName = "-small-practice.in";
//			inputFileName = "-small-attempt0.in";
//			inputFileName = "-large-practice.in";
//			inputFileName = "-large.in";
			inputFileName = B_StableNeighbors_HAMILTONIAN_CIRCUIT_BF.class.getSimpleName().substring(0, 1) + inputFileName;
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
			B_StableNeighbors_HAMILTONIAN_CIRCUIT_BF solver = new B_StableNeighbors_HAMILTONIAN_CIRCUIT_BF();
			solver.solve(inputFile, outputWriter);
			outputWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.printf("============ END ============\n");
//		CommonUtils.postamble();
	}

	private char[] colorNames = {'R', 'O', 'Y', 'G', 'B', 'V' };
	private int[] colorBits = {1, 3, 2, 6, 4, 5};
	private int[] colorFreq;
	private int[] nodes;

	public void solve(final File aFile, PrintWriter w) throws FileNotFoundException, IOException {
		String textLine;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		
		br = new BufferedReader(new FileReader(aFile));
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			String[] splitted = textLine.split("\\s+");
			System.out.printf("Case #%d: N=%s, R=%s, O=%s, Y=%s, G=%s, B=%s, V=%s\n", testCaseNumber, splitted[0], splitted[1], splitted[2], splitted[3], splitted[4], splitted[5], splitted[6]);
			w.printf("Case #%d:", testCaseNumber);
			colorFreq = new int[6];
			for (int i = 0; i < 6; i++) {
				colorFreq[i] = Integer.parseInt(splitted[i + 1]);
			}
			int N = Integer.parseInt(splitted[0]);
			nodes = new int[N];
			Arrays.fill(nodes, -1);
			for (int i = 0; i < 6; i++) {
				colorFreq[i] = Integer.parseInt(splitted[i + 1]);
			}
			for (int i = 0; i < 6; i++) {
				if (colorFreq[i] > 0) {
					colorFreq[i]--;
					nodes[N - 1] = i;
					findHamiltonianCircuit_Bruteforce(N - 1, colorBits[i]);
					break;
				}
			}

			StringBuilder answer = new StringBuilder();
			if (nodes[0] < 0) {
				answer.append("IMPOSSIBLE");
			} else {
				for (int c : nodes) {
					answer.append(colorNames[c]);
				}
			}
			
			System.out.printf("=> answer=%s\n", answer);
			w.printf(" %s\n", answer);
			
////////////////////////////////////////////////////////////////////////////////////
		}

		br.close();
	}

	public boolean findHamiltonianCircuit_Bruteforce(int n, int lastColorBits) {
		if (n == 0) {
			return true;
		}
		
		for (int i = 0; i < 6; i++) {
			if (colorFreq[i] > 0 
					&& (colorBits[i] & lastColorBits) == 0
					&& (n > 1 || (colorBits[i] & colorBits[nodes[nodes.length - 1]]) == 0)) {
				colorFreq[i]--;
				nodes[n - 1] = i;
				if (findHamiltonianCircuit_Bruteforce(n - 1, colorBits[i]) )
					return true;
				colorFreq[i]++;
			}
		}
		
		return false;
	}
}