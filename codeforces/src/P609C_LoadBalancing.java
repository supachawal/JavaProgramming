import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.PriorityQueue;

public class P609C_LoadBalancing {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		String textLine;
		String[] splitted;
		int i;
		long answer;

		int n;
		long sum, avg;
		int moves;
		Integer mi, min, max;

		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		n = Integer.parseInt(splitted[0]);

		PriorityQueue<Integer> minHeap = new PriorityQueue<Integer>(n);
		PriorityQueue<Integer> maxHeap = new PriorityQueue<Integer>(n, (o1, o2) -> o2.compareTo(o1));

		sum = 0;
		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		for (i = 0; i < n; i++) {
			mi = Integer.valueOf(splitted[i]);
			minHeap.add(mi);
			maxHeap.add(mi);
			sum += mi.intValue();
		}
		avg = sum / n;
		answer = 0;
		min = minHeap.poll();
		max = maxHeap.poll();
		while (max - min > 1){
			moves = (int)Math.min(avg - min, max - avg);
			answer += moves;
			min += moves;
			max -= moves;
			minHeap.add(min);
			maxHeap.add(max);
			
			min = minHeap.poll();
			max = maxHeap.poll();
		}

		pw.printf("%d", answer);
		pw.close();
	}
}
