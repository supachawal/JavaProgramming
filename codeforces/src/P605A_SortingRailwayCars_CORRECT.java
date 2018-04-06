import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class P605A_SortingRailwayCars_CORRECT {
	// source: http://codeforces.com/profile/bharat.khanna.cse14
	// find the maximum in-a-row increasing interval and exclude it from the capacity
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String textLine;
		String[] splitted;
		int answer;

		int n; // # integers
		int[] v = new int[100001];	// by the problem assumption
		int i, val, u, max = 0;

		textLine = br.readLine();
		n = Integer.parseInt(textLine);

		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		for (i = 0; i < n; i++) {	// loop to count accumulate contiguous sequence length
			val = Integer.parseInt(splitted[i]);
			u = v[val] = v[val - 1] + 1;
			
			if (u > max) {
				max = u;
			}
		}

		answer = n - max;
		System.out.printf("%d", answer);
	}
}
