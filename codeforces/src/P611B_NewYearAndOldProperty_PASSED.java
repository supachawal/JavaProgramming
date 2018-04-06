import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class P611B_NewYearAndOldProperty_PASSED {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		String textLine;
		String[] splitted;
		int answer = 0;

		long a, b;
		long x;
		
		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		a = Long.parseLong(splitted[0]);
		b = Long.parseLong(splitted[1]);
		
		SpecialIterator it = new SpecialIterator();
		for (x = it.iterateNext(); x <= b; x = it.iterateNext()) {
//			pw.printf("%s\n", Integer.toBinaryString(x));

			if (x >= a) {
				answer++;
			}
		}
		
		pw.printf("%d", answer);
		pw.close();
	}
	
	public static class SpecialIterator {
		int zeroBit;
		int highestBit;
		public SpecialIterator() {
			zeroBit = 0;
			highestBit = 0;
		}
		public long iterateNext() {
			long ret = ((1L << (highestBit + 1)) - 1) & ~(1L << zeroBit);

			if (zeroBit == 0) {
				zeroBit = highestBit;
				highestBit++;
			} else {
				zeroBit--;
			}
			
			return ret;
		}
	}
}
