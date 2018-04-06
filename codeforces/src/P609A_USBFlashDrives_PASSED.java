import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.PriorityQueue;

public class P609A_USBFlashDrives_PASSED {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		String textLine;
		// String[] splitted;
		int i;
		int answer;

		int n;
		int m;
		PriorityQueue<Integer> maxHeap;

		textLine = br.readLine();
		n = Integer.parseInt(textLine);
		textLine = br.readLine();
		m = Integer.parseInt(textLine);

		maxHeap = new PriorityQueue<Integer>(n, (o1, o2) -> o2.compareTo(o1));

		// splitted = textLine.split("\\s+");
		for (i = 0; i < n; i++) {
			textLine = br.readLine();
			maxHeap.add(Integer.parseInt(textLine));
		}

		answer = 0;
		while (m > 0) {
			answer++;
			m -= maxHeap.poll().intValue();
		}

		pw.printf("%d", answer);
		pw.close();
	}
}
