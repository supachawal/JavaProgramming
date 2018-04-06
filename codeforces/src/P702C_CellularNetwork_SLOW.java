import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

public class P702C_CellularNetwork_SLOW {
	private static BufferedReader br;
	private static PrintWriter pw;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		int i, j;
		String[] splitted = br.readLine().split("\\s+");
		int nCities = Integer.parseInt(splitted[0]);
		int nCellularTowers = Integer.parseInt(splitted[1]);
		int answer = 0;
		int[] cities = new int[nCities];
		int[] r = new int[nCities];
		
		splitted = br.readLine().split("\\s+");
		for (i = 0; i < nCities; i++) {
			cities[i] = Integer.parseInt(splitted[i]);
			r[i] = Integer.MAX_VALUE;
		}

		splitted = br.readLine().split("\\s+");
		for (i = 0; i < nCellularTowers; i++) {
			int cellularTowerPos = Integer.parseInt(splitted[i]);
			int k = Arrays.binarySearch(cities, cellularTowerPos);
			
			if (k < 0) {
				k = -k - 1;
			}
			
			for (j = k - 1; j >= 0; j--) {
				int newR = Math.abs(cities[j] - cellularTowerPos);
				if (r[j] < newR) {
					break;
				}
				r[j] = newR;
			}
		
			for (j = k; j < nCities; j++) {
				int newR = Math.abs(cities[j] - cellularTowerPos);
				if (r[j] < newR) {
					break;
				}
				r[j] = newR;
			}
		}

		for (i = 0; i < nCities; i++) {
			if (answer < r[i]) {
				answer = r[i];
			}
		}
		
		
		pw.printf("%d\n", answer);
		pw.close();
	}
}