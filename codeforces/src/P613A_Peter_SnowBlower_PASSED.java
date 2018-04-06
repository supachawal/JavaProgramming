import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class P613A_Peter_SnowBlower_PASSED {
/* Sample
Input
3 0 0
-1 1
0 3
1 1

Output
21.9911485751

Answer
25.132741228718344928
*/	
	private static PrintWriter pw;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String textLine = br.readLine();
		String[] splitted = textLine.split("\\s+");
		int nVertices = Integer.parseInt(splitted[0]);
		double centerX = Double.parseDouble(splitted[1]);
		double centerY = Double.parseDouble(splitted[2]);
		double minDistanceSq = Double.POSITIVE_INFINITY;
		double maxDistanceSq = 0;
		double pointDistanceSq, lineDistanceSq;
		double x, y;
		double prevX = 0, prevY = 0;
		double answer = 0;
		double firstX = 0, firstY = 0;
		
		for (int i = 0; i < nVertices; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			x = Double.parseDouble(splitted[0]);
			y = Double.parseDouble(splitted[1]);
			pointDistanceSq = Point2D.distanceSq(centerX, centerY, x, y);
			if (i == 0) {
				firstX = x;
				firstY = y;
				lineDistanceSq = pointDistanceSq;
			} else {
				lineDistanceSq = ptSegDistSq(x, y, prevX, prevY, centerX, centerY);
			}
			
			if (minDistanceSq > lineDistanceSq) {
				minDistanceSq = lineDistanceSq;
			}
			if (maxDistanceSq < pointDistanceSq) {
				maxDistanceSq = pointDistanceSq;
			}
			prevX = x;
			prevY = y;
		}
		
		if (nVertices > 1) {
			x = firstX;
			y = firstY;
			lineDistanceSq = ptSegDistSq(x, y, prevX, prevY, centerX, centerY);
			if (minDistanceSq > lineDistanceSq) {
				minDistanceSq = lineDistanceSq;
			}
			lineDistanceSq = ptSegDistSq(x, y, prevX, prevY, centerX, centerY);
		}
		
		answer = Math.PI * (maxDistanceSq - minDistanceSq);
		
		pw.printf("%.10f\n", answer);
		pw.close();
	}
	
    public static double ptSegDistSq(double x1, double y1,
            double x2, double y2,
            double px, double py)
	{
		// Adjust vectors relative to x1,y1
		// x2,y2 becomes relative vector from x1,y1 to end of segment
		x2 -= x1;
		y2 -= y1;
		// px,py becomes relative vector from x1,y1 to test point
		px -= x1;
		py -= y1;
		double dotprod = px * x2 + py * y2;
		double projlenSq;
		if (dotprod <= 0.0) {
			// px,py is on the side of x1,y1 away from x2,y2
			// distance to segment is length of px,py vector
			// "length of its (clipped) projection" is now 0.0
			projlenSq = 0.0;
		} else {
			// switch to backwards vectors relative to x2,y2
			// x2,y2 are already the negative of x1,y1=>x2,y2
			// to get px,py to be the negative of px,py=>x2,y2
			// the dot product of two negated vectors is the same
			// as the dot product of the two normal vectors
			px = x2 - px;
			py = y2 - py;
			dotprod = px * x2 + py * y2;
			if (dotprod <= 0.0) {
				// px,py is on the side of x2,y2 away from x1,y1
				// distance to segment is length of (backwards) px,py vector
				// "length of its (clipped) projection" is now 0.0
				projlenSq = 0.0;
			} else {
				// px,py is between x1,y1 and x2,y2
				// dotprod is the length of the px,py vector
				// projected on the x2,y2=>x1,y1 vector times the
				// length of the x2,y2=>x1,y1 vector
				projlenSq = dotprod * dotprod / (x2 * x2 + y2 * y2);
			}
		}
		// Distance to line is now the length of the relative point
		// vector minus the length of its projection onto the line
		// (which is zero if the projection falls outside the range
		// of the line segment).
		double lenSq = px * px + py * py - projlenSq;
		if (lenSq < 0f) {
			lenSq = 0f;
		}
		return lenSq;
	}
}