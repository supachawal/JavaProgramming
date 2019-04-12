import java.util.*;

public class Solution {
	public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
		int T = in.nextInt();
		ArrayList<String> lstN = new ArrayList<String>();
		
		for (int t = 1; t <= T; t++) {
			int N = in.nextInt();
			lstN.add(String.valueOf(N));
		}
			
		for (int t = 1; t <= T; t++) {
			String s = lstN.get(t - 1);
			String B = s.replace("4", "1"); 
			String A = s.replaceAll("[^4]", "0").replace("4", "3"); 

    		System.out.printf("Case #%d: %d %d\n", t, Integer.parseInt(A), Integer.parseInt(B));
		}
		in.close();
	}
}