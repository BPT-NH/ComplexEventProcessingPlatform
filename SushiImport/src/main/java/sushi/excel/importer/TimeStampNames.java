package sushi.excel.importer;

/**
 * This enum contains typical names for timestamps.
 * @author micha
 */
public enum TimeStampNames {
	
	TIMESTAMP, TIME, ZEIT, ZEITSTEMPEL, DATE, DATUM, UHRZEIT;
	
	public static boolean contains(String test) {
	    for (TimeStampNames timeStampName : TimeStampNames.values()) {
	        if (timeStampName.name().equals(test.toUpperCase())) {
	            return true;
	        }
	    }
	    return false;
	}
}
