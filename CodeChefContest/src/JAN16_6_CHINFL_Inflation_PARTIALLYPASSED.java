import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class JAN16_6_CHINFL_Inflation_PARTIALLYPASSED {
/*
Example

Input:
3 3 5
2 1 5 3 7 6
2 1 4 3 6 5
10 9 8 7 6 5

Output:
15.0000000000
*/
	private static PrintWriter pw;
	private static double[][][] exchangeRateLog;
	private static double[][][] memoization;
	private static int N;	//# of exchange machine
	private static int M;	//time periods
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);

		String textLine;
		String[] splitted;
		double answer;
		
		int D;	//initial money
		int i, j;
		
		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		N = Integer.parseInt(splitted[0]);
		M = Integer.parseInt(splitted[1]);
		D = Integer.parseInt(splitted[2]);
		exchangeRateLog = new double[N][M][2];
		for (i = 0; i < N; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			for (j = 0; j < M; j++) {
				exchangeRateLog[i][j][0] = -Math.log(Double.parseDouble(splitted[2 * j]));
				exchangeRateLog[i][j][1] = Math.log(Double.parseDouble(splitted[2 * j + 1]));
			}
		}
		
		double maxProfit = 0, profit;
		memoization = new double[N][M][2];
//		double quintillionnairePoint = Math.log(1e18/D);
		for (i = 0; i < N; i++) {
			profit = maximizedProfit(i, 0, 0);
			if (profit > maxProfit) {
				maxProfit = profit;
			}
		}
		
		answer = D * Math.exp(maxProfit);
		
		if (answer > 1e18)
			pw.println("Quintillionnaire");
		else
			pw.printf("%.10f\n", answer);
		pw.close();
	}
	
	private static double maximizedProfit(int exchangeKiosk, int period, int buying) {
		if (period >= M) {
			return 0.0;
		}
		if (memoization[exchangeKiosk][period][buying] > 0.0) {
			return memoization[exchangeKiosk][period][buying];
		}

		double result = 0.0;
		
		if (exchangeKiosk > 0) {
			result = Math.max(result, maximizedProfit(exchangeKiosk - 1, period + 1, buying));
		}
		result = Math.max(result, maximizedProfit(exchangeKiosk, period + 1, buying));
		if (exchangeKiosk < N - 1) {
			result = Math.max(result, maximizedProfit(exchangeKiosk + 1, period + 1, buying));
		}
		
		result = Math.max(result, exchangeRateLog[exchangeKiosk][period][buying] + maximizedProfit(exchangeKiosk, period + 1, buying ^ 1));

		memoization[exchangeKiosk][period][buying] = result;
		
		return result;
	}
}
