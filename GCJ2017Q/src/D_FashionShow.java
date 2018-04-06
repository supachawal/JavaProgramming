import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class D_FashionShow {
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
			inputFileName = D_FashionShow.class.getSimpleName().substring(0, 1) + inputFileName;
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
			D_FashionShow solver = new D_FashionShow();
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
			BishopBlack bishopBlack = new BishopBlack(N);
			BishopWhite bishopWhite = new BishopWhite(N);

			for (int m = 0; m < M; m++) {
				textLine = br.readLine();
				splitted = textLine.split("\\s+");
				char model = splitted[0].charAt(0);
				int row = Integer.parseInt(splitted[1]) - 1;
				int col = Integer.parseInt(splitted[2]) - 1;

				if (model == '+' || model == 'o') {
					if ((row + col) % 2 == 0) {
						bishopWhite.preplace(row, col);
					} else {
						bishopBlack.preplace(row, col);
					}
				} 
				
				if (model == 'x' || model == 'o') {
					rook.preplace(row, col);
				}
			}
			
			rook.memorizeSolution();
			rook.maximizePlacing();
			bishopBlack.memorizeSolution();
			bishopBlack.maximizePlacing();
			bishopWhite.memorizeSolution();
			bishopWhite.maximizePlacing();
			
			StringBuilder answer = new StringBuilder();
			int nStylePoints = 0;
			int nStrokes = 0;
			for (int row = 0; row < N; row++) {
				boolean rookPreoccupied = rook.isPreoccupied(row);
				
				for (int col = 0; col < N; col++) {
					boolean rookPlaced = rook.solutionGrid[row][col];
					boolean bishopPlaced = (bishopBlack.solutionGrid[row][col] || bishopWhite.solutionGrid[row][col]);
					
					if (rookPlaced || bishopPlaced) {
						if (rookPlaced && bishopPlaced) {
							nStylePoints += 2;
							if (!rookPreoccupied || !(bishopBlack.isPreoccupied(row, col) || bishopWhite.isPreoccupied(row, col))) {
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
								if (!(bishopBlack.isPreoccupied(row, col) || bishopWhite.isPreoccupied(row, col))) {
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
		
		protected int n;
		protected boolean[][] grid;
		protected boolean[] rowOccupied;
		protected boolean[] rowPreoccupied;
		protected boolean[] colOccupied;
		protected int[] rowIndices;
		protected boolean solutionFound;
		public Rook() {
		}		
		public Rook(int N) {
			n = N;
			grid = new boolean[N][N];
			solutionGrid = new boolean[N][N];
			rowPreoccupied = new boolean[N];
			rowOccupied = new boolean[N];
			colOccupied = new boolean[N];
		}

		public boolean isPreoccupied(int row) {
			return rowPreoccupied[row];
		}

		public void preplace(int row, int col) {
			place(row, col, true);
			rowPreoccupied[row] = true;
		}

		public void place(int row, int col, boolean isPlaced) {
			grid[row][col] = isPlaced;
			rowOccupied[row] = isPlaced;
			colOccupied[col] = isPlaced;
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

			solutionFound = false;
			maximizePlacing(nRows - 1);
		}

		private void maximizePlacing(int rowIndex) {
			if (rowIndex < 0 || solutionFound) {
				memorizeSolution();
				solutionFound = true;
				return;
			}
			
			int row = rowIndices[rowIndex];
			if (!rowOccupied[row]) {
				for (int col = n - 1; col >= 0; col--) {
					if (!colOccupied[col]) {
						place(row, col, true);
						maximizePlacing(rowIndex - 1);
						place(row, col, false);
					}
				}
			}
		}
	}

	private static class BishopBlack extends Rook {
		public BishopBlack() {
		}
		
		public BishopBlack(int N) {
			n = N;
			grid = new boolean[N][N];
			solutionGrid = new boolean[N][N];
			rowPreoccupied = new boolean[N - 1];
			rowOccupied = new boolean[N - 1];
			colOccupied = new boolean[N];
		}

		public boolean isPreoccupied(int row, int col) {
			return rowPreoccupied[(row + col) / 2];
		}
		
		public void preplace(int row, int col) {
			place(row, col, true);
			rowPreoccupied[(row + col) / 2] = true;
		}

		public void place(int row, int col, boolean isPlaced) {
			grid[row][col] = isPlaced;
			rowOccupied[(row + col) / 2] = isPlaced;
			colOccupied[(n - 1 - (row - col)) / 2] = isPlaced;
		}

		public void maximizePlacing() {
			int i, m = n - 1;
			int nVirtualRows = 0;
			
			for (i = 0; i < m; i++) {
				if (!rowPreoccupied[i]) {
					nVirtualRows++;
				}
			}
			
			rowIndices = new int[nVirtualRows];
			nVirtualRows = 0;
			for (i = 0; i < m; i++) {
				if (!rowPreoccupied[i]) {
					rowIndices[nVirtualRows++] = i;
				}
			}

			solutionFound = false;
			maximizePlacing(nVirtualRows - 1);
		}

		private void maximizePlacing(int virtualRowIndex) {
			if (virtualRowIndex < 0 || solutionFound) {
				memorizeSolution();
				solutionFound = true;
				return;
			}
			
			int vRow = rowIndices[virtualRowIndex];
			if (!rowOccupied[vRow]) {
				int minVCol = Math.abs(n / 2 - vRow * 2) / 2;
				for (int vCol = n / 2 * 2 - minVCol - 1; vCol >= minVCol; vCol--) {
					if (!colOccupied[vCol]) {
						int col = vCol - minVCol + Math.max(0, 2 * (vRow + 1) - n);
						int row = vRow - col;
						place(row, col, true);
						maximizePlacing(virtualRowIndex - 1);
						place(row, col, false);
					}
				}
			}
		}
	}

	private static class BishopWhite extends BishopBlack {
		public BishopWhite(int N) {
			n = N;
			grid = new boolean[N][N];
			solutionGrid = new boolean[N][N];
			rowPreoccupied = new boolean[N];
			rowOccupied = new boolean[N];
			colOccupied = new boolean[N - 1];
		}

		public void maximizePlacing() {
			int i;
			int nVirtualRows = 0;
			
			for (i = 0; i < n; i++) {
				if (!rowPreoccupied[i]) {
					nVirtualRows++;
				}
			}
			
			rowIndices = new int[nVirtualRows];
			nVirtualRows = 0;
			for (i = 0; i < n; i++) {
				if (!rowPreoccupied[i]) {
					rowIndices[nVirtualRows++] = i;
				}
			}

			solutionFound = false;
			maximizePlacing(nVirtualRows - 1);
		}

		private void maximizePlacing(int virtualRowIndex) {
			if (virtualRowIndex < 0 || solutionFound) {
				memorizeSolution();
				solutionFound = true;
				return;
			}
			//xxxxx
			int vRow = rowIndices[virtualRowIndex];
			if (!rowOccupied[vRow]) {
				int minVCol = Math.abs(n / 2 - vRow * 2) / 2;
				for (int vCol = n / 2 * 2 - minVCol - 1; vCol >= minVCol; vCol--) {
					if (!colOccupied[vCol]) {
						int col = vCol - minVCol + Math.max(0, 2 * (vRow + 1) - n);
						int row = vRow - col;
						place(row, col, true);
						maximizePlacing(virtualRowIndex - 1);
						place(row, col, false);
					}
				}
			}
		}
	}
}