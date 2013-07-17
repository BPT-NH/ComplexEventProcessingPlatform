package sushi.visualisation;

import java.util.Calendar;
import java.util.Date;

/**
 * This enumeration encapsulates the possible time periods for event views.
 */
public enum SushiTimePeriodEnum {
	ONEHOUR ("last hour", 3600),
	ONEDAY("last day", 86400),
	ONEMONTH ("last month", 2592000),
	ONEYEAR("last year", 31536000),
	INF ("infinite", 0);
	
	private String type;
	private int seconds;
	
	SushiTimePeriodEnum(String type, int seconds){
		this.type = type;
		this.seconds = seconds;
	}

	public String toString() {
		return type;
	}
	
	public int getTime() {
		return seconds;
	}

	/**
	 * Calculates a date that lies as far back from now as the time period.
	 * Between the returned date and now lies the configured time period. 
	 * @return date
	 */
	public Date getStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, - seconds);
		return cal.getTime();
	}

}
