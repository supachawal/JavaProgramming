import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class B_StableNeighbors_CORRECT {
	public static void main(String[] args) {
//		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
//			inputFileName = "-sample3.in";
//			inputFileName = "-sample4.in";
//			inputFileName = "-small-practice.in";
//			inputFileName = "-small-attempt0.in";
			inputFileName = "-large-practice.in";
//			inputFileName = "-large.in";
			inputFileName = B_StableNeighbors_CORRECT.class.getSimpleName().substring(0, 1) + inputFileName;
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
			B_StableNeighbors_CORRECT solver = new B_StableNeighbors_CORRECT();
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

	private int[] colorFreq;
			
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
			// ============= load input
			int i;
			int N = Integer.parseInt(splitted[0]);
			colorFreq = new int[7];

			for (i = 1; i <= 6; i++) {
				colorFreq[Mane._colorBits[i]] = Integer.parseInt(splitted[i]);
			}

			// ============= computation
			
			Mane[] manes = new Mane[3];
			boolean isImpossible = false;

			// 1. for each secondary color pair with its least primary color
			for (byte c2 : Mane._secondaryColors) {
				int nC2 = colorFreq[c2];
				if (nC2 > 0) {
					byte c1 = Mane._primaryColorMapping[c2];
					int nC1 = colorFreq[c1];
					if (!(nC1 > nC2 || nC1 == nC2 && nC1 + nC2 == N)) {
						isImpossible = true;
						break;
					}

					manes[Mane._primaryColorIndex[c1]] = new Mane(c1, nC1, nC2);
				}
			}

			// 2. for the rest primary color
			if (!isImpossible) {
				for (byte c1 = 1; c1 <= 4; c1 <<= 1) {
					i = Mane._primaryColorIndex[c1];
					if (manes[i] == null) {
						manes[i] = new Mane(c1, colorFreq[c1]);
					}
				}

				Arrays.sort(manes, (v1, v2) -> (v2.ComponentCount() - v1.ComponentCount()));
				int nRest = manes[1].ComponentCount() + manes[2].ComponentCount();
				if (nRest > 0 && manes[0].ComponentCount() > nRest) {
					isImpossible = true;
				}
			}
			
			StringBuilder answer = new StringBuilder();
			
			if (isImpossible) {
				answer.append("IMPOSSIBLE");
			} else {
				int n1 = manes[1].ComponentCount();
				int n2 = manes[2].ComponentCount();
				
				for (int n0 = manes[0].ComponentCount() - 1; n0 >= 0; n0--) {
					answer.append(manes[0].getComponent(n0));

					int quota = 1 + (n1 + n2 > n0 + 1 ? 1 : 0); 
					if (n1 > 0 && quota-- > 0) {
						answer.append(manes[1].getComponent(--n1));
					} 
					if (n2 > 0 && quota-- > 0) {
						answer.append(manes[2].getComponent(--n2));
					} 
				}
			}
			
			System.out.printf("=> answer=%s\n", answer);
			w.printf(" %s\n", answer);
			
////////////////////////////////////////////////////////////////////////////////////
		}

		br.close();
	}

	private static class Mane {
		private static char[] _colorNames = {'\0', 'R', 'Y', 'O', 'B', 'V', 'G' };
		private static byte[] _secondaryColorMapping = {0, 6, 5, 0, 3};

		public static byte[] _primaryColorMapping = {0, 0, 0, 4, 0, 2, 1};
		public static byte[] _secondaryColors = {3, 5, 6};
		public static byte[] _primaryColorIndex = {-1, 0, 1, -1, 2};
		public static byte[] _colorBits = {0, 1, 3, 2, 6, 4, 5};
		public byte primaryColor;
		public int nPrimaryColors;
		public int nSecondaryColors;
		public int ComponentCount() {
			return Math.max(nPrimaryColors > 0 ? 1 : 0, nPrimaryColors - nSecondaryColors);
		}
		
		public Mane(byte primaryColor, int nPrimaryColors, int nSecondaryColors) {
			this.primaryColor = primaryColor;
			this.nPrimaryColors = nPrimaryColors;
			this.nSecondaryColors = nSecondaryColors;
		}

		public Mane(byte primaryColor, int nPrimaryColors) {
			this(primaryColor, nPrimaryColors, 0);
		}

		public String getComponent(int index) {
			if (index < 0 && index >= ComponentCount()) {
				System.err.println("Index out of range");
				System.exit(-1);
			}
			
			char primaryColorName = _colorNames[primaryColor];
			if (index > 0) {
				return String.valueOf(primaryColorName);
			}
			
			// index = 0
			char secondaryColorName = _colorNames[_secondaryColorMapping[primaryColor]];
			StringBuilder sb = new StringBuilder();
			if (nPrimaryColors > nSecondaryColors) {
				sb.append(primaryColorName);
			}
			for (int i = 0; i < nSecondaryColors; i++) {
				sb.append(secondaryColorName);
				sb.append(primaryColorName);
			}
			
			return sb.toString();
		}

		@Override
		public String toString() {
			return "Mane(primaryColor=" + primaryColor + ", nPrimaryColors=" + nPrimaryColors + ", nSecondaryColors="
					+ nSecondaryColors + ")";
		}
		
	}
}