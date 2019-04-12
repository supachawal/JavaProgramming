import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

class Problem5_ListAllPossibleSummationsTest {

	@Test
	void testSolve() {
		Problem5_ListAllPossibleSummations tester = new Problem5_ListAllPossibleSummations();
		List<Integer[]> actualResult = tester.solve(1, 9, 100);
		
		int[][] expectedResult = {
									{ 1, 2, 3, 4, 5, 6, 78, 9 },
									{ 1, 2, 34, -5, 67, -8, 9 },
									{ 1, 23, -4, 5, 6, 78, -9 },
									{ 1, 23, -4, 56, 7, 8, 9 },
									{ 12, 3, 4, 5, -6, -7, 89 },
									{ 12, 3, -4, 5, 67, 8, 9 },
									{ 12, -3, -4, 5, -6, 7, 89 },
									{ 123, 4, -5, 67, -89 },
									{ 123, 45, -67, 8, -9 },
									{ 123, -4, -5, -6, -7, 8, -9 },
									{ 123, -45, -67, 89 }
								};
		
		assertTrue(expectedResult.containsAll(actualResult), "Some element in actual result is out of the expected one");
		assertTrue(actualResult.containsAll(expectedResult), "The actual result doesn't cover all the expected one");
	}
	
	private static boolean containsAll(actualResult)

}
