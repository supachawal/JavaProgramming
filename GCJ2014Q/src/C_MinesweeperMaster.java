/* Problem C. Minesweeper Master (difficult)
Minesweeper is a computer game that became popular in the 1980s, and is still included in some versions of the Microsoft 
Windows operating system. This problem has a similar idea, but it does not assume you have played Minesweeper. 

In this problem, you are playing a game on a grid of identical cells. The content of each cell is initially hidden. 
There are M mines hidden in M different cells of the grid. No other cells contain mines. You may click on any cell to reveal it. 
If the revealed cell contains a mine, then the game is over, and you lose. Otherwise, the revealed cell will contain 
a digit between 0 and 8, inclusive, which corresponds to the number of neighboring cells that contain mines. 
Two cells are neighbors if they share a corner or an edge. Additionally, if the revealed cell contains a 0, then all of 
the neighbors of the revealed cell are automatically revealed as well, recursively. When all the cells that don't 
contain mines have been revealed, the game ends, and you win. 

For example, an initial configuration of the board may look like this ('*' denotes a mine, and 'c' is the first clicked cell): 
*..*...**.
....*.....
..c..*....
........*.
..........

There are no mines adjacent to the clicked cell, so when it is revealed, it becomes a 0, and its 8 adjacent cells are revealed as well. This process continues, resulting in the following board: *..*...**.
1112*.....
00012*....
00001111*.
00000001..

At this point, there are still un-revealed cells that do not contain mines (denoted by '.' characters), so the player has to click again in order to continue the game. 

You want to win the game as quickly as possible. There is nothing quicker than winning in one click. 
Given the size of the board (R x C) and the number of hidden mines M, is it possible (however unlikely) to win in one click? 
You may choose where you click. If it is possible, then print any valid mine configuration and the coordinates of your click, 
following the specifications in the Output section. Otherwise, print "Impossible". 

Input
The first line of the input gives the number of test cases, T. T lines follow. 
Each line contains three space-separated integers: R, C, and M. 

Output
For each test case, output a line containing "Case #x:", where x is the test case number (starting from 1). 
On the following R lines, output the board configuration with C characters per line, using '.' to represent an empty cell, 
'*' to represent a cell that contains a mine, and 'c' to represent the clicked cell. 

If there is no possible configuration, then instead of the grid, output a line with "Impossible" instead. 
If there are multiple possible configurations, output any one of them. 

Limits
0 ≤ M < R * C.

Small dataset
1 ≤ T ≤ 230.
1 ≤ R, C ≤ 5.

Large dataset
1 ≤ T ≤ 140.
1 ≤ R, C ≤ 50.

Sample
Input 
5
5 5 23
3 1 1
2 2 1
4 7 3
10 10 82
   
Output 
Case #1:
Impossible
Case #2:
c
.
*
Case #3:
Impossible
Case #4:
......*
.c....*
.......
..*....
Case #5:
**********
**********
**********
****....**
***.....**
***.c...**
***....***
**********
**********
**********

*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.TreeMap;

public class C_MinesweeperMaster {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
//			inputFileName = "-small-practice.in";
			inputFileName = "-large-practice.in";
			inputFileName = C_MinesweeperMaster.class.getSimpleName().substring(0, 1) + inputFileName;
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
			C_MinesweeperMaster solver = new C_MinesweeperMaster();
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
		String [] splitted;
		StringBuilder answer;
		char [] board;
		int R, C, M;

		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				splitted = textLine.split("\\s+");
				R = Integer.parseInt(splitted[0]);
				C = Integer.parseInt(splitted[1]);
				M = Integer.parseInt(splitted[2]);
				
				System.out.printf("Case #%d: R=%d, C=%d, M=%d, ", testCaseNumber, R, C, M);

				if (testCaseNumber == 189) {
					System.out.printf("");
				}
				iterationCounter = 0;
				board = findBoardConfigurationToWinInOneClick(R, C, M);
				
				answer = new StringBuilder();
				if (board == null) {
					answer.append("\nImpossible");
				} else {
					for (int i = 0; i < R; i++) {
						answer.append('\n');
						for (int j = 0; j < C; j++) {
							answer.append(board[i * C + j]);
						}
					}
				}
				System.out.printf("answer=%s (iterations=%d)\n", answer, iterationCounter);

				w.printf("Case #%d:%s\n", testCaseNumber, answer);
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
	
	private char[] findBoardConfigurationToWinInOneClick(int R, int C, int M) {
		char[] board = new char[R * C];
		int nn = R * C;
		int mineCount = M;
		int i, j, k;
		
		// initialize the board
		board[0] = 'c';
		for (k = 1; k < nn; k++) {
			board[k] = '.';
		}
		
		// cut full row or full column
		int boundR = R;
		int boundC = C;
		boolean isProgress = true;
		while (mineCount > 0 && isProgress) {
			isProgress = false;
			if (boundR >= boundC && mineCount >= boundC) {
				i = boundR - 1;
				for (j = 0; j < boundC; j++) {
					iterationCounter++;
					board[i * C + j] = '*';
				}
				boundR--;
				mineCount -= boundC;
				isProgress = true;
			} else if (boundC >= boundR && mineCount >= boundR) {
				j = boundC - 1;
				for (i = 0; i < boundR; i++) {
					iterationCounter++;
					board[i * C + j] = '*';
				}
				boundC--;
				mineCount -= boundR;
				isProgress = true;
			}
		}
		
		// fill partial row/column with preserved area
		for (i = boundR - 1; i > 1 && mineCount > 0; i--) {
			for (j = boundC - 1; j > 1 && mineCount > 0; j--) {
				iterationCounter++;
				board[i * C + j] = '*';
				mineCount--;
			}
		}
		
		if (mineCount > 0 || !canWinInOneClick(board, R, C)) {
			return null;
		}
		
		return board;
	}
	
	private static boolean canWinInOneClick(char[] board, int R, int C) {
		return !(board[0] == '*'
				 || C > 1 && R > 1 && board[C + 1] == '*' && (board[1] != '*' || board[C] != '*')
				);
	}

	@SuppressWarnings("unused")
	private static char[] findBoardConfigurationToWinInOneClick_BUG3(int R, int C, int M) {
		TreeMap<Long, IntPair> tree = new TreeMap<Long, IntPair>();
		char[] board = new char[R * C];
		int nn = R * C;
		int mineCount = M;
		int i, j, k;
		
		// initialize the board
		board[0] = 'c';
		for (k = 1; k < nn; k++) {
			board[k] = '.';
		}
		
		for (i = 0; i < R; i++) {
			for (j = 0; j < C; j++) {
				Long key = (long)i * i + j * j;
				while (tree.containsKey(key)) {
					key++;
				}
				tree.put(key, new IntPair(i, j));
			}
		}

		while (mineCount-- > 0) {
			IntPair coord = tree.pollLastEntry().getValue();
			board[coord.v1 * C + coord.v2] = '*';
		}

		if (C > 1 && board[1] == '*'
			|| R > 1 && board[C] == '*'
			|| C > 1 && R > 1 && board[C + 1] == '*'
			) {
			return null;
		}
		
		return board;
	}

	@SuppressWarnings("unused")
	private static char[] findBoardConfigurationToWinInOneClick_BUG2(int R, int C, int M) {
		char[] board = new char[R * C];
		int nn = R * C;
		int mineCount = M;
		int k;
		// initialize the board
		board[0] = 'c';
		for (k = 1; k < nn; k++) {
			board[k] = '.';
		}

		for (k = nn - 1; k > 1 && mineCount > 0; k--) {
			if (R == 1 || (k != C && (C == 1 || k != C + 1))) {
				board[k] = '*';
				mineCount--;
			}
		}
		
		if (mineCount > 0) {
			return null;
		}
		
		return board;
		/*
			Case #189: R=3, C=5, M=8, answer=
			c..**
			...**
			.****
		*/
	}

	@SuppressWarnings("unused")
	private static char[] findBoardConfigurationToWinInOneClick_BUG1(int R, int C, int M) {
		char[] board = new char[R * C];
		int nn = R * C;
		int i = 0, j = 0, k;
		int di = 0, dj = 1;
		int borderTop = 0, borderLeft = 0, borderBottom = R - 1, borderRight = C - 1;
		// initialize the board
		for (k = 0; k < nn; k++) {
			board[k] = '.';
		}
		
		for (k = 0; k < M;) {
			if (i >= 0 && i < R && j >= 0 && j < C) {
				if (board[i * C + j] != '*') {
					board[i * C + j] = '*';
					k++;
				}
			}
			
			if (k < M) {
				i += di;
				j += dj;
				if (j > borderRight) {
					di = 1;
					dj = 0;
					j = borderRight;
					borderTop++;
					i = borderTop;
				} else if (i > borderBottom) {
					di = 0;
					dj = -1;
					i = borderBottom;
					borderRight--;
					j = borderRight;
				} else if (j < borderLeft) {
					di = -1;
					dj = 0;
					j = borderLeft;
					borderBottom--;
					i = borderBottom;
				} else if (i < borderTop) {
					di = 0;
					dj = 1;
					i = borderTop;
					borderLeft++;
					j = borderLeft;
				}
			}
		}
		
		for (i = borderTop; i <= borderBottom; i++) {
			for (j = borderLeft; j <= borderRight; j++) {
				if (board[i * C + j] != '*') {
					boolean hasMineAsNeighbor = false;
					
					for (di = -1; di <= 1 && !hasMineAsNeighbor; di++) {
						for (dj = -1; dj <= 1; dj++) {
							int testI = i + di;
							int testJ = j + dj;
							if ((di != 0 || dj != 0) 
									&& testI >= 0 && testI < R
									&& testJ >= 0 && testJ < C
								) {
								if (board[testI * C + testJ] == '*') {
									hasMineAsNeighbor = true;
									break;
								}
							}
						}
					}
					
					if (!hasMineAsNeighbor) {
						board[i * C + j] = 'c';
						return board;
					}
				}
			}
		}
		
		return null;
	}
}