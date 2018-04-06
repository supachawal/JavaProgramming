import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class P702D_Road_to_PostOffice {
	private static BufferedReader br;
	private static PrintWriter pw;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String[] splitted = br.readLine().split("\\s+");
		long d = Long.parseLong(splitted[0]);// the distance from home to the post office;
		long k = Long.parseLong(splitted[1]);// the distance, which car is able to drive before breaking;
		long a = Long.parseLong(splitted[2]);// the time, which Vasiliy spends to drive 1 kilometer on his car;
		long b = Long.parseLong(splitted[3]);// the time, which Vasiliy spends to walk 1 kilometer on foot;
		long t = Long.parseLong(splitted[4]);// the time, which Vasiliy spends to repair his car.
		long answer = 0;
		
		if (d <= k) {
			answer = d * a;
		} else {
			long timeByCar = k * a + t;
			long timeOnFoot = k * b;

			if (timeOnFoot <= timeByCar) {
				answer = k * a + (d - k) * b;
			} else {
				long totalTimeByCar = d / k * timeByCar;
				long remainingDistance = d % k;
				// choice 1: repair and drive to finish
				long choice1 = totalTimeByCar + remainingDistance * a;
				if (remainingDistance == 0) {
					choice1 -= t;
				}
				// choice 2: not repair and go on foot to finish
				long choice2 = totalTimeByCar - t + remainingDistance * b;
				answer = Math.min(choice1, choice2);
			}
		}
		
		pw.printf("%d\n", answer);
		pw.close();
	}
}