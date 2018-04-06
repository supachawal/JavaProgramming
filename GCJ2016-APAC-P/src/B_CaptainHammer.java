/* Problem B. Captain Hammer (Projectile Motion)


The Hamjet is a true marvel of aircraft engineering. It is a jet airplane with a single engine so powerful that it burns all of its fuel instantly during takeoff. The Hamjet doesn't have any wings because who needs them when the fuselage is made of a special Wonderflonium isotope that makes it impervious to harm. 

Piloting the Hamjet is a not a job for your typical, meek-bodied superhero. That's why the Hamjet belongs to Captain Hammer, who is himself impervious to harm. The G-forces that the pilot endures when taking a trip in the Hamjet are legen-dary. 

The Hamjet takes off at an angle of θ degrees up and a speed of V meters per second. V is a fixed value that is determined by the awesome power of the Hamjet engine and the capacity of its fuel tank. The destination is D meters away. Your job is to program the Hamjet's computer to calculate θ given V and D. 

Fortunately, the Hamjet's Wondeflonium hull is impervious to air friction. Even more fortunately, the Hamjet doesn't fly too far or too high, so you can assume that the Earth is flat, and that the acceleration due to gravity is a constant 9.8 m/s2 down. 

Input

The first line of the input gives the number of test cases, T. T lines follow. Each line will contain two positive integers -- V and D. 

Output

For each test case, output one line containing "Case #x: θ", where x is the case number (starting from 1) and θ is in degrees up from the the horizontal. If there are several possible answers, output the smallest positive one. 

An answer will be considered correct if it is within 10-6 of the exact answer, in absolute or relative error. See the FAQ for an explanation of what that means, and what formats of floating-point numbers we accept. 

Limits

1 ≤ T ≤ 4500;
1 ≤ V ≤ 300;
1 ≤ D ≤ 10000;
It is guaranteed that each test case will be solvable. 

Sample

Input 
3
98 980
98 490
299 1234
   
Output 
Case #1: 45.0000000
Case #2: 15.0000000
Case #3: 3.8870928
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class B_CaptainHammer {
	public static void main(String[] args) {
		CommonUtils.preamble();

		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
			inputFileName = "-small-practice.in";
//			inputFileName = "-small-practice-1.in";
//			inputFileName = "-small-practice-2.in";
//			inputFileName = "-large-practice.in";
			inputFileName = B_CaptainHammer.class.getSimpleName().substring(0, 1) + inputFileName;
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
			B_CaptainHammer solver = new B_CaptainHammer();
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
		double answer;
		double V, D;
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				if (testCaseNumber == 168) {
					System.out.printf("");
				}
				splitted = textLine.split("\\s+");
				V = Double.parseDouble(splitted[0]);
				D = Double.parseDouble(splitted[1]);

				System.out.printf("Case #%d: V=%g, D=%g, ", testCaseNumber, V, D);
				iterationCounter = 0;
				answer = calcTheta(V, D);
				
				System.out.printf("answer=%.7f (iterations=%d)\n", answer, iterationCounter);

				w.printf("Case #%d: %.7f\n", testCaseNumber, answer);
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
	
	private static double calcTheta(double V, double D) {
		double asinParam = 9.8 * D / (V * V);
		if (asinParam > 1.0) {
			asinParam = 1.0;
		}
		if (asinParam < -1.0) {
			asinParam = -1.0;
		}
		return 0.5 * Math.asin(asinParam) * 180.0 / Math.PI;
	}
}