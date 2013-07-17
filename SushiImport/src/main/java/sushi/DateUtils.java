package sushi;

import java.util.Date;
import java.text.*;

/**
 * This class centralizes methods to parse {@link Date}s.
 * @author micha
 */
public class DateUtils {

	/**
	 * This method parses a {@link Date} from a string with the format dd.MM.yyyy hh:mm.
	 * @param dateString
	 * @return
	 */
	public static Date parseDate(String dateString) {
		Date date = null;
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd.MM.yyyy kk:mm");
			date = (Date) formatter.parse(dateString);
		} catch (ParseException e) {
			System.out.println("Exception :" + e);
		}
		return date;
	}

}
