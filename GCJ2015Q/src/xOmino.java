public class xOmino {
	public enum Player {UNKNOWN, RICHARD, GABRIEL}
	public static void main(String[] args) {
		System.out.printf("\n");
	}

	private int X;	//X-Omino (number of 1x1 square to form adjacent squares
	private int [][] board;
	public int L;	//Max(Rows,Cols)
	public int S;	//Min(Rows,Cols)
	private Player winner;

	public void test() {
	}
	public xOmino(final int initX, final int initR, final int initC) {
		X = initX;
		L = Math.max(initR, initC);
		S = Math.min(initR, initC);
		winner = quicklyForecastWinner();
		
		if (winner == Player.UNKNOWN) {
			board = new int[L][S];
			for (int i = 0; i < L; i++) {
				for (int j = 0; j < S; j++) {
					board[i][j] = 0;
				}
			}
		}
	}
	
	public Player winner() {
		if (winner != Player.UNKNOWN)
			return winner;
		
		System.out.println("OUT OF SCOPE!");
		return Player.GABRIEL;
	}

	private Player quicklyForecastWinner() {
		Player w = Player.GABRIEL;
		int area = L * S;
		//Check shortcut for quick result
		/*
		 *  Richard can force a win if any of the following conditions hold; otherwise Gabriel will win.

		    X does not divide S*L,
		    X=3 and S=1,
		    X=4 and S<=2,
		    X =5 and either (i) S<=2 or (ii) (S, L) = (3, 5),
		    X=6 and S<=3,
		    X>=7.

		 */
		if (area % X != 0
				|| X >= 7
		   ) {
			w = Player.RICHARD;
		} else if (X == 3 && S == 1
				|| X == 4 && S <= 2
				|| X == 5 && (S <= 2 || (S == 3 && L == 5))
				|| X == 6 && S <= 3
				  ) {
			w = Player.RICHARD;
		}
		return w;
	}

	@SuppressWarnings("unused")
	private Player quicklyForecastWinner_Wrong() {
		Player w = Player.UNKNOWN;
		int area = L * S;
		//Check shortcut for quick result
		if (X >= 5
				|| area < 2 * X 
				|| area % X != 0
				|| X > L
		   ) {
			w = Player.RICHARD;
		} else if (X == 1) {
			w = Player.GABRIEL;
		} else if (X == 2) { // I
			if (L % 2 == 0
				|| S % 2 == 0
				) {
				w = Player.GABRIEL;
			} else {
				w = Player.RICHARD;
			}
		} else if (X == 3) {
			if (// L shape
				L % 3 == 0 && S % 2 == 0
				|| L % 2 == 0 && S % 3 == 0
				|| S == 3 && ((L - 1) % 3 == 0 && S % 2 == 0 || (L - 1) % 2 == 0 && S % 3 == 0) 
				|| L == 3 && (L % 3 == 0 && (S - 1) % 2 == 0 || L % 2 == 0 && (S - 1) % 3 == 0)
// less strict	// I shape
//				|| L % 3 == 0
//				|| C % 3 == 0
				) {
				w = Player.GABRIEL;
			} else {
				w = Player.RICHARD;
			}
		} else if (X == 4) { // T, L, I
			if (// T shape
				L == 4 && S == 3
				|| L % 4 == 0 && S % 4 == 0
				|| S == 4 && (L - 1) % 4 == 0
			   ) {
				w = Player.GABRIEL;
			} else {
				w = Player.RICHARD;
			}
		}
		return w;
	}
}
