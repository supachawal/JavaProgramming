import java.util.ArrayDeque;
import java.util.Iterator;


public class Problem5_ListAllPossibleSummations_PASSED {

	public static void main(String[] args) {
		sumAnyToResult(1, 9, 100, 0, new ArrayDeque<Integer>());
		System.out.printf("End program\n");
	}
	
	public static void sumAnyToResult(int i, int endNum, int targetNum, int currentResult, ArrayDeque<Integer> sol) {
		if (i <= endNum) {
			sol.addLast(i);
			sumAnyToResult(i + 1, endNum, targetNum, currentResult + i, sol);
			sol.removeLast();
			
			if (i > 1) {
				sol.addLast(-i);
				sumAnyToResult(i + 1, endNum, targetNum, currentResult - i, sol);
				sol.removeLast();

				Integer last = sol.pollLast();
				int newLast = Integer.valueOf(String.valueOf(last) + String.valueOf(i));
				sol.addLast(newLast);
				sumAnyToResult(i + 1, endNum, targetNum, currentResult - last + newLast, sol);
				sol.removeLast();
				sol.addLast(last);
			}
		} else if (currentResult == targetNum) {
			printSummations(sol);
		}
	}
	
	private static void printSummations(ArrayDeque<Integer> sol) {
		Iterator<Integer> it = sol.iterator();
		if (it.hasNext()) {
			System.out.printf("%d", it.next());
			while(it.hasNext()) {
				int term = it.next();
				System.out.printf(" %c %d", (term >= 0 ? '+' : '-'), Math.abs(term));
			}
			System.out.printf("\n");
		}
	}
}
