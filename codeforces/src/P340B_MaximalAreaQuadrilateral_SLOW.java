import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/*
5
0 0
0 4
4 0
4 4
2 3

OUTPUT:
16.00000
 */
public class P340B_MaximalAreaQuadrilateral_SLOW {
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
		
		double maxArea = 0;
		Point2D[] points = { new Point2D.Double(), new Point2D.Double(), new Point2D.Double(), new Point2D.Double() };
		Point2D[] pointsOutput = { new Point2D.Double(), new Point2D.Double(), new Point2D.Double(), new Point2D.Double() };
		boolean[] visited = { true, false, false, false };
		
		for (int i = 0; i < n; i++) {
			points[0].setLocation(x[i], y[i]);
			for (int j = i + 1; j < n; j++) {
				points[1].setLocation(x[j], y[j]);
				for (int k = j + 1; k < n; k++) {
					points[2].setLocation(x[k], y[k]);
					for (int p = k + 1; p < n; p++) {
						points[3].setLocation(x[p], y[p]);
						pointsOutput[0].setLocation(points[0]);
						maxArea = Math.max(maxArea, maxPolygonArea(pointsOutput, points, visited, 1));
					}
				}
				
			}
		}
		pw.printf("%.10f", maxArea);
		pw.close();
	}
	
	
	private static double polygonArea(Point2D[] points) {
		double sum = 0.0;
		int n = points.length;
		int last = n - 1;
		
		for (int i = 0; i < n; i++)	{
			int k = (i >= last ? 0 : i + 1);
		    sum += points[i].getX() * points[k].getY() - points[i].getY()*points[k].getX();
		}
        		
		return Math.abs(sum) / 2.0;
	}

	private static double maxPolygonArea(Point2D[] pointsGenerated, Point2D[] points, boolean[] visited, int k) {
		if (k >= points.length) {
			return polygonArea(pointsGenerated);
		}
		
		double ret = 0;
		
		for (int i = points.length - 1; i >= 0; i--)	{
			if (!visited[i]) {
				visited[i] = true;
				pointsGenerated[k].setLocation(points[i]);
				ret = Math.max(ret, maxPolygonArea(pointsGenerated, points, visited, k + 1));
				visited[i] = false;
			}
		}
        		
		return ret;
	}
}

