import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class P611C_NewYearAndDomino_SLOW {
	private static PrintWriter pw;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String textLine;
		String[] splitted;
		int answer = 0;

		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		int h = Integer.parseInt(splitted[0]);
		char[][] matrix = new char[h][];
		int i;

		for (i = 0; i < h; i++) {
			matrix[i] = br.readLine().toCharArray();
		}

		textLine = br.readLine();
		int q = Integer.parseInt(textLine);

		for (i = 0; i < q; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			answer = countWaysToPlaceDomino(matrix, Integer.parseInt(splitted[0]) - 1,
					Integer.parseInt(splitted[1]) - 1, Integer.parseInt(splitted[2]) - 1,
					Integer.parseInt(splitted[3]) - 1);
			pw.printf("%d\n", answer);
		}

		pw.close();
	}

	private static int countWaysToPlaceDomino(char[][] matrix, int r1, int c1, int r2, int c2) {
		int ret = 0;
		for (int i = r1; i <= r2; i++) {
			for (int j = c1; j <= c2; j++) {
				if (i < r2 && matrix[i][j] == '.' && matrix[i + 1][j] == '.') {
					ret++;
				}
				if (j < c2 && matrix[i][j] == '.' && matrix[i][j + 1] == '.') {
					ret++;
				}
			}
		}
		return ret;
	}
}
