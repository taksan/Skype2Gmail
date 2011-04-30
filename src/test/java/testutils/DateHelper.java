package testutils;

import java.util.Calendar;
import java.util.Date;

public class DateHelper {
	public static Date makeDate(int year, int month, int day, int hour, int minute,
			int second) {
		Calendar cal = makeCalendar(year, month, day, hour, minute, second);
		return cal.getTime();
	}
	
	public static long makeDateInMillis(int year, int month, int day, int hour, int minute,
			int second) {
		Calendar cal = makeCalendar(year, month, day, hour, minute, second);
		return cal.getTimeInMillis();
	}

	public static Calendar makeCalendar(int year, int month, int day, int hour,
			int minute, int second) {
		Calendar cal = Calendar.getInstance();

		// Clear all fields
		cal.clear();

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DATE, day);

		cal.set(Calendar.HOUR, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);

		return cal;
	}
}
