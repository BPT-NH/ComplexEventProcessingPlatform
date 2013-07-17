package sushi.application.pages.monitoring.notification;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;

import sushi.application.components.table.SushiProvider;
import sushi.application.pages.eventrepository.model.EventFilter;
import sushi.event.SushiEvent;
import sushi.notification.SushiNotification;
import sushi.notification.SushiNotificationForEvent;
import sushi.user.SushiUser;

/**
 * This class is a provider for @see SushiNotification 
 */
public class NotificationProvider extends SushiProvider<SushiNotification> implements IFilterStateLocator {

	private SushiUser user = null; 
	private NotificationFilter notificationFilter = new NotificationFilter();
	
	public NotificationProvider(SushiUser user) {
		super(new ArrayList<SushiNotification>());
		this.user = user;
		update();
	}

	public void update() {
		if (user == null) return;  
		List<SushiNotification> notifications = SushiNotification.findUnseenForUser(user);
        if (notifications != null);
		entities = notifications;
	}

	public void markSeenSelectedEntries() {
		for (SushiNotification notification : selectedEntities) {
			notification.setSeen();
		}
	}
	
	private List<SushiNotification> filterNotifications(List<SushiNotification> notificationsToFilter, NotificationFilter notificationFilter) {
		List<SushiNotification> returnedNotifications = new ArrayList<SushiNotification>();
		for(SushiNotification notification: notificationsToFilter){
			if(notificationFilter.match(notification)){
				returnedNotifications.add(notification);
			}
		}
		return returnedNotifications;
	}

	@Override
	public Object getFilterState() {
		return notificationFilter;
	}

	@Override
	public void setFilterState(Object state) {
		this.notificationFilter = (NotificationFilter) state;
	}
	
	public NotificationFilter getNotificationFilter() {
		return notificationFilter;
	}

	public void setNotificationFilter(NotificationFilter notificationFilter) {
		this.notificationFilter = notificationFilter;
		entities = filterNotifications(SushiNotificationForEvent.findUnseenForUser(user), notificationFilter);
	}

}
