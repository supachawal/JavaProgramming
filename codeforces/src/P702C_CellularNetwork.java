/*
 * Input
10 10
2 52 280 401 416 499 721 791 841 943
246 348 447 486 507 566 568 633 953 986
Output
244
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

public class P702C_CellularNetwork {
	private static BufferedReader br;
	private static PrintWriter pw;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		int i;
		String[] splitted = br.readLine().split("\\s+");
		int nCities = Integer.parseInt(splitted[0]);
		int nCellularTowers = Integer.parseInt(splitted[1]);
		int[] cities = new int[nCities];
		int[] cellularTowers = new int[nCellularTowers];
		int r = 0, t = 0, lastT = nCellularTowers - 2;
		
		splitted = br.readLine().split("\\s+");
		for (i = 0; i < nCities; i++) {
			cities[i] = Integer.parseInt(splitted[i]);
		}

		splitted = br.readLine().split("\\s+");
		for (i = 0; i < nCellularTowers; i++) {
			cellularTowers[i] = Integer.parseInt(splitted[i]);
		}

		for (i = 0; i < nCities; i++) {
			int cityPos = cities[i];
			int x = Arrays.binarySearch(cellularTowers, t, nCellularTowers - 1, cityPos);
			if (x < 0) {
				x = -x - 1;
			}
			if (x > 0) {
				t = x - 1;
			}
			
			int newR = Math.abs(cityPos - cellularTowers[t]);
			if (t <= lastT) {
				int newR2 = Math.abs(cityPos - cellularTowers[t + 1]);
				if (newR >= newR2) {
					t++;
					newR = newR2;
				}
			}
			
			if (r < newR) {
				r = newR;
			}
		}
		
		pw.printf("%d\n", r);
		pw.close();
	}
}