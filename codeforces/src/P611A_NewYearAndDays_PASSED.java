import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class P611A_NewYearAndDays_PASSED {
	private static PrintWriter pw;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		pw = new PrintWriter(System.out);
		String textLine;
		String[] splitted;
		int answer = 0;

		int x;
		
		textLine = br.readLine();
		splitted = textLine.split("\\s+");
		x = Integer.parseInt(splitted[0]);
		if (splitted[2].equals("week")) {
			if (++x > 7) {
				x = 1;
			}
			answer = countWeekdaysInYear(2016, x);
		} else if (splitted[2].equals("month")) {
			answer = countMonthdaysInYear(2016, x);
		}
		
		pw.printf("%d", answer);
		pw.close();
	}
	
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	public static int countWeekdaysInYear(int year, int x) {
		Calendar c = Calendar.getInstance();
// NO NEED:
//		pw.printf("first day of week = %d\n", c.getFirstDayOfWeek());
//		if (c.getFirstDayOfWeek() != 1) {
//			c.setFirstDayOfWeek(1);
//		}
		Date d;
		int daysInYear = isLeapYear(year) ? 366 : 365;
		int ret = 0;
		try {
			d = simpleDateFormat.parse(String.format("%d-01-01", year));
			c.setTime(d);
		} catch (ParseException e) {
		}
		int startWeekday = c.get(Calendar.DAY_OF_WEEK);

		try {
			d = simpleDateFormat.parse(String.format("%d-12-31", year));
			c.setTime(d);
		} catch (ParseException e) {
		}
		int endWeekday = c.get(Calendar.DAY_OF_WEEK);

		if (startWeekday > 1) {
			if (startWeekday <= x) {
				ret++;
			}
			daysInYear -= 8 - startWeekday;
		}
		if (endWeekday < 7) {
			if (endWeekday >= x) {
				ret++;
			}
			daysInYear -= endWeekday;
		}
		
		ret += daysInYear / 7;
		
		return ret;
	}

	public static final boolean isLeapYear(int year) {
		return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
	}
	public static int countMonthdaysInYear(int year, int x) {
		final int[] maxDaysOfMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		maxDaysOfMonth[2] = (isLeapYear(year) ? 29 : 28);
		
		int ret = 0;
		for (int i = 1; i <= 12; i++) {
			if (x <= maxDaysOfMonth[i]) {
				ret++;
			}
		}
		
		return ret;
	}
}
