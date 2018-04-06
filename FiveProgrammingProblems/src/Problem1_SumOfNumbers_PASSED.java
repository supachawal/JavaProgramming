
public class Problem1_SumOfNumbers_PASSED {

	public static void main(String[] args) {
		double [] numbers = {5, 10, 11.1, 20.1};
		
		System.out.printf("sum using for loop = %f\n", sumUsingForLoop(numbers));
		System.out.printf("sum using while loop = %f\n", sumUsingWhileLoop(numbers));
		System.out.printf("sum using recursion = %f\n", sumUsingRecursion(numbers, numbers.length));

	}

	public static double sumUsingForLoop(double [] numbers) {
		double sum = 0.0;
		for (double a : numbers) {
			sum += a;
		}
		return sum;
	}

	public static double sumUsingWhileLoop(double [] numbers) {
		double sum = 0.0;
		int i = numbers.length - 1;
		while (i >= 0) {
			sum += numbers[i--];
		}
		return sum;
	}

	public static double sumUsingRecursion(double [] numbers, int n) {
		return (n == 0 ? 0 : numbers[n - 1] + sumUsingRecursion(numbers, n - 1));
	}
}
