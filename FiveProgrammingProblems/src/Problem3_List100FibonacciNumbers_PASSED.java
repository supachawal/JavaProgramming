import java.math.BigInteger;


public class Problem3_List100FibonacciNumbers_PASSED {

	public static void main(String[] args) {
		System.out.printf("0, 1");

		BigInteger[] num = {BigInteger.ZERO, BigInteger.ONE};
		int currentIndex = 0;
		
		for (int i = 97; i >= 0; i--) {
			num[currentIndex] = num[currentIndex].add(num[currentIndex ^ 1]);
			System.out.printf(", %s", num[currentIndex]);
			currentIndex ^= 1;
		}
	}
}
