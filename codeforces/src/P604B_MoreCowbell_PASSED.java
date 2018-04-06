import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class P604B_MoreCowbell_PASSED {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String textLine;
		String[] splitted;
		int answer;

		int[] s;
		int n; // # cowbells
		int k; // # boxes
		int i, j, dualSize;
		
		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		n = Integer.parseInt(splitted[0]);
		k = Integer.parseInt(splitted[1]);
		s = new int[n];

		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		for (i = 0; i < n; i++) {
			s[i] = Integer.parseInt(splitted[i]);
		}

		answer = s[n - 1];		// last cowbell size is the biggest
		if (k < n) {
			if (2 * k > n) {
				j = 2 * (n - k) - 1;
			} else {
				j = n - 1;
			}
			// from now on, j would be the last cowbell index that must be packed in 2 pieces per box
			// let's find the max pair size
			for (i = 0; i < j; i++, j--) {
				dualSize = s[i] + s[j];
				if (dualSize > answer)
					answer = dualSize;
			}
		}
		System.out.printf("%d", answer);
	}

}
