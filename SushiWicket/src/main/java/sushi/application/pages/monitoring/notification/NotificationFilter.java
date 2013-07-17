package sushi.application.pages.monitoring.notification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import sushi.application.pages.eventrepository.model.AbstractFilter;
import sushi.application.pages.eventrepository.model.EventFilter;
import sushi.notification.SushiNotification;
import sushi.notification.SushiNotificationForEvent;

/**
 * Filter for Notification-List on @see NotificationPage
 */
public class NotificationFilter extends AbstractFilter {
	
	public NotificationFilter(String eventFilterCriteria, String eventFilterCondition, String filterValue){
		super(eventFilterCriteria, eventFilterCondition, filterValue);
	}
	
	public NotificationFilter() {
		super();
	}

	/**
	 * checks whether a notification matches a filtercriteria
	 * @param notification
	 * @return 
	 */
	public boolean match(SushiNotification notification) {
		if(filterCriteria == null || filterCondition == null || filterValue == null){
			return true;
		}
		
		//"ID", "Timestamp", "NotificationRule (ID)"
		if(filterCriteria.equals("ID")){
			try{
				if(filterCondition.equals("<")){
					if(notification.getID() < Integer.parseInt(filterValue)) return true;
				} else if(filterCondition.equals(">")){
					if(notification.getID() > Integer.parseInt(filterValue)) return true;
				} else {
					if(notification.getID() == Integer.parseInt(filterValue)) return true;
				}
			}
			catch(NumberFormatException e){
				return false;
			}
		} else if(filterCriteria.equals("Timestamp")){
			try {
				if(filterCondition.equals("<")){
					if(notification.getTimestamp().before(SimpleDateFormat.getInstance().parse(filterValue))) return true;
				} else if(filterCondition.equals(">")){
					if(notification.getTimestamp().after(SimpleDateFormat.getInstance().parse(filterValue))) return true;
				} else {
					if(notification.getTimestamp().equals(SimpleDateFormat.getInstance().parse(filterValue))) return true;
				}
				return false;
			}catch(ParseException e) {
				return false;
			}

		} else if(filterCriteria.equals("NotificationRule (ID)")){
			if(filterCondition.equals("<")){
				if(notification.getNotificationRule().getID() < Integer.parseInt(filterValue)) return true;
			} else if(filterCondition.equals(">")){
				if(notification.getNotificationRule().getID() > Integer.parseInt(filterValue)) return true;
			} else {
				if(notification.getNotificationRule().getID() == Integer.parseInt(filterValue)) return true;
			}
			return false;
		} else {
			return false;
		}
		return false;
	}

}
