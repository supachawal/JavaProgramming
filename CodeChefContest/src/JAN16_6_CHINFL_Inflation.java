import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

class JAN16_6_CHINFL_Inflation {
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
	private static int N;	//# of exchange machine
	private static int M;	//time periods
	private static BigDecimal[][][] memoization;
	private static BigDecimal[][][] exchangeRate;
	private static BigDecimal shortcutBound;
	private static BigDecimal shortcutResult;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);

		String textLine;
		String[] splitted;
		BigDecimal answer;
		
		BigDecimal D;	//initial money
		int i, j;
		
		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		N = Integer.parseInt(splitted[0]);
		M = Integer.parseInt(splitted[1]);
		D = new BigDecimal(splitted[2]);
		exchangeRate = new BigDecimal[N][M][2];
		memoization = new BigDecimal[N][M][2];
		MathContext selectiveMC = new MathContext(19, RoundingMode.HALF_UP);
		for (i = 0; i < N; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			for (j = 0; j < M; j++) {
				exchangeRate[i][j][0] = BigDecimal.ONE.divide(new BigDecimal(splitted[2 * j]), selectiveMC);
				exchangeRate[i][j][1] = new BigDecimal(splitted[2 * j + 1], selectiveMC);
			}
		}
		
		BigDecimal maxProfit = BigDecimal.ZERO, profit;
		BigDecimal quintillionnairePoint = BigDecimal.valueOf(1e18); 
		shortcutBound = quintillionnairePoint.divide(D, selectiveMC);
		shortcutResult = null;
		for (i = 0; i < N; i++) {
			profit = maximizedProfit(i, 0, 0);
			if (shortcutResult != null) {
				break;
			}
			if (profit.compareTo(maxProfit) > 0) {
				maxProfit = profit;
			}
		}
		
		if (shortcutResult != null) {
			pw.println("Quintillionnaire");
		} else {
			answer = maxProfit.multiply(D);
			pw.printf("%.10f\n", answer.doubleValue());
		}
		pw.close();
	}

	private static BigDecimal maximizedProfit(int exchangeKiosk, int period, int buying) {
		if (period >= M) {
			return BigDecimal.ONE;
		}
		if (shortcutResult != null) {
			return shortcutResult;
		}
		if (memoization[exchangeKiosk][period][buying] != null) {
			return memoization[exchangeKiosk][period][buying];
		}

		BigDecimal resultFinal = BigDecimal.ONE;
		BigDecimal result;
		
		if (exchangeKiosk > 0) {
			result = maximizedProfit(exchangeKiosk - 1, period + 1, buying);
			if (result.compareTo(resultFinal) > 0) {
				resultFinal = result;
			}
		}
		
		result = maximizedProfit(exchangeKiosk, period + 1, buying);
		if (result.compareTo(resultFinal) > 0) {
			resultFinal = result;
		}

		if (exchangeKiosk < N - 1) {
			result = maximizedProfit(exchangeKiosk + 1, period + 1, buying);
			if (result.compareTo(resultFinal) > 0) {
				resultFinal = result;
			}
		}
		
		result = exchangeRate[exchangeKiosk][period][buying].multiply(maximizedProfit(exchangeKiosk, period + 1, buying ^ 1));
		if (result.compareTo(resultFinal) > 0) {
			resultFinal = result;
		}

		memoization[exchangeKiosk][period][buying] = resultFinal;
		if (resultFinal.compareTo(shortcutBound) > 0) {
			shortcutResult = resultFinal;
		}
		return resultFinal;
	}
}
