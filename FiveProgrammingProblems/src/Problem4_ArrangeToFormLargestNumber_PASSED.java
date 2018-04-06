import java.util.ArrayList;

public class Problem4_ArrangeToFormLargestNumber_PASSED {

	public static void main(String[] args) {
//		int [] numbers = {50, 55, 56999, 569, 5692, 563, 18, 19, 90, 9, 9};
		int [] numbers = {50, 53, 53999, 539, 5392, 533, 18, 19, 90, 9, 9};
		System.out.printf("Largest number: %s", arrangeToFormLargestNumber(numbers));
	}
	
	public static String arrangeToFormLargestNumber(int [] numbers) {
		ArrayList<String> numberList = new ArrayList<String>(numbers.length);

		for (int num : numbers) {
			numberList.add(String.valueOf(num));
		}
		// compare only for the greatest length of two numbers (fill with the highest significant digit)
		numberList.sort((v1, v2) -> (v2 + v1).compareTo(v1 + v2));

		return String.join(",", numberList);
	}
}
