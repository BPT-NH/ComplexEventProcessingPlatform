package sushi.notification;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Query;
import sushi.persistence.Persistor;
import sushi.user.SushiUser;

/**
 * This class is a special @see SushiNotification that is created when a query is triggered.
 */
@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("Q")
public class SushiNotificationForQuery extends SushiNotification {
	
	@Column(name = "Log")
	String log;
	
	/**
	 *Default Constructor for JPA 
	 */
	@SuppressWarnings("unused")
	private SushiNotificationForQuery() {
	}
	
	/**
	 * Creates a new query notification.
	 * @param user
	 * @param log
	 * @param rule
	 */
	public SushiNotificationForQuery(SushiUser user, String log, SushiNotificationRuleForQuery rule) {
		this.timestamp = new Date();
		this.user = user;
		this.log = log;
		this.notificationRule = rule;
	}
	
	public String toString() {
		//cast notification rule to SushiNotificationRuleForEventType
		SushiNotificationRuleForQuery notificationQueryType = (SushiNotificationRuleForQuery) notificationRule;
			return notificationQueryType.getQuery() + " was triggered on " + timestamp + ":" + log;	
	}

	@Override
	public String getTriggeringText() {
		return log;
	}
	
	//JPA-Methods
	
	@SuppressWarnings("unchecked")
	public static List<SushiNotificationForQuery> findUnseenQueryNotificationForUser(SushiUser user) {
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiNotification WHERE USER_ID = '" + user.getID() + "' AND seen = 0 AND Disc = 'Q'", SushiNotificationForQuery.class);
		return query.getResultList();
	}

}
