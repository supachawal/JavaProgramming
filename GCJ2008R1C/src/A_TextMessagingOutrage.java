/* Problem A. Text Messaging Outrage (misunderstanding)

The story

Professor Loony, a dear friend of mine, stormed into my office. His face was red and he looked very angry. The first thing that came out of his mouth was "Damn those phone manufacturers. I was trying to send a text message, and it took me more than ten minutes to type a one-line message." I tried to calm him down. "But what is wrong? Why did it take you so long?" He continued, "Don't you see?! Their placement of the letters is so messed up? Why is 's' the 4th letter on its key? and 'e'? Why is it not the first letter on its key? I have to press '7' FOUR times to type an 's'? This is lunacy!" 
"Calm down, my friend," I said, "This scheme has been in use for so long, even before text messaging was invented. They had to keep it that way." 
"That's not an excuse," his face growing redder and redder. "It is time to change all this. It was a stupid idea to start with. And while we are at it, how come they only put letters on 8 keys? Why not use all 12? And why do they have to be consecutive?" 
"Umm... I... don't... know," I replied. 
"Ok, that's it. Those people are clearly incompetent. I am sure someone can come up with a better scheme." 
He was one of those people, I could see. People who complain about the problem, but never actually try to solve it. 
In this problem, you are required to come up with the best letter placement of keys to minimize the number of key presses required to type a message. You will be given the number of keys, the maximum number of letters we can put on every key, the total number of letters in the alphabet, and the frequency of every letter in the message. Letters can be placed anywhere on the keys and in any order. Each letter can only appear on one key. Also, the alphabet can have more than 26 letters (it is not English). 
For reference, the current phone keypad looks like this

key 2: abc
key 3: def
key 4: ghi
key 5: jkl
key 6: mno
key 7: pqrs
key 8: tuv
key 9: wxyz

The first press of a key types the first letter. Each subsequent press advances to the next letter. 
For example, to type the word "snow", you need to press "7" four times, followed by "6" twice, followed by "6" three times, 
followed by "9" once. The total number of key presses is 10. 

Input

The first line in the input file contains the number of test cases N. This is followed by N cases. 
Each case consists of two lines. On the first line we have the maximum number of letters to place on a key (P), 
the number of keys available (K) and the number of letters in our alphabet (L) all separated by single spaces. 
The second line has L non-negative integers. Each number represents the frequency of a certain letter. 
The first number is how many times the first letter is used, the second number is how many times the second letter is used, and so on. 

Output

For each case, you should output the following 
Case #x: [minimum number of keypad presses]

indicating the number of keypad presses to type the message for the optimal layout.

Limits

P * K ≥ L 
0 ≤ The frequency of each letter ≤ 1,000,000 

Small dataset

1 ≤ N ≤ 10
1 ≤ P ≤ 10
1 ≤ K ≤ 12
1 ≤ L ≤ 100

Large dataset

1 ≤ N ≤ 100
1 ≤ P ≤ 1 000
1 ≤ K ≤ 1 000
1 ≤ L ≤ 1 000

Sample

Input 
   
2
3 2 6
8 2 5 2 4 9
3 9 26
1 1 1 100 100 1 1 1 1 1 1 1 1 1 1 1 1 10 11 11 11 11 1 1 1 100

Output 
   
 Case #1: 47
 Case #2: 397

*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class A_TextMessagingOutrage {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
			inputFileName = "-sample1.in";
//			inputFileName = "-small-practice.in";
//			inputFileName = "-large-practice.in";
			inputFileName = A_TextMessagingOutrage.class.getSimpleName().substring(0, 1) + inputFileName;
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
			A_TextMessagingOutrage solver = new A_TextMessagingOutrage();
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
		int P; //the maximum number of letters to place on a key
		int K; //the number of keys available
		int L; //the number of letters in our alphabet
		int [] F;

		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				splitted = textLine.split("\\s+");
				
				P = Integer.valueOf(splitted[0]);
				K = Integer.valueOf(splitted[1]);
				L = Integer.valueOf(splitted[2]);
				
				System.out.printf("Case #%d: P=%d, K=%d, L=%d, ", testCaseNumber, P, K, L);
				F = new int[L];
				textLine = br.readLine();
				splitted = textLine.split("\\s+");
				for (int i = 0; i < L; i++) {
					F[i] = Integer.valueOf(splitted[i]);
				}
				
				if (testCaseNumber == 83) {
					System.out.printf("");
				}
				iterationCounter = 0;
				
				answer = numberOfKeypresses(P, L, F, K - 1, 0, 0);
//				System.out.printf("answer=%d, iterations=%d\n", answer, iterationCounter);
				System.out.printf("answer=%d, iterations=%d\n", answer, iterationCounter);

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
	
	private final int INFINITY = Integer.MAX_VALUE - 1000000; 
	
	private int numberOfKeypresses(int maxLettersOnAKey, int letterCount, int [] letterUsefreqs, int currentKey, int currentLetterOnKey, int currentLetter) {
//		int spare = maxLettersOnAKey * numOfKeys - numOfLetters;
		
		if (currentLetter >= letterCount)
			return 0;
		if (currentLetterOnKey >= maxLettersOnAKey) {
			currentLetterOnKey = 0;
			currentKey--;
		}
		if (currentKey < 0)
			return INFINITY;

		iterationCounter++;
		currentLetterOnKey++;
		return Math.min(
					letterUsefreqs[currentLetter] * currentLetterOnKey 
						+ numberOfKeypresses(maxLettersOnAKey, letterCount, letterUsefreqs, currentKey, currentLetterOnKey, currentLetter + 1)
					, numberOfKeypresses(maxLettersOnAKey, letterCount, letterUsefreqs, currentKey - 1, 0, currentLetter)
			        );
		
	}
}