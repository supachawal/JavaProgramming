/* Problem B. Train Timetable (minimum trains problem)

A train line has two stations on it, A and B. Trains can take trips from A to B or from B to A multiple times during a day. 
When a train arrives at B from A (or arrives at A from B), it needs a certain amount of time before it is ready to take the 
return journey - this is the turnaround time. For example, if a train arrives at 12:00 and the turnaround time is 0 minutes, 
it can leave immediately, at 12:00.
A train timetable specifies departure and arrival time of all trips between A and B. The train company needs to know how many 
trains have to start the day at A and B in order to make the timetable work: 
whenever a train is supposed to leave A or B, there must actually be one there ready to go. There are passing sections on 
the track, so trains don't necessarily arrive in the same order that they leave. Trains may not travel on trips that do 
not appear on the schedule.

Input
The first line of input gives the number of cases, N. N test cases follow.
Each case contains a number of lines. The first line is the turnaround time, T, in minutes. The next line has two numbers on it, 
NA and NB. NA is the number of trips from A to B, and NB is the number of trips from B to A. 
Then there are NA lines giving the details of the trips from A to B.
Each line contains two fields, giving the HH:MM departure and arrival time for that trip. The departure time for each trip will be earlier than the arrival time. All arrivals and departures occur on the same day. The trips may appear in any order - they are not necessarily sorted by time. The hour and minute values are both two digits, zero-padded, and are on a 24-hour clock (00:00 through 23:59).
After these NA lines, there are NB lines giving the departure and arrival times for the trips from B to A.

Output
For each test case, output one line containing "Case #x: " followed by the number of trains that must start at A and 
the number of trains that must start at B.

Limits: 1 ≤ N ≤ 100

Small dataset: 0 ≤ NA, NB ≤ 20     0 ≤ T ≤ 5

Large dataset: 0 ≤ NA, NB ≤ 100    0 ≤ T ≤ 60

Sample

Input
	
2
5
3 2
09:00 12:00
10:00 13:00
11:00 12:30
12:02 15:00
09:00 10:30
2
2 0
09:00 09:01
12:00 12:02

Output

Case #1: 2 2
Case #2: 2 0
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
import java.util.Iterator;
import java.util.List;

public class B_TrainTimetable {
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
			inputFileName = B_TrainTimetable.class.getSimpleName().substring(0, 1) + inputFileName;
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
			B_TrainTimetable solver = new B_TrainTimetable();
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
		int [] answer;

		int turnaroundMinutes, NA, NB;
		int i;
		List<IntPair> A, B;
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				turnaroundMinutes = Integer.parseInt(textLine);
				textLine = br.readLine();
				splitted = textLine.split("\\s+");
				NA = Integer.parseInt(splitted[0]);
				NB = Integer.parseInt(splitted[1]);

				A = new ArrayList<IntPair>();
				for (i = 0; i < NA; i++) {
					textLine = br.readLine();
					splitted = textLine.split("(\\s+)|:");
					A.add(new IntPair(Integer.parseInt(splitted[0]) * 60 + Integer.parseInt(splitted[1])
										   , Integer.parseInt(splitted[2]) * 60 + Integer.parseInt(splitted[3])
										   )
						 );
				}
				Collections.sort(A);

				B = new ArrayList<IntPair>();
				for (i = 0; i < NB; i++) {
					textLine = br.readLine();
					splitted = textLine.split("(\\s+)|:");
					B.add(new IntPair(Integer.parseInt(splitted[0]) * 60 + Integer.parseInt(splitted[1])
							   			  , Integer.parseInt(splitted[2]) * 60 + Integer.parseInt(splitted[3])
							              )
						 );
				}
				Collections.sort(B);

				System.out.printf("Case #%d: TA=%d, NA=%d, NB=%d\n\t,A=%s\n\t,B=%s\n\t,"
								, testCaseNumber, turnaroundMinutes, NA, NB
								, CommonUtils.showTimetable(A.iterator(), 20)
								, CommonUtils.showTimetable(B.iterator(), 20)
								);
				
				if (testCaseNumber == 15) {
					System.out.printf("");
				}
				iterationCounter = 0;
				answer = new int[] {minTrains(turnaroundMinutes, A, B), minTrains(turnaroundMinutes, B, A)};
				System.out.printf("answer=[%d %d], iterations=%d\n", answer[0], answer[1], iterationCounter);
				

				w.printf("Case #%d: %d %d\n", testCaseNumber, answer[0], answer[1]);
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

	private static int minTrains(int turnaroundMinutes, List<IntPair> A, List<IntPair> B) {
		int trainA = A.size();
		IntPair timeA;
		IntPair timeB = null;
		int tdA;
		int taB = 0;

		Collections.sort(A);
		Collections.sort(B, (o1, o2) -> (o1.v2 - o2.v2));
		Iterator<IntPair> Ai = A.iterator();
		Iterator<IntPair> Bi = B.iterator();
		
		while (Ai.hasNext() && (Bi.hasNext() || timeB != null)) {
			timeA = Ai.next();
			tdA = timeA.v1;
			
			if (timeB == null) {
				timeB = Bi.next();
				taB = timeB.v2;
			}
			
			if (taB + turnaroundMinutes <= tdA) {
				trainA--;
				timeB = null;
			}
		}
		
		return trainA;
	}
}