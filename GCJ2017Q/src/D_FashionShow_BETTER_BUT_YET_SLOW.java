import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class D_FashionShow_BETTER_BUT_YET_SLOW {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
//			inputFileName = "-small-attempt0.in";
			inputFileName = "-small-practice.in";
//			inputFileName = "-large.in";
			inputFileName = D_FashionShow_BETTER_BUT_YET_SLOW.class.getSimpleName().substring(0, 1) + inputFileName;
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
			D_FashionShow_BETTER_BUT_YET_SLOW solver = new D_FashionShow_BETTER_BUT_YET_SLOW();
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
			int N = Integer.parseInt(splitted[0]);
			int M = Integer.parseInt(splitted[1]);
			
			System.out.printf("Case #%d: N=%d, M=%d, answer=", testCaseNumber, N, M);
			w.printf("Case #%d:", testCaseNumber);
			Rook rook = new Rook(N);
			Bishop bishop = new Bishop(N);

			for (int m = 0; m < M; m++) {
				textLine = br.readLine();
				splitted = textLine.split("\\s+");
				char model = splitted[0].charAt(0);
				int row = Integer.parseInt(splitted[1]) - 1;
				int col = Integer.parseInt(splitted[2]) - 1;

				if (model == '+' || model == 'o') {
					bishop.preplace(row, col);
				} 
				
				if (model == 'x' || model == 'o') {
					rook.preplace(row, col);
				}
			}
			
			rook.memorizeSolution();
			rook.maximizePlacing();
			bishop.memorizeSolution();
			bishop.maximizePlacing();
			
			StringBuilder answer = new StringBuilder();
			int nStylePoints = 0;
			int nStrokes = 0;
			for (int row = 0; row < N; row++) {
				boolean rookPreoccupied = rook.rowPreoccupied[row];
				
				for (int col = 0; col < N; col++) {
					boolean rookPlaced = rook.solutionGrid[row][col];
					boolean bishopPlaced = bishop.solutionGrid[row][col];
					
					if (rookPlaced || bishopPlaced) {
						if (rookPlaced && bishopPlaced) {
							nStylePoints += 2;
							if (!rookPreoccupied || !bishop.udiagPreoccupied[row + col]) {
								answer.append('o').append(' ').append(row + 1).append(' ').append(col + 1).append('\n');
								nStrokes++;
							}
						} else {
							nStylePoints++;
							if (rookPlaced) {
								if (!rookPreoccupied) {
									answer.append('x').append(' ').append(row + 1).append(' ').append(col + 1).append('\n');
									nStrokes++;
								}
							} else { // bishop
								if (!bishop.udiagPreoccupied[row + col]) {
									answer.append('+').append(' ').append(row + 1).append(' ').append(col + 1).append('\n');
									nStrokes++;
								}
							}
						}

					}
				}
			}
			
			System.out.printf(" %d %d\n%s", nStylePoints, nStrokes, answer);
			w.printf(" %d %d\n%s", nStylePoints, nStrokes, answer);
		}

		br.close();
	}

	private static class Rook {
		public boolean[][] solutionGrid;
		public boolean[] rowPreoccupied;
		
		private int n;
		private int count, maxCount;
		private boolean[][] grid;
		private boolean[] rowOccupied;
		private boolean[] colOccupied;
		private int[] rowIndices;
		
		public Rook(int N) {
			n = N;
			count = 0;
			grid = new boolean[N][N];
			solutionGrid = new boolean[N][N];
			rowPreoccupied = new boolean[N];
			rowOccupied = new boolean[N];
			colOccupied = new boolean[N];
		}

		public void preplace(int row, int col) {
			place(row, col, true);
			rowPreoccupied[row] = true;
		}

		public void place(int row, int col, boolean isPlaced) {
			grid[row][col] = isPlaced;
			rowOccupied[row] = isPlaced;
			colOccupied[col] = isPlaced;
			count += isPlaced ? 1 : -1;
		}

		public void memorizeSolution() {
			for (int i = 0; i < n; i++) {
				System.arraycopy(grid[i], 0, solutionGrid[i], 0, n);
			}
		}
		
		public void maximizePlacing() {
			int i;
			int nRows = 0;
			for (i = 0; i < n; i++) {
				if (!rowPreoccupied[i]) {
					nRows++;
				}
			}
			
			rowIndices = new int[nRows];
			nRows = 0;
			for (i = 0; i < n; i++) {
				if (!rowPreoccupied[i]) {
					rowIndices[nRows++] = i;
				}
			}

			maxCount = count;
			maximizePlacing(nRows - 1);
		}

		private void maximizePlacing(int rowIndex) {
			if (rowIndex < 0 || maxCount >= n) {
				return;
			}
			
			int row = rowIndices[rowIndex];
			if (!rowOccupied[row]) {
				for (int col = n - 1; col >= 0; col--) {
					if (!colOccupied[col]) {
						place(row, col, true);
						if (count > maxCount) {
							memorizeSolution();
							maxCount = count;
						}
						maximizePlacing(rowIndex - 1);
						place(row, col, false);
					}
				}
			}
		}
	}

	private static class Bishop {
		public boolean[][] solutionGrid;
		public boolean[] udiagPreoccupied;

		private int n;
		private int count, maxCount;
		private boolean[][] grid;
		private boolean[] udiagOccupied;
		private boolean[] ddiagOccupied;
		private int[] udiagIndices;
		
		public Bishop(int N) {
			n = N;
			count = 0;
			grid = new boolean[N][N];
			solutionGrid = new boolean[N][N];
			udiagPreoccupied = new boolean[2 * N - 1];
			udiagOccupied = new boolean[2 * N - 1];
			ddiagOccupied = new boolean[2 * N - 1];
		}

		public void memorizeSolution() {
			for (int i = 0; i < n; i++) {
				System.arraycopy(grid[i], 0, solutionGrid[i], 0, n);
			}
		}

		public void preplace(int row, int col) {
			place(row, col, true);
			udiagPreoccupied[row + col] = true;
		}
		
		public void place(int row, int col, boolean isPlaced) {
			grid[row][col] = isPlaced;
			udiagOccupied[row + col] = isPlaced;
			ddiagOccupied[n - 1 - (row - col)] = isPlaced;
			count += isPlaced ? 1 : -1;
		}
		
		public void maximizePlacing() {
			int i, nUDiags = 0, nAllUDiags = 2 * n - 1;
			
			for (i = 0; i < nAllUDiags; i++) {
				if (!udiagPreoccupied[i]) {
					nUDiags++;
				}
			}
			
			udiagIndices = new int[nUDiags];
			nUDiags = 0;
			for (i = 0; i < nAllUDiags; i++) {
				if (!udiagPreoccupied[i]) {
					udiagIndices[nUDiags++] = i;
				}
			}

			maxCount = count;
			maximizePlacing(nUDiags - 1);
		}
		
		private void maximizePlacing(int udiagIndex) {
			if (udiagIndex < 0 || maxCount >= 2 * n - 2) {
				return;
			}
			
			int udiag = udiagIndices[udiagIndex];
			
			if (!udiagOccupied[udiag]) {
				int minRow = Math.max(0,  udiag - (n - 1));
				for (int row = Math.min(n - 1, udiag); row >= minRow; row--) {
					int col = udiag - row;
					if (!ddiagOccupied[n - 1 - (row - col)]) {
						place(row, col, true);
						if (count > maxCount) {
							memorizeSolution();
							maxCount = count;
						}
						maximizePlacing(udiagIndex - 1);
						place(row, col, false);
					}
				}
			}
		}
	}
}