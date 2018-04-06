
public class Problem2_CombineTwoListsAlternatively_PASSED {

	public static void main(String[] args) {
		Object [] list1 = {'a', 'b', 'c'};
		Object [] list2 = {1, 2, 3, 4};
		Object [] listResult = combineAlternatively(list1, list2);
		
		for (Object o : listResult) {
			System.out.printf("%s, ", o);
		}
	}

	public static Object [] combineAlternatively(Object [] l1, Object [] l2) {
		Object [] r = new Object[l1.length + l2.length];
		int n = Math.max(l1.length, l2.length);
		int count = 0;
		for (int i = 0; i < n; i++) {
			if (i < l1.length) {
				r[count++] = l1[i];
			}
				
			if (i < l2.length) {
				r[count++] = l2[i];
			}
		}
			
		return r;
	}

}
