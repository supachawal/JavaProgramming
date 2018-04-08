import java.util.Scanner;

// Time exceeded

public class C_GoGopher {
	
	private Scanner in = new Scanner(System.in);
	
	public static void main(String[] args) {
		(new C_GoGopher()).solve();
	}
	
	public void solve() {
		int T = in.nextInt();
		
		for (int t = 1; t <= T; t++) {
			boolean[][] booked = new boolean[1000][1000];
			int area = in.nextInt();
			int i = 500, j = 500;

			System.out.printf("%d %d\n", i, j);
			System.out.flush();

			i = in.nextInt();
			j = in.nextInt();
			int i1 = i, j1 = j, i2 = i, j2 = j;

			while (i > 0 || j > 0) {
				i1 = Math.min(i1, i); 
				i2 = Math.max(i2, i); 
				j1 = Math.min(j1, j); 
				j2 = Math.max(j2, j);
				booked[i][j] = true;
				
				int[] targetPoint = findCenterAvailablePoint(booked, i1, j1, i2, j2);
				System.out.printf("%d %d\n", targetPoint[0], targetPoint[1]);
				System.out.flush();

				i = in.nextInt();
				j = in.nextInt();
			}
		}
	}
	
	private static int[] findCenterAvailablePoint(boolean[][] booked, int i1, int j1, int i2, int j2) {
		int[] ret = new int[2];
		int i, j;
		
		for (i = i1 + 1; i < i2; i++) {
			for (j = j1 + 1; j < j2; j++) {
				if (!booked[i][j]) {
					ret[0] = i;
					ret[1] = j;
					return ret;
				}
			}
		}
		
		int width = j2 - j1;
		int height = i2 - i1;
		
		if (width <= height) {
			for (j = j2; j >= j1; j--) {
				if (!booked[i1][j]) {
					ret[0] = i1;
					ret[1] = j;
					break;
				}
				if (!booked[i2][j]) {
					ret[0] = i2;
					ret[1] = j;
					break;
				}
			}
			
			ret[0] = i1 > 2 ? i1 - 1 : i2 + 1;
			ret[1] = (j1 + j2) >> 1;

		} else {
			for (i = i2; i >= i1; i--) {
				if (!booked[i][j1]) {
					ret[0] = i;
					ret[1] = j1;
					break;
				}
				if (!booked[i][j2]) {
					ret[0] = i;
					ret[1] = j2;
					break;
				}
			}
			
			ret[0] = (i1 + i2) >> 1;
			ret[1] = j1 > 2 ? j1 - 1 : j2 + 1;
			
		}
		return ret;
	}
}