package sushi;

import java.util.Date;
import java.text.*;

/**
 * @author micha
 * 
 */
public class DateUtils {

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
