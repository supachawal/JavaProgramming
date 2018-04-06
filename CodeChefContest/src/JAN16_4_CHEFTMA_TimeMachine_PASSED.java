import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map.Entry;
import java.util.TreeMap;

class JAN16_4_CHEFTMA_TimeMachine_PASSED {
/*
Example

Input:
1
4 2 2 
5 7 6 1
3 3 1 1
6 3
1 4

Output:
3
Explanation

Example case 1.
In this example Chef goes through the following steps:
Use black button 1 on the first day.
Use black button 4 on the second day.
Use white button 3 on the third day.
The arrays A and B are now effectively changed to:
5 7 3 1
4 7 1 1
So he will have 3 uncompleted tasks.
*/

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		int testCaseNumber = 0, testCaseCount;
		String textLine;
		String[] splitted;
		long answer = 0;
		int i;
		
		textLine = br.readLine();
		testCaseCount = Integer.parseInt(textLine);
		while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
			splitted = textLine.split("\\s+");
			int N = Integer.parseInt(splitted[0]);
			int nWhiteButtons = Integer.parseInt(splitted[1]);
			int nBlackButtons = Integer.parseInt(splitted[2]);
			TreeMap<Integer, Integer> timeMachineButtons = new TreeMap<Integer, Integer>(); 
			int[] uncompletedTasks = new int[N];
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			for (i = 0; i < N; i++) {
				uncompletedTasks[i] = Integer.parseInt(splitted[i]);
			}
			answer = 0;
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			for (i = 0; i < N; i++) {
				uncompletedTasks[i] -= Integer.parseInt(splitted[i]);
				answer += uncompletedTasks[i];
			}

			int buttonNumber;
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			for (i = 0; i < nWhiteButtons; i++) {
				buttonNumber = Integer.parseInt(splitted[i]);
				Integer freq = timeMachineButtons.get(buttonNumber);
				timeMachineButtons.put(buttonNumber, freq == null ? 1 : freq + 1); 
			}
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			for (i = 0; i < nBlackButtons; i++) {
				buttonNumber = Integer.parseInt(splitted[i]);
				Integer freq = timeMachineButtons.get(buttonNumber);
				timeMachineButtons.put(buttonNumber, freq == null ? 1 : freq + 1); 
			}
			
//			Arrays.sort(uncompletedTasks);
								
//			for (i = N - 1; i >= 0; i--) {
			for (i = 0; i < N; i++) {
				int d = uncompletedTasks[i];
				if (d > 0) {
					Entry<Integer, Integer> e = timeMachineButtons.floorEntry(d);
					if (e != null) {
						buttonNumber = e.getKey();
						answer -= buttonNumber;
						int freq = e.getValue() - 1;
						if (freq == 0) {
							timeMachineButtons.remove(buttonNumber);
						} else {
							timeMachineButtons.put(buttonNumber, freq);
						}
					}
				}
			}
			
			pw.printf("%d\n", answer);
		}
		pw.close();
	}
}
