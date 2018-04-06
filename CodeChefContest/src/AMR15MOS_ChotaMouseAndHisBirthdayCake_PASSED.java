import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

class AMR15MOS_ChotaMouseAndHisBirthdayCake_PASSED {
/*Sample
Input (F H W)
3
1 1 2
1
1 4 5
3
2 3 9
6 2

Output
1
8
6

*/
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		int testCaseNumber = 0, testCaseCount;
		String textLine;
		String[] splitted;
		int answer;
		
		int F; // # cuts
		int H, W;
		int minWidth, eachWidth;
		int[] P;
		
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			splitted = textLine.split("\\s+");
			F = Integer.parseInt(splitted[0]);
			H = Integer.parseInt(splitted[1]);
			W = Integer.parseInt(splitted[2]);
			
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			P = new int[F + 2];
			for (int i = 0; i < F; i++) {
				P[i] = Integer.parseInt(splitted[i]);
			}
			P[F] = 0;
			P[F + 1] = W;
			Arrays.sort(P);

			minWidth = Integer.MAX_VALUE;
			for (int i = 0; i <= F; i++) {
				eachWidth = P[i + 1] - P[i];
				
				if (minWidth > eachWidth) {
					minWidth = eachWidth;
				}
			}

			answer = minWidth * H;
			
			pw.printf("%d\n", answer);
		}
		pw.close();
	}
}
