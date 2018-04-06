/* Problem C. Hiking Deer

Herbert Hooves the deer is going for a hike: one clockwise loop around his favorite circular trail, starting at 
degree zero. Herbert has perfect control over his speed, which can be any nonnegative value (not necessarily an integer) 
at any time -- he can change his speed instantaneously whenever he wants. When Herbert reaches his starting point again, 
the hike is over.

The trail is also used by human hikers, who also walk clockwise around the trail. Each hiker has a starting point and 
moves at her own constant speed. Humans continue to walk around and around the trail forever.

Herbert is a skittish deer who is afraid of people. He does not like to have encounters with hikers. An encounter 
occurs whenever Herbert and a hiker are in exactly the same place at the same time. You should consider Herbert and the 
hikers to be points on the circumference of a circle.

Herbert can have multiple separate encounters with the same hiker.

If more than one hiker is encountered at the same instant, all of them count as separate encounters.

Any encounter at the exact instant that Herbert finishes his hike still counts as an encounter.

If Herbert were to have an encounter with a hiker and then change his speed to exactly match that hiker's speed and 
follow along, he would have infinitely many encounters! Of course, he must never do this.

Encounters do not change the hikers' behavior, and nothing happens when hikers encounter each other.

Herbert knows the starting position and speed of each hiker. What is the minimum number of encounters with hikers that 
he can possibly have? 

Solving this problem

Usually, Google Code Jam problems have 1 Small input and 1 Large input. This problem has 2 Small inputs and 1 Large 
input. You must solve the first Small input before you can attempt the second Small input; as usual, you will be able 
to retry the Small inputs (with a time penalty). Once you have solved both Small inputs, you will be able to download 
the Large input; as usual, you will get only one chance at the Large input. 

Input

The first line of the input gives the number of test cases, T. T test cases follow. Each begins with one line with an 
integer N, and is followed by N lines, each of which represents a group of hikers starting at the same position on the 
trail. The ith of these lines has three space-separated integers: a starting position Di (representing Di/360ths of the 
way around the trail from the deer's starting point), the number Hi of hikers in the group, and Mi, the amount of time 
(in minutes) it takes for the fastest hiker in that group to make each complete revolution around the circle. The other 
hikers in that group each complete a revolution in Mi+1, Mi+2, ..., Mi+Hi-1 minutes. For example, the line

180 3 4

would mean that three hikers begin halfway around the trail from the deer's starting point, and that they take 4, 5, 
and 6 minutes, respectively, to complete each full revolution around the trail.

Herbert always starts at position 0 (0/360ths of the way around the circle), and no group of hikers does. Multiple 
groups of hikers may begin in the same place, but no two hikers will both begin in the same place and have the same speed. 

Output
For each test case, output one line containing "Case #x: y", where x is the test case number (starting from 1) and y is 
the minimum number of encounters with hikers that the deer can have. 

Limits
1 ≤ T ≤ 100.
1 ≤ Di ≤ 359.
1 ≤ N ≤ 1000.
1 ≤ Hi.
1 ≤ Mi ≤ 109. (Note that this only puts a cap on the time required for the fastest hiker in each group to complete a 
revolution. Slower hikers in the group will take longer.)

Small dataset 1
The total number of hikers in each test case will not exceed 2.


Small dataset 2
The total number of hikers in each test case will not exceed 10.


Large dataset
The total number of hikers in each test case will not exceed 500000.


Sample
Input 
3
4
1 1 12
359 1 12
2 1 12
358 1 12
2
180 1 100000
180 1 1
1
180 2 1


Output 
Case #1: 0
Case #2: 1
Case #3: 0

 
In Case #1, the hikers all happen to be moving at the same speed, and one way for Herbert to avoid encountering any of them is to move exactly as fast as they do.

 In Case #2, the second hiker is moving much faster than the first, and if Herbert goes slowly enough to avoid overtaking the first hiker, he will have multiple encounters with the speedy second hiker. One optimal strategy for Herbert is to go exactly as fast as the second hiker, encountering the first hiker once and never encountering the second hiker at all.

 In Case #3, the two hikers start in the same place, but one is twice as fast as the other. One optimal strategy is for Herbert to immediately catch up to the slower hiker without overtaking him, follow just behind him until he passes the deer's starting position, and then finish quickly before the faster hiker can catch Herbert.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class C_HikingDeer {
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
			inputFileName = C_HikingDeer.class.getSimpleName().substring(0, 1) + inputFileName;
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
			C_HikingDeer solver = new C_HikingDeer();
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
	
	private class HikerInfo {
		public int startingPosition;
		public long periodInMilliminutes;
		public long T(int i) {
			return ((360 - startingPosition) * periodInMilliminutes) / 360 
					+ (i - 1) * periodInMilliminutes;
		}
	}

	public boolean solve(final File aFile, PrintWriter w) {
		String textLine;
		boolean result = false;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
		String [] splitted;
		int answer;
		int N;
		ArrayList<HikerInfo> hikers = new ArrayList<HikerInfo>();
		int lineNo = 1;

		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				N = Integer.parseInt(textLine);
				hikers.clear();
				for (int i = 0; i < N; i++) {
					textLine = br.readLine();
					splitted = textLine.split("\\s+");
					int hikerPosition = Integer.parseInt(splitted[0]);
					int hikerCount = Integer.parseInt(splitted[1]);
					int periodInMinutes = Integer.parseInt(splitted[2]);
					for (int j = 0; j < hikerCount; j++) {
						HikerInfo hi = new HikerInfo();
						hi.startingPosition = hikerPosition;
						hi.periodInMilliminutes = (periodInMinutes + j) * 1000L;
						hikers.add(hi);
					}
				}
				
				System.out.printf("Case #%d: LineNo=%d, N=%d, H=%d, ", testCaseNumber, lineNo + 1, N, hikers.size());
				lineNo += N + 1;
				
				if (testCaseNumber == 7) {
					System.out.printf("");
				}
				
				iterationCounter = 0;
				answer = minimumEncounters(hikers);
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

	private static class HikerInfoComparator implements Comparator<HikerInfo> {
		public static int maxEncounters;
		
		@Override
		public int compare(HikerInfo o1, HikerInfo o2) {
			return Long.signum(o1.T(maxEncounters + 1) - o2.T(maxEncounters + 1));
		}
	}
	
	private int minimumEncounters(ArrayList<HikerInfo> hikers) {
		/*
		 * if X <= T1, Herbert pass hiker 1 encounter
		 * else if T1 < X < T2, no encounter
		 * else if T2 <= X < T3,  hiker passes Herbert 1 encounter
		 * else if T3 <= X < T4,  hiker passes Herbert 1 encounter
		 * ...
		 * else if Tn <= X < Tn+1,  hiker passes Herbert n-1 encounter
		 * So, n = encounter+1
		 */
		int maxEncounters = hikers.size();
		int minSoFar = maxEncounters;
		long fastestTn = Long.MAX_VALUE;
		HikerInfoComparator.maxEncounters = maxEncounters;
		Collections.sort(hikers, new HikerInfoComparator());
		fastestTn = hikers.get(0).T(maxEncounters + 1);
		System.out.printf("fastestTn=%d, ", fastestTn);
		
		for (HikerInfo h : hikers) {
			for (long Ti = h.T(1); Ti < fastestTn && minSoFar > 0; Ti += h.periodInMilliminutes) {
				for (long k = -1; k <= 1 && minSoFar > 0; k++) {
					long X = Ti + k;
					int encounter = 0;
					
					for (HikerInfo hiker : hikers) {
						iterationCounter++;
						long T1 = hiker.T(1);
						long T2 = T1 + hiker.periodInMilliminutes;
						if ( !(T1 < X && X < T2) ) {
							if (X <= T1) {
								if (++encounter >= minSoFar) {
									break;
								}
							} else {
								// ----- TUNED
								int j = 3 + (int)((X - T2) / hiker.periodInMilliminutes);
								T1 = hiker.T(j - 1);
								T2 = T1 + hiker.periodInMilliminutes;
								encounter += j - 2;
								if (encounter >= minSoFar) {
									if (X < fastestTn) {
										fastestTn = X;
//										System.out.printf("fastestTn=%d, ", fastestTn);
									}
									break;
								}
								// -----
							}
						}
					}

					if (encounter < minSoFar) {
						minSoFar = encounter;
//						System.out.printf("minSoFar=%d, ", minSoFar);
					}
				}
			}
		}
		
		return minSoFar;
	}
}