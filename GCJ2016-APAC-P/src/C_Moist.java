/* Problem C. Moist (Sorter Cost) 

Problem

Moist has a hobby -- collecting figure skating trading cards. His card collection has been growing, and it is now too large to keep in one disorganized pile. Moist needs to sort the cards in alphabetical order, so that he can find the cards that he wants on short notice whenever it is necessary. 

The problem is -- Moist can't actually pick up the cards because they keep sliding out his hands, and the sweat causes permanent damage. Some of the cards are rather expensive, mind you. To facilitate the sorting, Moist has convinced Dr. Horrible to build him a sorting robot. However, in his rather horrible style, Dr. Horrible has decided to make the sorting robot charge Moist a fee of $1 whenever it has to move a trading card during the sorting process. 

Moist has figured out that the robot's sorting mechanism is very primitive. It scans the deck of cards from top to bottom. Whenever it finds a card that is lexicographically smaller than the previous card, it moves that card to its correct place in the stack above. This operation costs $1, and the robot resumes scanning down towards the bottom of the deck, moving cards one by one until the entire deck is sorted in lexicographical order from top to bottom. 

As wet luck would have it, Moist is almost broke, but keeping his trading cards in order is the only remaining joy in his miserable life. He needs to know how much it would cost him to use the robot to sort his deck of cards. 

Input

The first line of the input gives the number of test cases, T. T test cases follow. Each one starts with a line containing a single integer, N. The next N lines each contain the name of a figure skater, in order from the top of the deck to the bottom. 

Output

For each test case, output one line containing "Case #x: y", where x is the case number (starting from 1) and y is the number of dollars it would cost Moist to use the robot to sort his deck of trading cards. 

Limits

1 ≤ T ≤ 100.
 Each name will consist of only letters and the space character.
 Each name will contain at most 100 characters.
 No name with start or end with a space.
 No name will appear more than once in the same test case.
 Lexicographically, the space character comes first, then come the upper case letters, then the lower case letters. 

Small dataset

1 ≤ N ≤ 10. 

Large dataset

1 ≤ N ≤ 100. 

Sample

Input 
2
2
Oksana Baiul
Michelle Kwan
3
Elvis Stojko
Evgeni Plushenko
Kristi Yamaguchi
   
Output 
Case #1: 1
Case #2: 0
 

*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class C_Moist {
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
			inputFileName = "-small-practice-2.in";
//			inputFileName = "-large-practice.in";
			inputFileName = C_Moist.class.getSimpleName().substring(0, 1) + inputFileName;
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
			C_Moist solver = new C_Moist();
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
		@SuppressWarnings("unused")
		String [] splitted;
		int answer;
		String [] A;
		int n;
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				if (testCaseNumber == 168) {
					System.out.printf("");
				}
				n = Integer.parseInt(textLine);
				A = new String[n];
				for (int i = 0; i < n; i++) {
					A[i] = br.readLine();
				}

				System.out.printf("Case #%d: N=%d, ", testCaseNumber, n);
				iterationCounter = 0;
				answer = sortingRobotCost(A);
				
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
	
	private static int sortingRobotCost(String [] a) {
		int result = 0;
		String maxSoFar = "";
		
		for (String s : a) {
			int compareResult = s.compareTo(maxSoFar); 
			if (compareResult < 0) {
				result++;
			} else if (compareResult > 0) {
				maxSoFar = s;
			}
		}
		return result;
	}
}