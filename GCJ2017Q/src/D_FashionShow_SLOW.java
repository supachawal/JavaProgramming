import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class D_FashionShow_SLOW {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
			inputFileName = "-small-attempt0.in";
//			inputFileName = "-small-1-attempt0.in";
//			inputFileName = "-small-2-attempt0.in";
//			inputFileName = "-large.in";
			inputFileName = D_FashionShow_SLOW.class.getSimpleName().substring(0, 1) + inputFileName;
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
			D_FashionShow_SLOW solver = new D_FashionShow_SLOW();
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
			FashionGrid fg = new FashionGrid(N);

			for (int m = 0; m < M; m++) {
				textLine = br.readLine();
				splitted = textLine.split("\\s+");
				char model = splitted[0].charAt(0);
				int row = Integer.parseInt(splitted[1]) - 1;
				int col = Integer.parseInt(splitted[2]) - 1;
				fg.placeModel(row, col, model, true);
			}
			ArrayList<GridPos> posList = new ArrayList<GridPos>();
			fg.genStokes(posList);
			
			int maxValue = fg.maximizeStylePoints(posList, posList.size() - 1);
			fg.nTargetStylePoints = maxValue + fg.nStylePoints;
			fg.maximizeStylePoints(posList, posList.size() - 1);

			StringBuilder answer = new StringBuilder();
			int nStrokes = fg.countStroke(answer);
			System.out.printf(" %d %d\n%s", fg.nStylePoints, nStrokes, answer);
			w.printf(" %d %d\n%s", fg.nStylePoints, nStrokes, answer);
		}

		br.close();
	}

	private static class FashionGrid {
		public char[][] grid;
		public boolean[][] predefined;
		public int nStylePoints;
		public int nTargetStylePoints;
		
		public boolean[] rowUsed;
		public boolean[] colUsed;
		public boolean[] udiagUsed;
		public boolean[] ddiagUsed;
		public boolean[] rowUpgraded;
		public boolean[] colUpgraded;
		public boolean[] udiagUpgraded;
		public boolean[] ddiagUpgraded;
		
		public FashionGrid(int N) {
			grid = new char[N][N];
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					grid[i][j] = '.';
				}
			}
			predefined = new boolean[N][N];
			nStylePoints = 0;
			nTargetStylePoints = Integer.MAX_VALUE;
			rowUsed = new boolean[N];
			colUsed = new boolean[N];
			udiagUsed = new boolean[2 * N - 1];
			ddiagUsed = new boolean[2 * N - 1];

			rowUpgraded = new boolean[N];
			colUpgraded = new boolean[N];
			udiagUpgraded = new boolean[2 * N - 1];
			ddiagUpgraded = new boolean[2 * N - 1];
		}

		public void placeModel(int row, int col, char model, boolean predefined) {
			int n = grid.length;
			if (model == '+') {
				udiagUsed[row + col] = true;
				ddiagUsed[n - 1 - (row - col)] = true;
				nStylePoints++;
			} else if (model == 'x') {
				rowUsed[row] = true;
				colUsed[col] = true;
				nStylePoints++;
			} else if (model == 'o') {
				rowUsed[row] = true;
				colUsed[col] = true;
				udiagUsed[row + col] = true;
				ddiagUsed[n - 1 - (row - col)] = true;

				rowUpgraded[row] = true;
				colUpgraded[col] = true;
				udiagUpgraded[row + col] = true;
				ddiagUpgraded[n - 1 - (row - col)] = true;

				if (grid[row][col] == '+' || grid[row][col] == 'x') {
					nStylePoints++;
				} else {
					nStylePoints += 2;
				}
			}
			
			grid[row][col] = model;

			this.predefined[row][col] = predefined;
		} 

		public void unplaceModel(int row, int col) {
			char model = grid[row][col];
			if (model != '.') {
				int n = grid.length;
				if (model == '+') {
					udiagUsed[row + col] = false;
					ddiagUsed[n - 1 - (row - col)] = false;
					nStylePoints--;
				} else if (model == 'x') {
					rowUsed[row] = false;
					colUsed[col] = false;
					nStylePoints--;
				} else if (model == 'o') {
					udiagUsed[row + col] = false;
					ddiagUsed[n - 1 - (row - col)] = false;
					rowUsed[row] = false;
					colUsed[col] = false;

					udiagUpgraded[row + col] = false;
					ddiagUpgraded[n - 1 - (row - col)] = false;
					rowUpgraded[row] = false;
					colUpgraded[col] = false;

					nStylePoints -= 2;
				}
				
				grid[row][col] = '.';
			}
		}
		
		public int countStroke(StringBuilder sbOutput) {
			int ret = 0;
			int n = grid.length;
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (!predefined[i][j] && grid[i][j] != '.') {
						sbOutput.append(grid[i][j]).append(' ').append(i + 1).append(' ').append(j + 1).append('\n');
						ret++;
					}
				}
			}
			return ret;
		}

		public void genStokes(ArrayList<GridPos> strokes) {
			int n = grid.length;
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					strokes.add(new GridPos(i, j));
				}
			}
		}
		
		public int maximizeStylePoints(ArrayList<GridPos> posList, int k) {
			if (k < 0) {
				return 0;
			}

			int i = posList.get(k).row;
			int j = posList.get(k).col;

			int n = grid.length;
			char oldModel = grid[i][j];
			int ret = maximizeStylePoints(posList, k - 1);
			if (nStylePoints >= nTargetStylePoints) {
				return ret;
			}
			
			if (((oldModel == '+' && !rowUsed[i] && !colUsed[j] || oldModel == 'x' && !udiagUsed[i + j] && !ddiagUsed[n - 1 - (i - j)]) && predefined[i][j] && !rowUpgraded[i] && !colUpgraded[j] && !udiagUpgraded[i + j] && !ddiagUpgraded[n - 1 - (i - j)])
					|| !rowUsed[i] && !colUsed[j] && !udiagUsed[i + j] && !ddiagUsed[n - 1 - (i - j)]) {
				placeModel(i, j, 'o', false);

				if (oldModel == '+' || oldModel == 'x') {
					ret = Math.max(ret, 1 + maximizeStylePoints(posList, k - 1));
					if (nStylePoints >= nTargetStylePoints) {
						return ret;
					}
					unplaceModel(i, j);
					placeModel(i, j, oldModel, true);
				} else {
					ret = Math.max(ret, 2 + maximizeStylePoints(posList, k - 1));
					if (nStylePoints >= nTargetStylePoints) {
						return ret;
					}
					unplaceModel(i, j);
				}
			}
				
			if (oldModel == '.') {
				if (!rowUsed[i] && !colUsed[j]) {
					placeModel(i, j, 'x', false);
					ret = Math.max(ret, 1 + maximizeStylePoints(posList, k - 1));
					if (nStylePoints >= nTargetStylePoints) {
						return ret;
					}
					unplaceModel(i, j);
				}
				
				if (!udiagUsed[i + j] && !ddiagUsed[n - 1 - (i - j)]) {
					placeModel(i, j, '+', false);
					ret = Math.max(ret, 1 + maximizeStylePoints(posList, k - 1));
					if (nStylePoints >= nTargetStylePoints) {
						return ret;
					}
					unplaceModel(i, j);
				}
			}
			
			return ret;
		}

	}
	
	private static class GridPos {
		int row, col;
		public GridPos(int row, int col) {
			this.row = row;
			this.col = col;
		}
	}
	
}