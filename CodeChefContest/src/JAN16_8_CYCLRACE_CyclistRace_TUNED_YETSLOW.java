import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class JAN16_8_CYCLRACE_CyclistRace_TUNED_YETSLOW {
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
		int i, t, cyclist, newSpeed;

		CyclistInfo[] cyclists = new CyclistInfo[N];
		int[] activeIndices = new int[N];
		int activeIndexCount = 0;
		
		for (i = 0; i < Q; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			t = Integer.parseInt(splitted[1]);
			char c = splitted[0].charAt(0); 
			if (c == '1') {
				cyclist = Integer.parseInt(splitted[2]) - 1;	// make it zero-based 
				newSpeed = Integer.parseInt(splitted[3]);
				CyclistInfo ci = cyclists[cyclist];
				
				if (ci == null) {
					ci = new CyclistInfo();
					cyclists[cyclist] = ci;
					activeIndices[activeIndexCount++] = cyclist;
				}
				
				ci.changeSpeed(t, newSpeed);
			} else if (c == '2') {
				answer = 0;
				for (int j = 0; j < activeIndexCount; j++) {
					answer = Math.max(answer, cyclists[activeIndices[j]].getUpdatePosition(t));
				}
				pw.printf("%d\n", answer);
			}
		
		}
		pw.close();
	}

	public static class CyclistInfo {
		long lastPosition;
		int lastT;
		int lastSpeed;
		
		public long getUpdatePosition(int t) {
			lastPosition += (long)lastSpeed * (t - lastT);
			lastT = t;
			return lastPosition;
		}
		public void changeSpeed(int t, int newSpeed) {
			lastPosition += (long)lastSpeed * (t - lastT);
			lastT = t;
			lastSpeed = newSpeed;
		}
	}
}
