import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class JAN16_8_CYCLRACE_CyclistRace_SLOW {
/*
Example

Input:
5 14
1 1 1 2
1 1 2 5
1 2 3 4
1 2 4 7
2 3
2 4
1 5 5 4
2 5
2 6
1 7 5 8
2 7
2 8
2 9
2 10

Output:
10
15
21
28
35
42
49
56
*/

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		String textLine;
		String[] splitted;
		long answer;
		
		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		int N = Integer.parseInt(splitted[0]);
		int Q = Integer.parseInt(splitted[1]);
		int t, cyclist, newSpeed;
		long[] lastPosition = new long[N + 1];	//indexed by cyclist
		int[] lastT = new int[N + 1];			//indexed by cyclist
		int[] lastSpeed = new int[N + 1];			//indexed by cyclist
		
		for (int i = 0; i < Q; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			t = Integer.parseInt(splitted[1]);
			char c = splitted[0].charAt(0); 
			if (c == '1') {
				cyclist = Integer.parseInt(splitted[2]);
				newSpeed = Integer.parseInt(splitted[3]);
				lastPosition[cyclist] += (long)lastSpeed[cyclist] * (t - lastT[cyclist]);
				lastSpeed[cyclist] = newSpeed;
				lastT[cyclist] = t;
			} else if (c == '2') {
				answer = 0;
				for (cyclist = 1; cyclist <= N; cyclist++) {
					lastPosition[cyclist] += (long)lastSpeed[cyclist] * (t - lastT[cyclist]);
					lastT[cyclist] = t;
					if (answer < lastPosition[cyclist]) {
						answer = lastPosition[cyclist];
					}
				}
				pw.printf("%d\n", answer);
			}
		
		}
		pw.close();
	}
	
}
