/* Problem D. gSnake

Note: This Google Doodle does not exactly match the rules of the Snake game we will describe below. It is only intended to give you a general idea of what the game looks like. 

Alex just learned how to program and wants to develop his own version of Snake, with the following rules: 
• The game board has R rows and C columns. The top left cell of the board has coordinates (1, 1), and the bottom right 
  cell has coordinates (R, C). 
• At the start of the game, in every cell with coordinates (r, c) such that r + c is odd, there is one piece of food. 
  No other cells have food. 
• The snake's body is always an ordered, connected sequence of one or more cells on the board. The first cell of the 
  sequence is called the "head" of the snake. The second cell (if any) shares an edge (not just a corner) with the first 
  cell, and so on. The last cell in the sequence is called the "tail" of the snake. 
• The snake's head is always facing in one of four directions: left, up, right, or down. 
• At the start of the game, the snake is at cell (1, 1) and has a length of one (that is, the snake consists of only a 
  head), and the head faces right. 
• At each integer time (1 second, 2 seconds, etc.), the head of the snake will move into the adjacent cell that its 
  head is facing toward. The board is cyclic, i.e., trying to move off an edge will cause the head to appear on the 
  opposite edge of the board. For example, if the snake is at (1, C) and its head is facing right, the head will next 
  move to (1, 1). If the snake is at (1, C) and its head is facing up, the head will next move to (R, C). 
• When the snake's head moves into a cell with no food, the snake does not grow. The snake's second cell (if any) moves 
  to the place where the snake's head was, the snake's third cell (if any) moves to the place where the second cell was, 
  and so on. 
• When the snake's head moves into a cell with a piece of food, it eats the food (meaning that cell no longer has food), 
  and grows its body. A new head is created in the cell where the food was. The cell that was the snake's head becomes 
  the snake's second cell, the cell that was the snake's second cell (if any) becomes the snake's third cell, and so on. 
• If, after a move is complete, the snake's head is in the same place as one of another of its cells, the snake dies 
  and the game ends immediately. (Note that if the snake's head moves toward a cell where its tail was, the game will 
  not end, because the tail will move out of the way before the move is complete.) 
• In the game, the player can let the snake perform some turn actions. Each action Ai will happen between the Tith and 
  Ti+1 th seconds. There are two possible actions: "L" and "R". An "L" action will turn the head 90 degrees to the left, 
  so, for example, if the snake had been facing down before, it would face right after. An "R" action will turn the head 
  90 degrees to the right, so, for example, if the snake had been facing down before, it would face left after. 
• The game has a time limit: it will end after the move on the 109th second is complete (if the game has even gone on 
  that long!) 

To test the game, Alex has written a series of TURN actions. Your task is to simulate that series of actions, and tell 
Alex the final length of the snake when the game is over. Remember that the game can end either because the snake's head 
and another cell of its body are in the same place after a move is complete, or because time runs out. In the former 
case, you should count both the head and the overlapping cell of its body as two separate cells, for the purpose of 
determining length. 

Input
The first line of the input gives the number of test cases, T. T test cases follow. Each test cases starts with three 
integers S, R, and C, where S gives the number of turn actions and R and C represent the number of rows and columns of 
the board. S lines follow; the ith of these lines has an integer Xi, then a character Ai that is either L or R. Each of 
these lines corresponds to performing an action between Xith and Xi+1 th seconds. It's guaranteed that the actions are 
given in time order and there will never be more than one action between the same two seconds. However, you should note 
that the game may end before the snake gets to execute all of these actions. 

Output
For each test case, output one line containing "Case #x: y", where x is the test case number (starting from 1) and y is 
the length of the snake when the game is over. 

Limits
1 ≤ T ≤ 10.

Small dataset
1 ≤ R, C ≤ 100;
1 ≤ S ≤ 100;
1 ≤ Xi ≤ 2000.


Large dataset
1 ≤ R, C ≤ 100000;
1 ≤ S ≤ 100000;
1 ≤ Xi ≤ 1000000.


Sample

Input 
2
3 3 3
1 R
2 L
3 R
5 3 3
2 R
4 R
6 R
7 R
8 R
   
Output 
Case #1: 3
Case #2: 5
 
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.HashSet;

public class D_gSnake {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
//			inputFileName = "-small-practice.in";
//			inputFileName = "-small-practice-1.in";
//			inputFileName = "-small-practice-2.in";
			inputFileName = "-large-practice.in";
			inputFileName = D_gSnake.class.getSimpleName().substring(0, 1) + inputFileName;
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
			D_gSnake solver = new D_gSnake();
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
		int answer;
		int turnActionCount, rowCount, columnCount;
		ArrayDeque<IntPair> turnActions = new ArrayDeque<IntPair>(100000);
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				if (testCaseNumber == 2) {
					System.out.printf("");
				}
				splitted = textLine.split("\\s+");
				turnActionCount = Integer.parseInt(splitted[0]);
				rowCount = Integer.parseInt(splitted[1]);
				columnCount = Integer.parseInt(splitted[2]);
				turnActions.clear();
				for (int i = 0; i < turnActionCount; i++) {
					textLine = br.readLine();
					splitted = textLine.split("\\s+");
					turnActions.add(new IntPair(Integer.parseInt(splitted[0]), splitted[1].charAt(0) == 'L' ? -1 : 1));
				}

				System.out.printf("Case #%d: #turns=%d, #rows=%d, #columns=%d, ", testCaseNumber, turnActionCount, rowCount, columnCount);
				iterationCounter = 0;

				answer = snakeFinalLength(turnActions, rowCount, columnCount);
				
				System.out.printf("answer=%d (iterations=%d)\n", answer, iterationCounter);

				w.printf("Case #%d: %d\n", testCaseNumber, answer);
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

	static int [][] directionMap = {{0, 1}/*right*/, {1, 0}/*down*/, {0, -1}/*left*/, {-1, 0}/*up*/};
	
	private int snakeFinalLength(ArrayDeque<IntPair> turnActions, int m, int n) {
		int maxDimen = Math.max(m, n);
		ArrayDeque<IntPair> snake = new ArrayDeque<IntPair>(2 * maxDimen);
		HashSet<IntPair> snakeInnerBody = new HashSet<IntPair>(2 * maxDimen);
		int directionIndex = 0 /*right*/;
		int i = 0, j = 0;
		int timeCounter = 0;
		snake.add(new IntPair(0, 0));
		int idleCounter = 0;
		int idleCounterLimit = maxDimen;
		HashSet<IntPair> visitedCells = new HashSet<IntPair>(4 * maxDimen);
		
		while (idleCounter++ < idleCounterLimit) {
			iterationCounter++;

			if (turnActions.size() > 0) {
				if (timeCounter == turnActions.peekFirst().v1) {
					directionIndex = CommonUtils.positiveMod(directionIndex + turnActions.pollFirst().v2, 4);
				}
				idleCounter = 0;
			}
			
			int [] direction = directionMap[directionIndex];
			i = CommonUtils.positiveMod(i + direction[0], m);
			j = CommonUtils.positiveMod(j + direction[1], n);
			
			IntPair nextHeadPos = new IntPair(i, j);

			// check if the head collides its body
			if (snakeInnerBody.contains(nextHeadPos)) {
				break;
			}
			
			// move the snake
			if (((i + j) & 1) == 0 || !visitedCells.add(nextHeadPos)) {
				snakeInnerBody.remove(snake.removeLast());
			}
			snakeInnerBody.add(snake.getFirst());
			snakeInnerBody.remove(snake.getLast());
			snake.addFirst(nextHeadPos);
				
			timeCounter++;
		}
		return snake.size();
	}
}