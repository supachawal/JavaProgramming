/* Problem B. Center of Mass

You are studying a swarm of N fireflies. Each firefly is moving in a straight line at a constant speed. You are standing at the center of the 
universe, at position (0, 0, 0). Each firefly has the same mass, and you want to know how close the center of the swarm will get to your location
 (the origin).

You know the position and velocity of each firefly at t = 0, and are only interested in t ≥ 0. The fireflies have constant velocity, and may 
pass freely through all of space, including each other and you. Let M(t) be the location of the center of mass of the N fireflies at time t. 
Let d(t) be the distance between your position and M(t) at time t. 

Find the minimum value of d(t), dmin, and the earliest time when d(t) = dmin, tmin.

Input

The first line of input contains a single integer T, the number of test cases. Each test case starts with a line that contains an integer N, 
the number of fireflies, followed by N lines of the form

x y z vx vy vz

Each of these lines describes one firefly: (x, y, z) is its initial position at time t = 0, and (vx, vy, vz) is its velocity.

Output
For each test case, output
Case #X: dmin tmin
where X is the test case number, starting from 1. Any answer with absolute or relative error of at most 10-5 will be accepted.

Limits
All the numbers in the input will be integers.
1 ≤ T ≤ 100
The values of x, y, z, vx, vy and vz will be between -5000 and 5000, inclusive.

Small dataset: 3 ≤ N ≤ 10
Large dataset: 3 ≤ N ≤ 500

Sample

Input
3
3
3 0 -4 0 0 3
-3 -2 -1 3 0 0
-3 -1 2 0 3 0
3
-5 0 0 1 0 0
-7 0 0 1 0 0
-6 3 0 1 0 0
4
1 2 3 1 2 3
3 2 1 3 2 1
1 0 0 0 0 -1
0 10 0 0 -10 -1

Output
Case #1: 0.00000000 1.00000000
Case #2: 1.00000000 6.00000000
Case #3: 3.36340601 1.00000000

Notes

Given N points (xi, yi, zi), their center of the mass is the point (xc, yc, zc), where:

xc = (x1 + x2 + ... + xN) / N
yc = (y1 + y2 + ... + yN) / N
zc = (z1 + z2 + ... + zN) / N

 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class B_CenterOfMass {
	public static void main(String[] args) {
		CommonUtils.preamble();
		String inputFileName, outputFileName;
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "B-sample1.in";
//			inputFileName = "B-sample2.in";
//			inputFileName = "B-small-practice.in";
			inputFileName = "B-large-practice.in";
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
			B_CenterOfMass solver = new B_CenterOfMass();
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
		double [] answer;
		double [] x = new double[500], y = new double[500], z = new double[500]
				, vx = new double[500], vy = new double[500], vz = new double[500];

		int N;

		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
				N = Integer.parseInt(textLine);
				for (int i = 0; i < N; i++) {
					textLine = br.readLine();
					splitted = textLine.split("\\s+");
					x[i] = Double.parseDouble(splitted[0]);
					y[i] = Double.parseDouble(splitted[1]);
					z[i] = Double.parseDouble(splitted[2]);
					vx[i] = Double.parseDouble(splitted[3]);
					vy[i] = Double.parseDouble(splitted[4]);
					vz[i] = Double.parseDouble(splitted[5]);
				}

				System.out.printf("Case #%d: N=%d, ", testCaseNumber, N);
				
				if (testCaseNumber == 53) {
//					System.out.println("");
				}
				iterationCounter = 0;
				answer = convergeOriginByCenterOfMass(x, y, z, vx, vy, vz, N);

				System.out.printf("answer=%.8f %.8f, iterations=%d\n", answer[0], answer[1], iterationCounter);
				w.printf("Case #%d: %.8f %.8f\n", testCaseNumber, answer[0], answer[1]);
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
	
	private static double [] convergeOriginByCenterOfMass(double [] x, double [] y, double [] z, double [] vx, double [] vy, double [] vz, int n) {
		final double SAMPLE_TIME = 100.0; 
		double perpendicularLength;		// = (p0->b X a) / norm(a)
		double [] perpendicularIntersection; // = p0 + ((p0->b . a) / (norm(a)^2)) * a
		double [] p0 = centroidAtTime(0, x, y, z, vx, vy, vz, n);
		double [] b = {0.0, 0.0, 0.0};
		double [] p0b = CommonUtils.vectorCreate(p0, b, 3);
		double [] p = centroidAtTime(SAMPLE_TIME, x, y, z, vx, vy, vz, n);
		double [] a = CommonUtils.vectorCreate(p0, p, 3);
		double normA = CommonUtils.vectorNorm(a, 3);
		double t;
		
		if (normA == 0.0) {
			perpendicularLength = CommonUtils.vectorNorm(p0, 3);
			t = 0.0;
		} else { 
			double [] p0bCrossA = CommonUtils.vectorCrossProduct3D(p0b, a);
			perpendicularLength = CommonUtils.vectorNorm(p0bCrossA, 3) / normA;
			double p0bDotA = CommonUtils.vectorDotProduct(p0b, a, 3);
			perpendicularIntersection = CommonUtils.vectorAdd(p0, CommonUtils.vectorScale(a, 3, p0bDotA / (normA * normA)), 3);
	
			double [] v = CommonUtils.vectorScale(a, 3, 1.0 / SAMPLE_TIME);
			double [] p0perpend = CommonUtils.vectorCreate(p0, perpendicularIntersection, 3);
			t = calcTimeVector(p0perpend, v, 3);
		}
		
		if (t <= 0.0) {
			perpendicularLength = CommonUtils.vectorNorm(p0, 3);
			t = 0.0;
		}

		return new double [] {perpendicularLength, t};
	}
	
	private static final double calcTimeVector(double [] displacement, double [] velocity, int n) {
		for (int i = 0; i < n; i++) {
			if (displacement[i] != 0.0 && velocity[i] != 0.0)
				return displacement[i] / velocity[i];
		}

		return 0.0;
	}

	private static double [] centroidAtTime(double t, double [] x, double [] y, double [] z, double [] vx, double [] vy, double [] vz, int n) {
		double sumX = 0.0, sumY = 0.0, sumZ = 0.0;
		for (int i = 0; i < n; i++) {
			sumX += x[i] + vx[i] * t;
			sumY += y[i] + vy[i] * t;
			sumZ += z[i] + vz[i] * t;
		}
		
		return new double [] {sumX / n, sumY / n, sumZ / n};
	}
	
}