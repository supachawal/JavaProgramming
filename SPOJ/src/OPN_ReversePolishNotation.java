import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Stack;

class OPN_ReversePolishNotation {
	/*
	Example

	Input:
3
(a+(b*c))
((a+b)*(z+x))
((a+t)*((b+(a+c))^(c+d)))

	Output:
	abc*+
	ab+zx+*
	at+bac++cd+^*

	*/
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);
		
		String textLine;
		String answer;
		int nTestCases = Integer.parseInt(br.readLine());
		
		for (int testCaseIndex = 0; testCaseIndex < nTestCases; testCaseIndex++) {
			if ((textLine = br.readLine()) == null) {
				break;
			}

			answer = reversePolishNotationLite(textLine);
			
			pw.println(answer);
		}
		pw.close();
		br.close();
	}

	/**
	 * The modified version of Shunting-yard algorithm (Edsger Dijkstra) 
	 * with constraint that needs parenthesis in every term.
	 * @param e expression string
	 * @return
	 */
	public static String reversePolishNotationLite(String e) {
		int len = e.length();
		StringBuilder ret = new StringBuilder();
		Stack<Character> opStack = new Stack<Character>();

		char op;
	    for (int i = 0; i < len; i++) {
	        char c = e.charAt(i);
	
	        if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^') {  // operators
	            opStack.push(c);    //push o1 onto the operator stack
	        } else if (c >= 'A' && c <= 'z' || c >= '0' && c <= '9') { // operands
	            ret.append(c);           //add it to the output queue
	        } else if (c == '(') {
	            opStack.push(c);     // push it onto the stack
	        } else if (c == ')') {
	            // loop pop operators off the stack onto the output queue until the token at the top of the stack is a left parenthesis.
	            while ((op = opStack.pop()) != '(') {
	                ret.append(op);
	            }
	        }
	    }
	    
//	    // Pop the remaining operator onto the output queue.
//	    while (opStack.size() > 0) {
//	        if ((op = opStack.pop()) != '(') {
//	            ret.append(op);
//	        }
//	    } 
	    return ret.toString();
	}
}
