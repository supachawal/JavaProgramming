import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class P1547_ToandFro_PASSED {
/*
Sample Input

5
toioynnkpheleaigshareconhtomesnlewx
3
ttyohhieneesiaabss
0
Sample Output

theresnoplacelikehomeonasnowynightx
thisistheeasyoneab

*/
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		String textLine;
		int i, N;
		int index;
		int k, r;
		
		while ((textLine = br.readLine()) != null) {
			if ((N = Integer.parseInt(textLine)) == 0)
				break;

			char[] C = br.readLine().toCharArray();
			int m = (C.length + N - 1) / N;
			
			for (i = 0; i < C.length; i++) {
				k = i % m;
				r = i / m;
				index = 2 * ((k + 1) / 2) * N + ((k & 1) == 0 ? r : -(r + 1));
				
				pw.print(C[index]);
			}
			pw.println();
		}
		pw.close();
	}
}

