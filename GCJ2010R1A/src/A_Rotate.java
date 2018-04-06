import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class A_Rotate {
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
			A_Rotate solver = new A_Rotate();
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
		String [] splitted;
		boolean header = true;
		char [][] table, rtable;

		int N = 0, K = 0, i;
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
				
			while ((testCaseNumber += header ? 1 : 0) <= testCaseCount && (textLine = br.readLine()) != null) {
				if (header) {
					splitted = textLine.split("\\s+");
					N = Integer.parseInt(splitted[0]);
					K = Integer.parseInt(splitted[1]);
/////////////////////////////////////////////////////////////////////////////////////
				} else {
					table = new char[N][];
					for (i = 0; i < N && textLine != null; i++) {
						table[i] = textLine.toCharArray();
						if (i < N - 1) {
							textLine = br.readLine();
						}
					}

					rtable = new char[N][N];

					int [] dimen = rotate(table, rtable);
					
					iterationCounter = 0;
					boolean Rwin = checkJoinK(rtable, dimen[0], dimen[1], 'R', K);
					boolean Bwin = checkJoinK(rtable, dimen[0], dimen[1], 'B', K);
					answer = (Rwin && Bwin ? "Both" : Rwin ? "Red" : Bwin ? "Blue" : "Neither");
	
					w.printf("Case #%d: %s\n", testCaseNumber, answer);
					System.out.printf("Case #%d: N=%d, answer=%s, iterations=%d\n", testCaseNumber, N, answer, iterationCounter);
////////////////////////////////////////////////////////////////////////////////////
				}
				header = !header;
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

	private static int[] rotate(char[][]t, char[][]r) {
		int i, j, maxRow = -1, maxCol = -1;
		int n = t.length;
		
		for (i = 0; i < n; i++) {
			for (j = 0; j < n; j++) {
				r[i][j] = '.';
			}
		}
		
		i = 0;
		for (j = 0; j < n && i >= 0; j++) {
			int k = n - 1 - j;
			i = -1;
			for (int u = n - 1; u >= 0; u--) {
				if (t[k][u] != '.') {
					r[++i][j] = t[k][u];
				}
			}

			if (i >= 0) {
				maxCol = j + 1;
				
				if (i >= maxRow) {
					maxRow = i + 1;
				}
			}
		}
		
		return new int[] {maxRow, maxCol};
	}


	private boolean checkJoinK(char[][]r, int m, int n, char player, int K) {
		int i, j, count;
		int dimen = Math.max(m, n);
		
		// 1 of 4 Vertical
		for (j = 0; j < n; j++) {
			count = 0;
			for (i = 0; i < m; i++) {
				iterationCounter++;
				char c = r[i][j]; 
				if (c == player) {
					if (++count == K) {
						return true;		////////////////// SHORT CUT
					}
				} else if (c == '.') {
					break;
				} else {
					count = 0;
				}
			}
		}

		// 2 of 4 Horizontal
		for (i = 0; i < m; i++) {
			count = 0;
			for (j = 0; j < n; j++) {
				iterationCounter++;
				if (r[i][j] == player) {
					if (++count == K) {
						return true;		////////////////// SHORT CUT
					}
				} else {
					count = 0;
				}
			}
		}

		// 3 of 4 Downward Diagonal
		// 3.1 lower
		for (i = 1; i <= dimen - K; i++) {
			count = 0;
			for (j = 0; i + j < dimen; j++) {
				iterationCounter++;
				if (r[i + j][j] == player) {
					if (++count == K) {
						return true;		////////////////// SHORT CUT
					}
				} else {
					count = 0;
				}
			}
		}
		// 3.2 upper
		for (i = K - 1; i < dimen; i++) {
			count = 0;
			for (j = dimen - 1; i - (dimen - 1 - j) >= 0; j--) {
				iterationCounter++;
				if (r[i - (dimen - 1 - j)][j] == player) {
					if (++count == K) {
						return true;		////////////////// SHORT CUT
					}
				} else {
					count = 0;
				}
			}
		}
			
		// 4 of 4 Upward Diagonal
		// 4.1 upper
		for (i = K - 1; i < dimen; i++) {
			count = 0;
			for (j = 0; i - j >= 0; j++) {
				iterationCounter++;
				if (r[i - j][j] == player) {
					if (++count == K) {
						return true;		////////////////// SHORT CUT
					}
				} else {
					count = 0;
				}
			}
		}
		// 4.2 lower
		for (i = 1; i <= dimen - K; i++) {
			count = 0;
			for (j = dimen - 1; i + (dimen - 1 - j) < dimen; j--) {
				iterationCounter++;
				if (r[i + (dimen - 1 - j)][j] == player) {
					if (++count == K) {
						return true;		////////////////// SHORT CUT
					}
				} else {
					count = 0;
				}
			}
		}

		return false;
	}
}