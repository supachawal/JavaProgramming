import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class P616B_DinnerWithEmma_PASSED {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		String textLine = br.readLine();
		String[] splitted = textLine.split("\\s+");
		int nRows = Integer.parseInt(splitted[0]);
		int nCols = Integer.parseInt(splitted[1]);
		int answer = 0;
		
		
		for (int i = 0; i < nRows; i++) {
			int min = Integer.MAX_VALUE;
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			for (int j = 0; j < nCols; j++) {
				int val = Integer.parseInt(splitted[j]);
				if (min > val) {
					min = val;
				}
			}
			if (answer < min) {
				answer = min;
			}
		}
		pw.printf("%d\n", answer);
		pw.close();
	}
}