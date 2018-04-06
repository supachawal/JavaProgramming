import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

class P1705_SensorNetwork2 {
	//TAG: Unit disk graph (UDG) construction and find the maximum clique
	
/*test
INPUT:
4 1
0 0
0 1
1 0
1 1
OUTPUT:
2
1 2

INPUT:
5 20
0 0
0 2
100 100
100 110
100 120
OUTPUT:
3
4 3 5

INPUT:
3 1
1 1
10 10
20 20
OUTPUT:
1
1

INPUT:
6 20
98 110
90 95
75 110
90 120
93 136
96 135
OUTPUT:
3
4 5 6

INPUT:
24 20
98 110
90 95
80 80
65 70
55 55
36 50
18 50
7 34
-10 25
-30 25
96 136
-40 40
-40 60
-30 75
-20 90
-10 104
1 120
20 120
35 120
50 120
65 120
78 120
90 120
98 135
OUTPUT:
3
11 23 24

INPUT:
48 20
98 110
90 95
80 80
65 70
55 55
36 50
18 50
7 34
-10 25
-30 25
96 136
-40 40
-40 60
-30 75
-20 90
-10 104
1 120
20 120
35 120
50 120
65 120
78 120
90 120
98 135
98 110
90 95
80 80
65 70
55 55
36 50
18 50
7 34
-10 25
-30 25
96 136
-40 40
-40 60
-30 75
-20 90
-10 104
1 120
20 120
35 120
50 120
65 120
78 120
90 120
98 135

OUTPUT:
6
23 11 47 24 35 48 
computationCost = 144 iterations

 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		String textLine;
		String[] splitted;
//		Point2D[] centerPoint = {new Point2D.Double(), new Point2D.Double()};
////		int numOfPoints = GeometryUtils.findCircleCenterBy2PointsAtPerimeterAndRadius(centerPoint, -8, 8, 12, 8, 10);
//		int numOfPoints = GeometryUtils.findCircleCenterBy2PointsAtPerimeterAndRadius(centerPoint, -8, 8, 10, 14, 10);
//		if (numOfPoints == 0) {
//			pw.println("Impossible\n");
//		} else {
//			for (int i = 0; i < numOfPoints; i++) {
//				pw.printf("CenterPoint%d = (%f, %f)\n", i + 1, centerPoint[i].getX(), centerPoint[i].getY());
//			}
//		}
		int i, j, k;
		
		int nodeCount;
		int nodeRadius, nodeRadiusSq;
		
		int [][] coords;
		Point2D[] circleCenters = {new Point2D.Double(), new Point2D.Double()};
		ArrayList<Integer> maximumClique = new ArrayList<Integer>();
		ArrayList<Integer> unitDiskNeighborbood = new ArrayList<Integer>();
		double unitDiskRadius, unitDiskRadiusSq;
		
		while ((textLine = br.readLine()) != null) {
			if (textLine.length() == 0) {
				break;
			}
			splitted = textLine.split("\\s+");
			nodeCount = Integer.parseInt(splitted[0]);
			nodeRadius = Integer.parseInt(splitted[1]);
			nodeRadiusSq = nodeRadius * nodeRadius; 
			coords = new int[nodeCount][2];

			for (i = 0; i < nodeCount; i++) {
				textLine = br.readLine();
				splitted = textLine.split("\\s+");
				coords[i][0] = Integer.parseInt(splitted[0]);
				coords[i][1] = Integer.parseInt(splitted[1]);
			}

			maximumClique.clear();
			unitDiskRadius = nodeRadius / 2.0;
			unitDiskRadiusSq = unitDiskRadius * unitDiskRadius;
			
			for (i = 0; i < nodeCount; i++) {
				for (j = i + 1; j < nodeCount; j++) {
					if (GeometryUtils.intDistanceSq(coords[i][0], coords[i][1], coords[j][0], coords[j][1]) <= nodeRadiusSq) { 
						int nPossibleCircleCenters 
							= GeometryUtils.findCircleCenterBy2PointsAtPerimeterAndRadius(circleCenters
								, coords[i][0], coords[i][1], coords[j][0], coords[j][1], unitDiskRadius);
						for (int centerChoice = 0; centerChoice < nPossibleCircleCenters; centerChoice++) {
							Point2D diskCenter = circleCenters[centerChoice];
							unitDiskNeighborbood.clear();
							for (k = 0; k < nodeCount; k++) {
								if (diskCenter.distanceSq(coords[k][0], coords[k][1]) <= unitDiskRadiusSq) {
									unitDiskNeighborbood.add(k);
								}
							}
							if (unitDiskNeighborbood.size() > maximumClique.size()) {
								maximumClique.clear();
								maximumClique.addAll(unitDiskNeighborbood);
							}
						}
					}
				}
			}

			pw.printf("%d\n", maximumClique.size());
			for (Integer nodeIndex : maximumClique) {
				pw.printf("%d ", nodeIndex + 1);
			}
			
			pw.println();
		}
		
		pw.close();
	}

	public static class GeometryUtils {
		public static int findCircleCenterBy2PointsAtPerimeterAndRadius(Point2D[] answer, double x1, double y1, double x2, double y2, double r) {
			int answerCount = 0;
			//ref source :http://mathforum.org/library/drmath/view/53027.html
			//First, find the distance between points 1 and 2.  We'll call that q, and it's given by sqrt((x2-x1)^2 + (y2-y1)^2).
			double dX = x2 - x1, dY = y1 - y2;
			double dist = Math.sqrt(dX*dX + dY*dY);
			double hDist = dist / 2;
			//Second, find the point halfway between your two points.  We'll call it (x3, y3).  x3 = (x1+x2)/2  and  y3 = (y1+y2)/2.
			double x3 = (x1 + x2) / 2;
			double y3 = (y1 + y2) / 2;

			/*
			One answer will be:
				x = x3 + sqrt(r^2-(q/2)^2)*(y1-y2)/q
				y = y3 + sqrt(r^2-(q/2)^2)*(x2-x1)/q  
			
				The other will be:
				 
				x = x3 - sqrt(r^2-(q/2)^2)*(y1-y2)/q
				y = y3 - sqrt(r^2-(q/2)^2)*(x2-x1)/q
			*/  			
			double alpha = r*r - hDist*hDist;
			if (alpha >= 0) {
				double temp = Math.sqrt(alpha) / dist; 
				double dX3 = temp * dY;
				double dY3 = temp * dX;
	
				// beautify the floating point
				dX3 = ((long)(dX3 * 10000000000.0)) / 10000000000.0; 
				dY3 = ((long)(dY3 * 10000000000.0)) / 10000000000.0;
				
				answerCount = (dX3 == 0.0 && dY3 == 0.0) ? 1 : 2;
				
				answer[0] = new Point2D.Double(x3 + dX3, y3 + dY3);
				answer[1] = new Point2D.Double(x3 - dX3, y3 - dY3);
			}
			
			return answerCount;
		}
		
		public static int intDistanceSq(int x1, int y1, int x2, int y2) {
			return (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2); 
		}
	}
}
