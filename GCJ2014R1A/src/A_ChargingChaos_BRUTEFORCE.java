/* Problem A. Charging Chaos
Shota the farmer has a problem. He has just moved into his newly built farmhouse, but it turns out that the outlets 
haven't been configured correctly for all of his devices. Being a modern farmer, Shota owns a large number of 
smartphones and laptops, and even owns a tablet for his favorite cow Wagyu to use. In total, he owns N different devices. 

As these devices have different specifications and are made by a variety of companies, they each require a different 
electric flow to charge. Similarly, each outlet in the house outputs a specific electric flow. An electric flow can be 
represented by a string of 0s and 1s of length L. 

Shota would like to be able to charge all N of his devices at the same time. Coincidentally, there are exactly N outlets 
in his new house. In order to configure the electric flow from the outlets, there is a master control panel with L switches. 
The ith switch flips the ith bit of the electric flow from each outlet in the house. For example, if the electric flow from the outlets is: 
Outlet 0: 10
Outlet 1: 01
Outlet 2: 11

Then flipping the second switch will reconfigure the electric flow to: 
Outlet 0: 11
Outlet 1: 00
Outlet 2: 10

If Shota has a smartphone that needs flow "11" to charge, a tablet that needs flow "10" to charge, and a laptop that 
needs flow "00" to charge, then flipping the second switch will make him very happy! 

Misaki has been hired by Shota to help him solve this problem. She has measured the electric flows from the outlets 
in the house, and noticed that they are all different. Decide if it is possible for Shota to charge all of his devices 
at the same time, and if it is possible, figure out the minimum number of switches that needs to be flipped, because 
the switches are big and heavy and Misaki doesn't want to flip more than what she needs to. 

Input

The first line of the input gives the number of test cases, T. T test cases follow. Each test case consists of three lines. 
The first line contains two space-separated integers N and L. The second line contains N space-separated strings of length L, 
representing the initial electric flow from the outlets. The third line also contains N space-separated strings of length L, 
representing the electric flow required by Shota's devices. 

Output

For each test case, output one line containing "Case #x: y", where x is the case number (starting from 1) and y is the 
minimum number of switches to be flipped in order for Shota to charge all his devices. If it is impossible, y should be 
the string "NOT POSSIBLE" (without the quotes). Please note that our judge is not case-sensitive, but it is strict in 
other ways: so although "not  possible" will be judged correct, any misspelling will be judged wrong. We suggest 
copying/pasting the string NOT POSSIBLE into your code. 

Limits
1 ≤ T ≤ 100.
No two outlets will be producing the same electric flow, initially.
No two devices will require the same electric flow.

Small dataset
1 ≤ N ≤ 10.
2 ≤ L ≤ 10. 

Large dataset
1 ≤ N ≤ 150.
10 ≤ L ≤ 40. 

Sample

Input 
3
3 2
01 11 10
11 00 10
2 3
101 111
010 001
2 2
01 10
10 01
   
Output 
Case #1: 1
Case #2: NOT POSSIBLE
Case #3: 0

Explanation

In the first example case, Misaki can flip the second switch once. The electric flow from the outlets becomes: 
Outlet 0: 00
Outlet 1: 10
Outlet 2: 11

Then Shota can use the outlet 0 to charge device 1, the outlet 1 to charge device 2, outlet 2 to charge device 0. 
This is also a solution that requires the minimum amount number of switches to be flipped.
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;

public class A_ChargingChaos_BRUTEFORCE {
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
			inputFileName = A_ChargingChaos_BRUTEFORCE.class.getSimpleName().substring(0, 1) + inputFileName;
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
			A_ChargingChaos_BRUTEFORCE solver = new A_ChargingChaos_BRUTEFORCE();
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
	private long[] outlets;
	private HashSet<Long> devices;
	private int bitCount;
	
	public boolean solve(final File aFile, PrintWriter w) {
		String textLine;
		boolean result = false;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		String [] splitted;
		String answer = "";
		int i, n;
		int initialAnswer;
		
//		iterateBits(0, 3, 1, 0);

		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				splitted = textLine.split("\\s+");
				n = Integer.valueOf(splitted[0]);
				this.bitCount = Integer.valueOf(splitted[1]);

				textLine = br.readLine();
				splitted = textLine.split("\\s+");
				this.outlets = new long[n];
				for (i = 0; i < n; i++) {
					this.outlets[i] = Long.valueOf(splitted[i], 2);
				}

				textLine = br.readLine();
				splitted = textLine.split("\\s+");
				this.devices = new HashSet<Long>(150);
				for (i = 0; i < n; i++) {
					this.devices.add(Long.valueOf(splitted[i], 2));
				}
				
//				if (testCaseNumber == 101) {
//					System.out.printf("");
//				}
				iterationCounter = 0;
				System.out.printf("Case #%d: N=%d, L=%d, ", testCaseNumber, n, bitCount);

				initialAnswer = minSwitchesToBeFlipped();
				
				if (initialAnswer < 0) {
					answer = "NOT POSSIBLE";
				} else {
					answer = String.valueOf(initialAnswer);
				}

				System.out.printf("answer=%s (iterations=%d)\n", answer, iterationCounter);

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
	
	private int minSwitchesToBeFlipped() {
		for (int i = 0; i <= this.bitCount; i++) {
			if (iterateBitsToChargeDevices(i, 0, 0)) {
				return i;
			}
		}
			
		return -1;
	}
	
	private boolean iterateBitsToChargeDevices(int bitCountToBeSet, int startBitToSet, long currentFlipper) {
		boolean result;
		iterationCounter++;
		if (bitCountToBeSet <= 0) {
			result = true;
// TUNED:
//			for (long outlet : this.outlets) {
			for (int i = this.outlets.length - 1; i >= 0; i--) {
				result = this.devices.contains(this.outlets[i] ^ currentFlipper); 
				if (!result) {
					break;
				}
			}
			
		} else {
			result = false;
			for (int i = startBitToSet; i < this.bitCount; i++) {
				result = iterateBitsToChargeDevices(bitCountToBeSet - 1, i + 1, currentFlipper | (1 << i)); 
				if (result) {
					break;
				}
			}
		}

		return result;
	}

//	
//	@SuppressWarnings("unused")
//	private static void iterateBits(long num, int bitCount, int bitCountToBeSet, int startBitToSet) {
//		if (bitCountToBeSet <= 0) {
//			System.out.printf("%0" + String.valueOf(bitCount) + "d ", Long.valueOf(Long.toBinaryString(num)));
//		} else {
//			for (int i = startBitToSet; i < bitCount; i++) {
//				iterateBits(num | (1 << i), bitCount, bitCountToBeSet - 1, i + 1);
//			}
//		}
//	}
}