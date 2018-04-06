/* Problem A. Magic Trick

Recently you went to a magic show. You were very impressed by one of the tricks, so you decided to try to figure out 
the secret behind it! 

The magician starts by arranging 16 cards in a square grid: 4 rows of cards, with 4 cards in each row. Each card has a 
different number from 1 to 16 written on the side that is showing. Next, the magician asks a volunteer to choose a card, 
and to tell him which row that card is in. 

Finally, the magician arranges the 16 cards in a square grid again, possibly in a different order. Once again, he asks 
the volunteer which row her card is in. With only the answers to these two questions, the magician then correctly determines 
which card the volunteer chose. Amazing, right?
 
You decide to write a program to help you understand the magician's technique. The program will be given the two arrangements 
of the cards, and the volunteer's answers to the two questions: the row number of the selected card in the first arrangement, 
and the row number of the selected card in the second arrangement. The rows are numbered 1 to 4 from top to bottom.
 
Your program should determine which card the volunteer chose; or if there is more than one card the volunteer might have 
chosen (the magician did a bad job); or if there's no card consistent with the volunteer's answers (the volunteer cheated). 

Solving this problem
Usually, Google Code Jam problems have 1 Small input and 1 Large input. This problem has only 1 Small input. Once you have 
solved the Small input, you have finished solving this problem. 

Input
The first line of the input gives the number of test cases, T. T test cases follow. Each test case starts with a line containing 
an integer: the answer to the first question. The next 4 lines represent the first arrangement of the cards: each contains 
4 integers, separated by a single space. The next line contains the answer to the second question, and the following 
four lines contain the second arrangement in the same format. 

Output
For each test case, output one line containing "Case #x: y", where x is the test case number (starting from 1). 

If there is a single card the volunteer could have chosen, y should be the number on the card. If there are multiple cards the volunteer could have chosen, y should be "Bad magician!", without the quotes. If there are no cards consistent with the volunteer's answers, y should be "Volunteer cheated!", without the quotes. The text needs to be exactly right, so consider copying/pasting it from here. 

Limits

1 ≤ T ≤ 100.
 1 ≤ both answers ≤ 4.
 Each number from 1 to 16 will appear exactly once in each arrangement. 

Sample

Input 
3
2
1 2 3 4
5 6 7 8
9 10 11 12
13 14 15 16
3
1 2 5 4
3 11 6 15
9 10 7 12
13 14 8 16
2
1 2 3 4
5 6 7 8
9 10 11 12
13 14 15 16
2
1 2 3 4
5 6 7 8
9 10 11 12
13 14 15 16
2
1 2 3 4
5 6 7 8
9 10 11 12
13 14 15 16
3
1 2 3 4
5 6 7 8
9 10 11 12
13 14 15 16

   
Output 
Case #1: 7
Case #2: Bad magician!
Case #3: Volunteer cheated!


*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;

public class A_MagicTrick {
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
			inputFileName = A_MagicTrick.class.getSimpleName().substring(0, 1) + inputFileName;
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
			A_MagicTrick solver = new A_MagicTrick();
			solver.solve(inputFile, outputWriter);
			outputWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		CommonUtils.postamble();
	}

	@SuppressWarnings("unused")
	private long iterationCounter = 0;
	
	public boolean solve(final File aFile, PrintWriter w) {
		String textLine;
		boolean result = false;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		String [] splitted;
		String answer;
		int R;
		int i;
		HashSet<Integer> set1 = new HashSet<Integer>();
		HashSet<Integer> set2 = new HashSet<Integer>();

		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				R = Integer.parseInt(textLine) - 1;
				set1.clear();
				for (i = 0; i < 4; i++) {
					textLine = br.readLine();
					if (i == R) {
						splitted = textLine.split("\\s+");
						for (String s : splitted) {
							set1.add(Integer.valueOf(s));
						}
					}
				}
				
				textLine = br.readLine();
				R = Integer.parseInt(textLine) - 1;
				set2.clear();
				for (i = 0; i < 4; i++) {
					textLine = br.readLine();
					if (i == R) {
						splitted = textLine.split("\\s+");
						for (String s : splitted) {
							set2.add(Integer.valueOf(s));
						}
					}
				}
				
//				if (testCaseNumber == 101) {
//				System.out.printf("");
//			}
//			iterationCounter = 0;

				set2.retainAll(set1);
				int intersectionSize = set2.size();
				if (intersectionSize == 1) {
					answer = set2.iterator().next().toString();
				} else if (intersectionSize == 0) {
					answer = "Volunteer cheated!";
				} else {
					answer = "Bad magician!";
				}
				
				System.out.printf("Case #%d: answer=%s\n", testCaseNumber, answer);

				w.printf("Case #%d: %s\n", testCaseNumber, answer);
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