import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;


public class Problem5_ListAllPossibleSummations {

	public static void main(String[] args) {
		Problem5_ListAllPossibleSummations solver = new Problem5_ListAllPossibleSummations();
		
		solver.solve(1, 9, 100);
		System.out.printf("%sEnd program\n", solver);
	}
	
	private List<Integer[]> _wholeResult;
	private ArrayDeque<Integer> _eachResult;

	public List<Integer[]> solve(int i, int endNum, int targetNum) {
		_wholeResult = new ArrayList<Integer[]>();
		_eachResult = new ArrayDeque<Integer>();
		sumAnyToResult(i, endNum, targetNum, 0);
		return _wholeResult;
	}
	
	private void sumAnyToResult(int i, int endNum, int targetNum, int currentResult) {
		if (i <= endNum) {
			_eachResult.addLast(i);
			sumAnyToResult(i + 1, endNum, targetNum, currentResult + i);
			_eachResult.removeLast();
			
			if (i > 1) {
				_eachResult.addLast(-i);
				sumAnyToResult(i + 1, endNum, targetNum, currentResult - i);
				_eachResult.removeLast();

				Integer last = _eachResult.pollLast();
				int newLast = Integer.valueOf(String.valueOf(last) + String.valueOf(i));
				_eachResult.addLast(newLast);
				sumAnyToResult(i + 1, endNum, targetNum, currentResult - last + newLast);
				_eachResult.removeLast();
				_eachResult.addLast(last);
			}
		} else if (currentResult == targetNum) {
			_wholeResult.add(_eachResult.toArray(new Integer[_eachResult.size()]));
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sbOutput = new StringBuilder();
		_wholeResult.forEach(each->{
			int n = each.length; 
			if (n > 0) {
				sbOutput.append("print(");
				sbOutput.append(each[0]);
				for (int i = 1; i < n; i++) {
					int term = each[i];
					sbOutput.append(term >= 0 ? " + " : " - ").append(Math.abs(term));
				}
				sbOutput.append(")\n");
			}
		});
			
		return sbOutput.toString();
	}
}
