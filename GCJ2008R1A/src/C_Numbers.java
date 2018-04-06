// ****************** I give up. It's true maths!.
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class C_Numbers {
	public static void main(String[] args) {
		CommonUtils.preamble();
		String inputFileName, outputFileName;

		if (args.length > 0) {
			inputFileName = args[0];
		} else {
//			inputFileName = "-sample1.in";
//			inputFileName = "-sample2.in";
//			inputFileName = "-small-practice.in";
			inputFileName = "-large-practice.in";
			inputFileName = C_Numbers.class.getSimpleName().substring(0, 1) + inputFileName;
		}
		
		if (args.length > 1) {
			outputFileName = args[1];
		} else {
			outputFileName = inputFileName.split("[.]in")[0].concat(".out");
		}

		System.out.printf("%s --> %s\n", inputFileName, outputFileName);
		
		File inputFile = new File(inputFileName);
		File outputFile = new File(outputFileName);
		
		try {
			PrintWriter outputWriter = new PrintWriter(outputFile, "ISO-8859-1");
			C_Numbers solver = new C_Numbers();
			solver.solve(inputFile, outputWriter);
			outputWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		CommonUtils.postamble();
	}

	public boolean solve(final File aFile, PrintWriter w) {
		String textLine;
		boolean result = false;
		BufferedReader br;
		int testCaseNumber = 0, testCaseCount;
//		String [] splitted;
		String answer;

		int n = 0;	 // # power
		BigDecimal c = new BigDecimal("3").add(bigSqrt(new BigDecimal("5")));
		final int MAX_POWER = 1000;
		BigDecimal iterC = c.pow(MAX_POWER);
//		final BigDecimal THOUSAND = new BigDecimal("1000");
		
		try {
			br = new BufferedReader(new FileReader(aFile));
			textLine = br.readLine();
			testCaseCount = Integer.parseInt(textLine);
			
			while (++testCaseNumber <= testCaseCount && (textLine = br.readLine()) != null) {
/////////////////////////////////////////////////////////////////////////////////////
				n = Integer.parseInt(textLine); 
				//double d = Math.pow(3 + Math.sqrt(5), n) / 1000.0;
//				BigDecimal d = (new BigDecimal("5.23606797749979")).pow(n);
//				answer = String.format("%03d", d.toBigInteger().mod(new BigInteger("1000")).intValue());

				BigDecimal d = BigDecimal.ONE;
				int i = n;
				while (i > MAX_POWER) {
					d = d.multiply(iterC);
					i -= MAX_POWER;
				}
				d = d.multiply(c.pow(i));
				
//				if (d.compareTo(THOUSAND) >= 0) {
//					d = d.subtract(d.divideToIntegralValue(THOUSAND).multiply(THOUSAND));
//				}

				String bs = "00" + d.toPlainString();
				int p = bs.indexOf('.'); 
				answer = bs.substring(p - 3, p);

				w.printf("Case #%d: %s\n", testCaseNumber, answer);
				System.out.printf("Case #%d: n=%d, answer=%s\n", testCaseNumber, n, CommonUtils.showString(answer, 100));
////////////////////////////////////////////////////////////////////////////////////
				
			}

			br.close();
			result = true;
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	private static final int SQRT_DIG = 150;
	private static final BigDecimal SQRT_PRE = BigDecimal.TEN.pow(SQRT_DIG);

	/**
	 * Private utility method used to compute the square root of a BigDecimal.
	 * 
	 * @author Luciano Culacciatti 
	 * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
	 */
	private static BigDecimal sqrtNewtonRaphson  (BigDecimal c, BigDecimal xn, BigDecimal precision){
	    BigDecimal fx = xn.pow(2).add(c.negate());
	    BigDecimal fpx = xn.multiply(new BigDecimal(2));
	    BigDecimal xn1 = fx.divide(fpx,2*SQRT_DIG,RoundingMode.HALF_DOWN);
	    xn1 = xn.add(xn1.negate());
	    BigDecimal currentSquare = xn1.pow(2);
	    BigDecimal currentPrecision = currentSquare.subtract(c);
	    currentPrecision = currentPrecision.abs();
	    if (currentPrecision.compareTo(precision) <= -1){
	        return xn1;
	    }
	    return sqrtNewtonRaphson(c, xn1, precision);
	}

	/**
	 * Uses Newton Raphson to compute the square root of a BigDecimal.
	 * 
	 * @author Luciano Culacciatti 
	 * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
	 */
	public static BigDecimal bigSqrt(BigDecimal c){
	    return sqrtNewtonRaphson(c,new BigDecimal(1),new BigDecimal(1).divide(SQRT_PRE));
	}	
}