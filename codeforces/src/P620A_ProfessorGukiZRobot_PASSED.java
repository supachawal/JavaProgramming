import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class P620A_ProfessorGukiZRobot_PASSED {
	private static BufferedReader br;
	private static PrintWriter pw;
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String textLine = br.readLine();
		String[] splitted = textLine.split("\\s+");
		int x1 = Integer.parseInt(splitted[0]);
		int y1 = Integer.parseInt(splitted[1]);
		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		int x2 = Integer.parseInt(splitted[0]);
		int y2 = Integer.parseInt(splitted[1]);
		
		int dX = Math.abs(x2 - x1);
		int dY = Math.abs(y2 - y1);
		pw.println(Math.max(dX, dY));
		pw.close();
	}

}