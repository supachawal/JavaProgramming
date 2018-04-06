import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class CommonUtils {
	public static final int INTEGER_INFINITY = Integer.MAX_VALUE >>> 4; 
	public static final long LONG_INFINITY = Long.MAX_VALUE >>> 8; 
	public static final BigInteger BIGINT_TWO = new BigInteger("2"); 
	public static final BigInteger BIGINT_THREE = new BigInteger("3"); 
	public static final BigInteger BIGINT_FIVE = new BigInteger("5"); 
	public static final BigInteger BIGINT_SEVEN = new BigInteger("7"); 

	public static void main(String[] args) {
		//System.out.println(genIndexCombination(6, 2));
		//System.out.printf("%s removed at bit 0 = %s", Long.toBinaryString(3), Long.toBinaryString(removeBit(3, 0)));
		
		for (int i = 8; i > -8; i--) {
			System.out.printf("%d cyclic to %d, i %% 4 = %d\n", i, positiveMod(i, 4), i % 4);
		}
		
//		int d = 359/360;
//		System.out.printf("%d", d);
		
	}
	static long startTimeMilliseconds;
	public static void preamble() {
		startTimeMilliseconds = System.currentTimeMillis();
		Runtime r = Runtime.getRuntime();
		r.gc();
		System.out.printf("=============== start program (FREEMEM=%.0fm)===============\n", r.freeMemory()/1024.0/1024.0);
	}
	
	public static void postamble() {
		Runtime r = Runtime.getRuntime();
		System.out.printf("=============== end program (totalTime = %d ms. FREEMEM = %.0fm before GC, "
				, System.currentTimeMillis() - startTimeMilliseconds
				, r.freeMemory()/1024.0/1024.0);
		r.gc();
		System.out.printf("%.0fm after GC)===============\n", r.freeMemory()/1024.0/1024.0);
	}

	public static String showString(String s, int limit) {
		if (s.length() > limit) {
			return s.substring(0, limit - 3) + "...";
		}
		
		return s;
	}

	public static String showTimetable(Iterator<IntPair> Ai, int limit) {
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		boolean usedTo = false;
		while (Ai.hasNext() && limit-- > 0) {
			IntPair t = Ai.next();
			int from = t.v1;
			int to = t.v2;
			
			if (usedTo) {
				sb.append(",");
			} else {
				usedTo = true;
			}
			
			sb.append(String.format("(%02d:%02d~%02d:%02d)", from / 60, from % 60, to / 60, to % 60));
		}
		
		if (Ai.hasNext()) {
			sb.append(", ...");
		}
		sb.append(">");
		
		return sb.toString();
	}

	public static final double vectorNorm(double [] p, int n) {
		double sumSq = 0;
		for (int i = 0; i < n; i++) {
			sumSq += p[i] * p[i];
		}
		return Math.sqrt(sumSq);
	}
	
	public static final double vectorDotProduct(double [] a, double [] b, int n) {
		double sum = 0;
		for (int i = 0; i < n; i++) {
			sum += a[i] * b[i];
		}
		return sum;
	}

	public static final double [] vectorCrossProduct3D(double [] u, double [] v) {
		return new double[] {
				u[1]*v[2] - u[2]*v[1]
			  , u[2]*v[0] - u[0]*v[2]
			  , u[0]*v[1] - u[1]*v[0]
		};
	}
	
	public static final double [] vectorAdd(double [] a, double [] b, int n) {
		double [] ret = new double[n];
		for (int i = 0; i < n; i++) {
			ret[i] = a[i] + b[i];
		}
		return ret;
	}

	public static final double [] vectorScale(double [] a, int n, double s) {
		double [] ret = new double[n];
		for (int i = 0; i < n; i++) {
			ret[i] = s * a[i];
		}
		return ret;
	}
	
	public static final double [] vectorCreate(double [] start, double [] end, int n) {
		double [] ret = new double[n];
		for (int i = 0; i < n; i++) {
			ret[i] = end[i] - start[i];
		}
		return ret;
	}
	
	public static List<TreeSet<Integer>> genIndexCombination(int n, int r) {
		if (n < r) {
			return null;
		}
		
		List<TreeSet<Integer>> result = new ArrayList<TreeSet<Integer>>();
		result.add(new TreeSet<Integer>());
		int currentSize = 0;

		while (currentSize < r) {
			currentSize++;
			List<TreeSet<Integer>> newResult = new ArrayList<TreeSet<Integer>>(currentSize * n);
			for (TreeSet<Integer> t : result) {
				for (int i = 0; i < n; i++) {
					if (!t.contains(i) && t.higher(i) == null) {
						TreeSet<Integer> combi = new TreeSet<Integer>();
						combi.addAll(t);
						combi.add(i);
						newResult.add(combi);
					}
				}
			}
			
			result = newResult;
		}
		
		return result;
	}
	
//	def GCD(a, b):
//	    """ The Euclidean Algorithm """
//	    a = abs(a)
//	    b = abs(b)
//	    while a != 0:
//	        a, b = b%a, a
//	    return b

	public static long GCD(long a, long b) {
		while (a != 0) {
			long mod = b % a;
			b = a;
			a = mod;
		}
		return b;
	}
	
	public static long removeBit(long v, int i) {
		return (v >>> (i + 1) << i) | (v & (1 << i) - 1); 
	}

	public static void copy2dArray(long[][] src, long[][] dest) {
		int m = dest.length;
		int n = dest[0].length;
		
		
		for (int i = 0; i < m; i++) {
			System.arraycopy(src[i], 0, dest[i], 0, n);
//			for (int j = 0; j < n; j++) {
//				dest[i][j] = src[i][j];
//			}
		}
	}

	public static long positiveMod(long dividend, long divisor) {
		long result = dividend % divisor;
		
		if (result < 0) {
			result += divisor;
		}
		
		return result; 
	}

	public static int positiveMod(int dividend, int divisor) {
		int result = dividend % divisor;
		
		if (result < 0) {
			result += divisor;
		}
		
		return result; 
	}
	
	public static long fastCombinationCount(int n, int r, int mod) { // Cnr
		int[][] pascalT = new int[2][n + 1];
		int result;
		int row = 1;

		// build Pascal's Triangle 0..n lines using 2 lines alternatively
		for (int i = 0; i <= n; i++) {
			row ^= 1;
			pascalT[row][0] = 1;
			pascalT[row][i] = 1;
			for (int j = 1; j < i; j++) {
				int prevRow = row ^ 1;
				pascalT[row][j] = (int)(((long)pascalT[prevRow][j - 1] + pascalT[prevRow][j]) % mod);
			}
		}
		result = pascalT[row][r];
		return result;
	}
}