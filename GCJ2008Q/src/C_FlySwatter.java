/* Problem C. Fly Swatter (Oh! Fucking hard geometry problem)

What are your chances of hitting a fly with a tennis racquet?
To start with, ignore the racquet's handle. Assume the racquet is a perfect ring, 
of outer radius R and thickness t (so the inner radius of the ring is R−t).

The ring is covered with horizontal and vertical strings. Each string is a cylinder of radius r. 
Each string is a chord of the ring (a straight line connecting two points of the circle). 
There is a gap of length g between neighbouring strings. The strings are symmetric with respect to the center 
of the racquet i.e. there is a pair of strings whose centers meet at the center of the ring.

The fly is a sphere of radius f. Assume that the racquet is moving in a straight line perpendicular to the plane of 
the ring. Assume also that the fly's center is inside the outer radius of the racquet and is equally likely 
to be anywhere within that radius. Any overlap between the fly and the racquet (the ring or a string) counts as a hit.

Input
One line containing an integer N, the number of test cases in the input file.

The next N lines will each contain the numbers f, R, t, r and g separated by exactly one space. 
Also the numbers will have at most 6 digits after the decimal point.

Output
N lines, each of the form "Case #k: P", where k is the number of the test case and 
P is the probability of hitting the fly with a piece of the racquet.

Answers with a relative or absolute error of at most 10-6 will be considered correct.

Limits
f, R, t, r and g will be positive and smaller or equal to 10000.
t < R
f < R
r < R

Small dataset: 1 ≤ N ≤ 30

The total number of strings will be at most 60 (so at most 30 in each direction).

Large dataset: 1 ≤ N ≤ 100

The total number of strings will be at most 2000 (so at most 1000 in each direction).

Sample

Input
5
0.25 1.0 0.1 0.01 0.5
0.25 1.0 0.1 0.01 0.9
0.00001 10000 0.00001 0.00001 1000
0.4 10000 0.00001 0.00001 700
1 100 1 1 10

Output
Case #1: 1.000000
Case #2: 0.910015
Case #3: 0.000000
Case #4: 0.002371
Case #5: 0.573972
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class C_FlySwatter {
	public static void main(String[] args) {
		String inputFileName, outputFileName;
		CommonUtils.preamble();
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
//			inputFileName = "-small-practice.in";
			inputFileName = "-large-practice.in";
			inputFileName = C_FlySwatter.class.getSimpleName().substring(0, 1) + inputFileName;
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
			C_FlySwatter solver = new C_FlySwatter();
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

		double f;		//fly sphere radius
		double R;		//racket outer radius
		double t;		//racket thickness
		double r;		//string cylinder radius (string thickness = 2r) 
		double g;		//string gap (square)
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				splitted = textLine.split("\\s+");
				f = Double.parseDouble(splitted[0]);
				R = Double.parseDouble(splitted[1]);
				t = Double.parseDouble(splitted[2]);
				r = Double.parseDouble(splitted[3]);
				g = Double.parseDouble(splitted[4]);

				System.out.printf("Case #%d: f=%s, R=%s, t=%s, r=%s, g=%s, "
								, testCaseNumber, f, R, t, r, g
								);
				
				if (testCaseNumber == 15) {
					System.out.printf("");
				}
				iterationCounter = 0;
				answer = 1.0 - missingProbabilty(f, R, t, r, g);
				System.out.printf("answer=%.6f, iterations=%d\n", answer, iterationCounter);
				

				w.printf("Case #%d: %.6f\n", testCaseNumber, answer);
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

	private static double circle_segment(double rad, double theta) {
		  return rad*rad * (theta - Math.sin(theta))/2.0;
	}

	/**
	 * @param f : fly sphere radius
	 * @param R : racket outer radius
	 * @param t : racket thickness
	 * @param r : string cylinder radius (string thickness = 2r) 
	 * @param g : string gap (square)
	 * @return the probability of missed hitting the fly (use 1-returnValue to get the complement)
	 */
	private static double missingProbabilty(double f,	double R, double t,	double r, double g) {
		// not originated by me. I copied from the correct solution
		double rad = R - t - f;
		double ar = 0.0;
		for (double x1 = r + f; x1 < rad; x1 += g + 2*r) {
			for (double y1 = r + f; y1 < rad; y1 += g + 2*r) {
				double x2 = x1 + g - 2*f;
				double y2 = y1 + g - 2*f;
				if (x2 <= x1 || y2 <= y1)
					continue;
				if (x1 * x1 + y1 * y1 >= rad * rad)
					continue;
				if (x2 * x2 + y2 * y2 <= rad * rad) {
					// All points are inside circle.
					ar += (x2 - x1) * (y2 - y1);
				} else if (x1 * x1 + y2 * y2 >= rad * rad
						&& x2 * x2 + y1 * y1 >= rad * rad) {
					// Only (x1,y1) inside circle.
					ar += circle_segment(rad, Math.acos(x1 / rad) - Math.asin(y1 / rad))
							+ (Math.sqrt(rad * rad - x1 * x1) - y1)
							* (Math.sqrt(rad * rad - y1 * y1) - x1) / 2;
				} else if (x1 * x1 + y2 * y2 >= rad * rad) {
					// (x1,y1) and (x2,y1) inside circle.
					ar += circle_segment(rad, Math.acos(x1 / rad) - Math.acos(x2 / rad))
							+ (x2 - x1)
							* (Math.sqrt(rad * rad - x1 * x1) - y1
									+ Math.sqrt(rad * rad - x2 * x2) - y1) / 2;
				} else if (x2 * x2 + y1 * y1 >= rad * rad) {
					// (x1,y1) and (x1,y2) inside circle.
					ar += circle_segment(rad, Math.asin(y2 / rad) - Math.asin(y1 / rad))
							+ (y2 - y1)
							* (Math.sqrt(rad * rad - y1 * y1) - x1
									+ Math.sqrt(rad * rad - y2 * y2) - x1) / 2;
				} else {
					// All except (x2,y2) inside circle.
					ar += circle_segment(rad, Math.asin(y2 / rad) - Math.acos(x2 / rad))
							+ (x2 - x1) * (y2 - y1)
							- (y2 - Math.sqrt(rad * rad - x2 * x2))
							* (x2 - Math.sqrt(rad * rad - y2 * y2)) / 2;
				}
			}
		}
		return ar / (Math.PI * R * R / 4);
	}
}