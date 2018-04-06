import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

public class P1106_TwoTeams {
/*
SAMPLE ...

INPUT:
7
2 3 0
3 1 0
1 2 4 5 0
3 0
3 0
7 0
6 0

OUTPUT:
4
2 4 5 6
*/
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter pw = new PrintWriter(System.out);

		String textLine;
		String[] splitted;
		StringBuilder answer = new StringBuilder();
		
		textLine = br.readLine();
		int N = Integer.parseInt(textLine);
		int u, v;
// REMOVED: always possible
//		boolean impossible = false;
		int[] teamOfMember = new int[N + 1];		// 0=team1 or 1=team2
		Arrays.fill(teamOfMember, -1);
		boolean hasChoice;
		boolean hasFriendInTheOtherTeam;
		
		for (u = 1; u <= N; u++) {
			textLine = br.readLine();
			splitted = textLine.split("\\s+");
			
			do {
				if (teamOfMember[u] < 0) {
					teamOfMember[u] = 0;
					hasChoice = true;
				} else {
					hasChoice = false;
				}
				
				int friendTeam = teamOfMember[u] ^ 1;
				hasFriendInTheOtherTeam = false;
				
				for (String vStr : splitted) {
					v = Integer.parseInt(vStr);
					if (v > 0) {
						if (teamOfMember[v] == friendTeam) {
							hasFriendInTheOtherTeam = true;
							break;
						}
					}
				}
				if (!hasFriendInTheOtherTeam) {
					for (String vStr : splitted) {
						v = Integer.parseInt(vStr);
						if (v > 0) {
							if (teamOfMember[v] < 0) {
								teamOfMember[v] = friendTeam;
								hasFriendInTheOtherTeam = true;
								break;
							}
						}
					}
				}
				if (!hasFriendInTheOtherTeam && hasChoice) {
					teamOfMember[u] ^= 1;
				}
			} while (!hasFriendInTheOtherTeam && hasChoice);
// REMOVED: always possible
//			if (!hasFriendInTheOtherTeam) {
//				impossible = true;
//				break;
//			}
		}

// REMOVED: always possible
//		if (impossible) {
//			pw.printf("0\n");
//		} else {
			int memberCount = 0;
			for (u = 1; u <= N; u++) {
				if (teamOfMember[u] == 1) {
					if (answer.length() > 0) {
						answer.append(' ');
					}

					answer.append(u);
					memberCount++;
				}
			}
			
			pw.printf("%d\n%s\n", memberCount, answer);
//		}
		
		pw.close();
	}
}
