public class Quaternion {
	public static void main(String[] args) {
//		Quaternion q = new Quaternion(9, 200, "kjikjikjikjikjikjikjikjikji");
		Quaternion q = new Quaternion(12, 200, "ijjkkkijjkkk");		
		for (int i = 0; i < 10; i++) {
			System.out.printf("%s, ", q.getChar(i));
		}
		
		System.out.printf("\n");
		q.test();
//		System.out.printf("Multiply result: %s\n", intToChar(q.multiplyIncremental(I, 0)));
	}

	public static final int I = 2, J = 3, K = 4;
	private int inputLength;
	private int [] inputInts;
	public long maxVirtualLength;
	private int blockValue;
	private boolean isHomogeneous = true;

	public Quaternion(final int inputLength, final long repeatTimes, final String inputChars) {
		String s = inputChars;
		if (s.length() > inputLength) {
			s = s.substring(0, inputLength);
		}

		s = reducedPattern(s);
		int n = s.length();
		this.inputLength = n;
		inputInts = new int[n];
		
		int i = 0;
		char [] C = s.toCharArray();
		for (i = 0; i < n; i++) {
			inputInts[i] = C[i] - 'i' + I;
			
			if (isHomogeneous && i > 0 && C[i] != C[i - 1])
				isHomogeneous = false;
		}
		
		maxVirtualLength = inputLength * repeatTimes;  
		int v = 1;
		for (i = 0; i < n; i++) {
			v = multiplyIncremental(v, i);
		}
		blockValue = v;
	}

	public void test() {
		int v = 1;
		System.out.printf("Multiply repeated block: ");
		for (int i = 0; i < 8 * inputLength; i++) {
			v = multiplyIncremental(v, i);
			System.out.printf("%s ", intToChar(v));
			if ((i + 1) % inputLength == 0) {
				System.out.printf(" ");
			}
		}
		System.out.printf("\n");
	}
	
	public static String reducedPattern(String s) {
		int n = s.length();
		int lastIndex = n >> 1;
		for (int i = 1; i <= lastIndex; i++) {
			String v = s.substring(0, i);
			if (s.replace(v, "").length() == 0) {
				System.out.printf("reduced to {%s} (%d times), ", v, n / v.length());
				return v;
			}
		}
		
		return s;
	}

	public static final String intToChar(int value) {
		return value == -1 ? "-1" : (value == 1 ? "1" : ((value < 0 ? "-" : "") + (char)((char)(Math.abs(value) - I) + 'i')));
	}

	public final int getInt(long virtualIndex) {
		return inputInts[(int)(virtualIndex % inputLength)];
	}

	public final String getChar(long virtualIndex) {
		return intToChar(getInt(virtualIndex));
	}

	private static final int [][] _formulaMap = new int[][]
			{
				{}
				//        1   I   J   K
				/*1*/,{0, 1,  I,  J,  K}
				/*I*/,{0, I, -1,  K, -J}
				/*J*/,{0, J, -K, -1,  I}
				/*K*/,{0, K,  J, -I, -1}
			};
	
	public final int multiplyIncremental(int operand1, long virtualIndex) {
		int sign = 1;

		if (operand1 < 0) {
			sign = -sign;
			operand1 = -operand1;
		}

		int operand2 = inputInts[(int)(virtualIndex % inputLength)];//TUNED: getInt(virtualIndex);
		if (operand2 < 0) {
			sign = -sign;
			operand2 = -operand2;
		}
		
		return sign * _formulaMap[operand1][operand2];
	}
	
	public final boolean canBeReducedToIJK() {
		if (isHomogeneous) {
			return false;
		}

		int singleBlockSize = inputLength;
		int doubleBlockSize = singleBlockSize * 2;
		int tripleBlockSize = singleBlockSize * 3;
		int quartBlockSize = singleBlockSize * 4;
		int thisBlockValue = blockValue;
		long scope = quartBlockSize - 1;
		long virtualLastIndex = maxVirtualLength - 1;
		int v1 = 1;
		long iBound = Math.min(virtualLastIndex - 2, scope);

		for (long i = 0; i <= iBound; i++) {
			if ((v1 = multiplyIncremental(v1, i)) == I) {
				int v3 = 1;
				long kBound = i + 2 * scope;

				if (kBound > virtualLastIndex)
					kBound = virtualLastIndex;

				for (long k = i + 1; k <= kBound; k++) {
					v3 = multiplyIncremental(v3, k);
					if (v3 == I || v3 == -I) {
						
						int remainder = (int)((virtualLastIndex - k) % quartBlockSize);
						
						if (v3 == I && (remainder == 0 || thisBlockValue == 1 && (remainder == singleBlockSize || remainder == doubleBlockSize || remainder == tripleBlockSize) )
							|| v3 == -I && (thisBlockValue != 1 && thisBlockValue != -1 && remainder == doubleBlockSize
											|| thisBlockValue == -1 && (remainder == singleBlockSize || remainder == tripleBlockSize)
										   )
							) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}

	public final boolean canBeReducedToIJK_Slow() {
		long quartBlockSize = inputLength * 4;
		long scope = quartBlockSize - 1;
		long virtualLastIndex = maxVirtualLength - 1;
		int v1 = 1;
		long iBound = Math.min(virtualLastIndex, scope);

		for (long i = 0; i <= iBound; i++) {
			if ((v1 = multiplyIncremental(v1, i)) == I) {
				int v2 = 1;
				long jBound = i + scope;

				if (jBound > virtualLastIndex)
					jBound = virtualLastIndex;

				for (long j = i + 1; j <= jBound; j++) {
					if ((v2 = multiplyIncremental(v2, j)) == J) {
						int v3 = 1;
						long kBound = j + scope;

						if (kBound > virtualLastIndex)
							kBound = virtualLastIndex;

						for (long k = j + 1; k <= kBound; k++) {
							v3 = multiplyIncremental(v3, k);
							
							if (v3 == K && (virtualLastIndex - k) % quartBlockSize == 0) {
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
}
