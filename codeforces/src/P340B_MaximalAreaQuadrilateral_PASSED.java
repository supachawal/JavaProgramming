import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class P340B_MaximalAreaQuadrilateral_PASSED {
/*
 * Category: Geom
5
0 0
0 4
4 0
4 4
2 3

OUTPUT:
16.00000

Input
4
0 0
0 5
5 0
1 1
Answer
10.000000
 */
	private static BufferedReader br;
	private static PrintWriter pw;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String textLine = br.readLine();
		int n = Integer.parseInt(textLine);
		double[] x = new double[n];
		double[] y = new double[n];
		String[] splitted;
		
		for (int i = 0; i < n; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			x[i] = Double.parseDouble(splitted[0]);
			y[i] = Double.parseDouble(splitted[1]);
		}
		
		double maxArea = 0.0;
		
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < n; j++) {
				double maxAreaNegativeSubplane = 0.0;
				double maxAreaPositiveSubplane = 0.0;
				
				for (int k = 0; k < n; k++) {
					if (k != i && k != j) {
						int ccw = Line2D.relativeCCW(x[i], y[i], x[j], y[j], x[k], y[k]);
						if (ccw < 0) {
							maxAreaNegativeSubplane = Math.max(maxAreaNegativeSubplane, triangleArea(x[i], y[i], x[j], y[j], x[k], y[k]));
						} else if (ccw > 0){
							maxAreaPositiveSubplane = Math.max(maxAreaPositiveSubplane, triangleArea(x[i], y[i], x[j], y[j], x[k], y[k]));
						}
					}
				}
				
				if (maxAreaNegativeSubplane > 0 && maxAreaPositiveSubplane > 0) {
					maxArea = Math.max(maxArea, maxAreaNegativeSubplane + maxAreaPositiveSubplane);
				}
			}
		}
		pw.printf("%.6f", maxArea);
		pw.close();
	}
	
	
	private static final double triangleArea(double x1, double y1, double x2, double y2, double x3, double y3) {
		return Math.abs(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)) / 2.0;
	}

}

