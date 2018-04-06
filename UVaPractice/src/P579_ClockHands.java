import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class P579_ClockHands {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
//		int testCaseNumber = 0;
		String textLine;
		String[] splitted;
		long answer;

		long totalMinutes;
		long degreeShortHand1000;
		long degreeLongHand1000;
		final long minutesPerShortHandRound = 12 * 60;
		final long minutesPerLongHandRound = 60;
		while ((textLine = br.readLine()) != null) {
			if (textLine.equals("0:00"))
				break;
			
			splitted = textLine.split(":");
			totalMinutes = Integer.parseInt(splitted[0]) * 60 + Integer.parseInt(splitted[1]);
			
			degreeShortHand1000 = (360000 * (totalMinutes % minutesPerShortHandRound) + minutesPerShortHandRound / 2) / minutesPerShortHandRound;
			degreeLongHand1000 = (360000 * (totalMinutes % minutesPerLongHandRound) + minutesPerLongHandRound / 2) / minutesPerLongHandRound;
			
			answer = Math.abs(degreeLongHand1000 - degreeShortHand1000);
			if (answer > 180000) {
				answer = 360000 - answer;
			}
			
			pw.printf("%.3f\n", answer / 1000.0);
		}
		
		pw.close();
	}
}
