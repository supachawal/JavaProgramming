import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;

/*

C. Tourist Problem
Iahub is a big fan of tourists. He wants to become a tourist himself, so he planned a trip. 
There are n destinations on a straight road that Iahub wants to visit. Iahub starts the excursion from kilometer 0. 
The n destinations are described by a non-negative integers sequence a1, a2, ..., an. The number ak represents 
that the kth destination is at distance ak kilometers from the starting point. No two destinations are located in the
same place.

Iahub wants to visit each destination only once. Note that, crossing through a destination is not considered visiting, 
unless Iahub explicitly wants to visit it at that point. Also, after Iahub visits his last destination, he doesn't come 
back to kilometer 0, as he stops his trip at the last destination.

The distance between destination located at kilometer x and next destination, located at kilometer y, is |x - y| kilometers. 
We call a "route" an order of visiting the destinations. Iahub can visit destinations in any order he wants, as long as 
he visits all n destinations and he doesn't visit a destination more than once.

Iahub starts writing out on a paper all possible routes and for each of them, he notes the total distance he would walk. 
He's interested in the average number of kilometers he would walk by choosing a route. As he got bored of writing out 
all the routes, he asks you to help him.

Input
The first line contains integer n (2 ≤ n ≤ 105). Next line contains n distinct integers a1, a2, ..., an (1 ≤ ai ≤ 107).

Output
Output two integers — the numerator and denominator of a fraction which is equal to the wanted average number. The fraction must be irreducible.

Examples
input
3
2 3 5
output
22 3

5
0 0
0 4
4 0
4 4
2 3

OUTPUT:
16.00000
 */
public class P340C_TouristProblem {
	private static BufferedReader br;
	private static PrintWriter pw;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String textLine = br.readLine();
		n = Integer.parseInt(textLine);
		permuteBuffer = new int[n + 1];
		a = new int[n];
		visited = new boolean[n];

		textLine = br.readLine();
		String[] splitted = textLine.split("\\s+");
		BigInteger fraction = BigInteger.ONE;
		for (int i = 0; i < n; i++) {
			a[i] = Integer.parseInt(splitted[i]);
			fraction = fraction.multiply(BigInteger.valueOf(i + 1));
		}
		BigInteger v = permuteCount(0);
		BigInteger gcd = v.gcd(fraction);
		v = v.divide(gcd);
		fraction = fraction.divide(gcd);
		pw.printf("%s %s", v, fraction);
		pw.close();
	}

	private static int n;
	private static int[] permuteBuffer;  
	private static int[] a;
	private static boolean[] visited;
	
	private static BigInteger permuteCount(int level) {
		BigInteger result = BigInteger.ZERO;
		if (level == n) {
			for(int i = 1; i <= n; i++) {
				result = result.add(BigInteger.valueOf(Math.abs(permuteBuffer[i] - permuteBuffer[i - 1])));
			}
		} else {
			for (int i = 0; i < n; i++)	{
				if (!visited[i]) {
					visited[i] = true;
					permuteBuffer[level + 1] = a[i];
					result = result.add(permuteCount(level + 1));
					visited[i] = false;
				}
			}
		}
		return result;
	}
}