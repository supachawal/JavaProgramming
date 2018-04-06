import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class P1225_YourTermProject_Parenthesis_PASSED {
/*
Sample Input
8
(A-B + C) - (A+(B - C)) - (C-(D- E) )
((A)-( (B)))
A-(B+C)
A-((B)+(C))
A-((B)+((C)))
(A-(((B))+((C))))
(-)(A-B)
A-((B+C))


Sample Output
A-B+C-(A+B-C)-(C-(D-E))
A-B
A-(B+C)
A-(B+C)
A-(B+C)
A-(B+C)
A-(B+C)

*/
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		StringBuilder answer;
		String textLine;
		int T, t;
		T = Integer.parseInt(br.readLine());
		for (t = 0; t < T; t++) {
			if ((textLine = br.readLine()) == null) {
				break;
			}
			
			answer = new StringBuilder();
			removeExtraParenthesises(answer, textLine.toCharArray(), 0, false);
			pw.println(answer);
		}
		pw.close();
	}
	
	private static class ReturnInfo {
		public int operandCount;
		public int newStartIndex;
		StringBuilder sbSub = new StringBuilder();
	}
	
	private static ReturnInfo removeExtraParenthesises(StringBuilder sbOutput, char[] expression, int startIndex, boolean outerMinus) {
		char c;
		boolean minus = false;
		ReturnInfo returnInfo = new ReturnInfo();

		while (startIndex < expression.length) {
			c = expression[startIndex++];
			
			if (c == ')') {
				break;
			}
			
			if (!Character.isWhitespace(c)) {
				if (c == '-') {
					minus = true;
					returnInfo.sbSub.append(c);
				} else {
					if (c == '(') {
						ReturnInfo sub = removeExtraParenthesises(returnInfo.sbSub, expression, startIndex, minus);
						startIndex = sub.newStartIndex;
						if (sub.operandCount > 0) {
							returnInfo.operandCount += sub.operandCount;
						} else if (sub.sbSub.indexOf("-") >= 0){
							minus = true;
							continue;
						}
					} else {
						if (c >= 'A' && c <= 'Z') {
							returnInfo.operandCount++;
						}

						returnInfo.sbSub.append(c);
					}

					if (c != '+') {
						minus = false;
					}
				}
			}
		}

		if (outerMinus && returnInfo.operandCount > 1) {
			sbOutput.append('(').append(returnInfo.sbSub).append(')');
			returnInfo.operandCount = 1;
		} else {
			sbOutput.append(returnInfo.sbSub);
		}
		returnInfo.newStartIndex = startIndex;
		return returnInfo;
	}
}

