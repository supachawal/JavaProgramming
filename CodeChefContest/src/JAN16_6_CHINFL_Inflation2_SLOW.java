import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;

class JAN16_6_CHINFL_Inflation2_SLOW {
/* Example
	  
Input:
3 3 5
2 1 5 3 7 6
2 1 4 3 6 5
10 9 8 7 6 5

Output: 15.0000000000
*/
	private static PrintWriter pw;
	private static int N; // # of exchange machine
	private static int M; // time periods
	private static BigDecimal[][][] memoization;
	private static BigDecimal[][][] exchangeRateLog;
	private static BigDecimal shortcutBound;
	private static BigDecimal shortcutResult;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);

		String textLine;
		String[] splitted;
		BigDecimal answer;

		BigDecimal D; // initial money
		int i, j;
		final int scale = 20;

		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		N = Integer.parseInt(splitted[0]);
		M = Integer.parseInt(splitted[1]);
		D = new BigDecimal(splitted[2]);
		exchangeRateLog = new BigDecimal[N][M][2];
		memoization = new BigDecimal[N][M][2];
		for (i = 0; i < N; i++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			for (j = 0; j < M; j++) {
				exchangeRateLog[i][j][0] = ln(new BigDecimal(splitted[2 * j]), scale).negate();
				exchangeRateLog[i][j][1] = ln(new BigDecimal(splitted[2 * j + 1]), scale);
			}
		}

		BigDecimal maxProfit = BigDecimal.ZERO, profit;
		BigDecimal quintillionnairePoint = BigDecimal.valueOf(1e18);
		shortcutBound = ln(quintillionnairePoint.divide(D), scale);
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
			answer = D.multiply(exp(maxProfit, scale));
			pw.printf("%.10f\n", answer.doubleValue());
		}
		pw.close();
	}

	private static BigDecimal maximizedProfit(int exchangeKiosk, int period, int buying) {
		if (period >= M) {
			return BigDecimal.ZERO;
		}
		if (shortcutResult != null) {
			return shortcutResult;
		}
		if (memoization[exchangeKiosk][period][buying] != null) {
			return memoization[exchangeKiosk][period][buying];
		}

		BigDecimal resultFinal = BigDecimal.ZERO;
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

		result = exchangeRateLog[exchangeKiosk][period][buying].add(maximizedProfit(exchangeKiosk, period + 1, buying ^ 1));
		if (result.compareTo(resultFinal) > 0) {
			resultFinal = result;
		}

		memoization[exchangeKiosk][period][buying] = resultFinal;
		if (resultFinal.compareTo(shortcutBound) > 0) {
			shortcutResult = resultFinal;
		}
		return resultFinal;
	}

	/**
	 * Compute the natural logarithm of x to a given scale, x > 0.
	 */
	public static BigDecimal ln(BigDecimal x, int scale) {
		// Check that x > 0.
		if (x.signum() <= 0) {
			throw new IllegalArgumentException("x <= 0");
		}

		// The number of digits to the left of the decimal point.
		int magnitude = x.toString().length() - x.scale() - 1;

		if (magnitude < 3) {
			return lnNewton(x, scale);
		}

		// Compute magnitude*ln(x^(1/magnitude)).
		// x^(1/magnitude)
		BigDecimal root = intRoot(x, magnitude, scale);

		// ln(x^(1/magnitude))
		BigDecimal lnRoot = lnNewton(root, scale);

		// magnitude*ln(x^(1/magnitude))
		return BigDecimal.valueOf(magnitude).multiply(lnRoot).setScale(scale, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * Compute the natural logarithm of x to a given scale, x > 0. Use Newton's
	 * algorithm.
	 */
	private static BigDecimal lnNewton(BigDecimal x, int scale) {
		int sp1 = scale + 1;
		BigDecimal n = x;
		BigDecimal term;

		// Convergence tolerance = 5*(10^-(scale+1))
		BigDecimal tolerance = BigDecimal.valueOf(5).movePointLeft(sp1);

		// Loop until the approximations converge
		// (two successive approximations are within the tolerance).
		do {

			// e^x
			BigDecimal eToX = exp(x, sp1);

			// (e^x - n)/e^x
			term = eToX.subtract(n).divide(eToX, sp1, BigDecimal.ROUND_DOWN);

			// x - (e^x - n)/e^x
			x = x.subtract(term);

			Thread.yield();
		} while (term.compareTo(tolerance) > 0);

		return x.setScale(scale, BigDecimal.ROUND_HALF_EVEN);
	}

	private static BigDecimal intRoot(BigDecimal x, long index, int scale) {
		// Check that x >= 0.
		if (x.signum() < 0) {
			throw new IllegalArgumentException("x < 0");
		}

		int sp1 = scale + 1;
		BigDecimal n = x;
		BigDecimal i = BigDecimal.valueOf(index);
		BigDecimal im1 = BigDecimal.valueOf(index - 1);
		BigDecimal tolerance = BigDecimal.valueOf(5).movePointLeft(sp1);
		BigDecimal xPrev;

		// The initial approximation is x/index.
		x = x.divide(i, scale, BigDecimal.ROUND_HALF_EVEN);

		// Loop until the approximations converge
		// (two successive approximations are equal after rounding).
		do {
			// x^(index-1)
			BigDecimal xToIm1 = intPower(x, index - 1, sp1);

			// x^index
			BigDecimal xToI = x.multiply(xToIm1).setScale(sp1, BigDecimal.ROUND_HALF_EVEN);

			// n + (index-1)*(x^index)
			BigDecimal numerator = n.add(im1.multiply(xToI)).setScale(sp1, BigDecimal.ROUND_HALF_EVEN);

			// (index*(x^(index-1))
			BigDecimal denominator = i.multiply(xToIm1).setScale(sp1, BigDecimal.ROUND_HALF_EVEN);

			// x = (n + (index-1)*(x^index)) / (index*(x^(index-1)))
			xPrev = x;
			x = numerator.divide(denominator, sp1, BigDecimal.ROUND_DOWN);

			Thread.yield();
		} while (x.subtract(xPrev).abs().compareTo(tolerance) > 0);

		return x;
	}

	private static BigDecimal intPower(BigDecimal x, long exponent, int scale) {
		// If the exponent is negative, compute 1/(x^-exponent).
		if (exponent < 0) {
			return BigDecimal.ONE.divide(intPower(x, -exponent, scale), scale, BigDecimal.ROUND_HALF_EVEN);
		}

		BigDecimal power = BigDecimal.ONE;

		// Loop to compute value^exponent.
		while (exponent > 0) {

			// Is the rightmost bit a 1?
			if ((exponent & 1) == 1) {
				power = power.multiply(x).setScale(scale, BigDecimal.ROUND_HALF_EVEN);
			}

			// Square x and shift exponent 1 bit to the right.
			x = x.multiply(x).setScale(scale, BigDecimal.ROUND_HALF_EVEN);
			exponent >>= 1;

			Thread.yield();
		}

		return power;
	}

	private static BigDecimal exp(BigDecimal x, int scale) {
		// e^0 = 1
		if (x.signum() == 0) {
			return BigDecimal.ONE;
		}

		// If x is negative, return 1/(e^-x).
		else if (x.signum() == -1) {
			return BigDecimal.ONE.divide(exp(x.negate(), scale), scale, BigDecimal.ROUND_HALF_EVEN);
		}

		// Compute the whole part of x.
		BigDecimal xWhole = x.setScale(0, BigDecimal.ROUND_DOWN);

		// If there isn't a whole part, compute and return e^x.
		if (xWhole.signum() == 0)
			return expTaylor(x, scale);

		// Compute the fraction part of x.
		BigDecimal xFraction = x.subtract(xWhole);

		// z = 1 + fraction/whole
		BigDecimal z = BigDecimal.ONE.add(xFraction.divide(xWhole, scale, BigDecimal.ROUND_HALF_EVEN));

		// t = e^z
		BigDecimal t = expTaylor(z, scale);

		BigDecimal maxLong = BigDecimal.valueOf(Long.MAX_VALUE);
		BigDecimal result = BigDecimal.ONE;

		// Compute and return t^whole using intPower().
		// If whole > Long.MAX_VALUE, then first compute products
		// of e^Long.MAX_VALUE.
		while (xWhole.compareTo(maxLong) >= 0) {
			result = result.multiply(intPower(t, Long.MAX_VALUE, scale)).setScale(scale, BigDecimal.ROUND_HALF_EVEN);
			xWhole = xWhole.subtract(maxLong);

			Thread.yield();
		}
		return result.multiply(intPower(t, xWhole.longValue(), scale)).setScale(scale, BigDecimal.ROUND_HALF_EVEN);
	}

	private static BigDecimal expTaylor(BigDecimal x, int scale) {
		BigDecimal factorial = BigDecimal.ONE;
		BigDecimal xPower = x;
		BigDecimal sumPrev;

		// 1 + x
		BigDecimal sum = x.add(BigDecimal.ONE);

		// Loop until the sums converge
		// (two successive sums are equal after rounding).
		int i = 2;
		do {
			// x^i
			xPower = xPower.multiply(x).setScale(scale, BigDecimal.ROUND_HALF_EVEN);

			// i!
			factorial = factorial.multiply(BigDecimal.valueOf(i));

			// x^i/i!
			BigDecimal term = xPower.divide(factorial, scale, BigDecimal.ROUND_HALF_EVEN);

			// sum = sum + x^i/i!
			sumPrev = sum;
			sum = sum.add(term);

			++i;
			Thread.yield();
		} while (sum.compareTo(sumPrev) != 0);

		return sum;
	}

}
