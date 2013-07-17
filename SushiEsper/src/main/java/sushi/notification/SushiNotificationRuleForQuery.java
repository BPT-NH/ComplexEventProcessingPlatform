package sushi.notification;

import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import sushi.email.EmailUtils;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;
import sushi.query.SushiQuery;
import sushi.user.SushiUser;

/**
 * This class represents a special @see SushiNotificationRule that notfies a user about triggered queries.
 */
@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("Q")
public class SushiNotificationRuleForQuery extends SushiNotificationRule {
	
	@ManyToOne
	protected SushiQuery query;
		
	/**
	 * Default-Constructor for JPA.
	 */
	public SushiNotificationRuleForQuery() {
		this.ID = 0;
		this.query = null;
		this.user = null;
	}
	
	/**
	 * Creates a new query notification rule.
	 * @param query
	 * @param user
	 * @param priority
	 */
	public SushiNotificationRuleForQuery(SushiQuery query, SushiUser user, SushiNotificationPriorityEnum priority) {
		this.query = query;
		this.user = user;
		this.priority = priority;
	}

	/**
	 * This method creates a new @see SushiNotificationForQuery.
	 * It is called when the query of this notification rule is triggered.
	 * @param log
	 */
	public void trigger(String log) {
		SushiNotificationForQuery notification = new SushiNotificationForQuery(user, log, this);
		notification.save();
		if (priority == SushiNotificationPriorityEnum.HIGH) {
			//send mail
			EmailUtils.sendBP2013Mail(user.getMail(), "Notification GET-Events", notification.toString());
		}
	}
			
	public String toString() {
		String representation = "Notification for " + this.query;
		representation += " for user " + user.getName();
		return representation;
	}

	
	//Getter and Setter
	
	public SushiQuery getQuery() {
		return query;
	}

	public void setQuery(SushiQuery query) {
		this.query = query;
	}

	@Override
	public Persistable getTriggeringEntity() {
		return getQuery();
	}
	
	//JPA-Methods
	
	/**
	 * Finds all query notification rules from database.
	 * @return all query notification rules
	 */
	@SuppressWarnings("unchecked")
	public static List<SushiNotificationRuleForQuery> findAllQueryNotificationRules() {
		Query q = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiNotificationRule WHERE Disc = 'Q'", SushiNotificationRuleForQuery.class);
		return q.getResultList();
	}
	
	/**
	 * Finds a query notification rule from database by ID.
	 * @param ID
	 * @return query notification rule
	 */
	public static SushiNotificationRuleForQuery findByID(int ID){
		return Persistor.getEntityManager().find(SushiNotificationRuleForQuery.class, ID);
	}
		
	/**
	 * Finds query notification rules from database that are connected with a certain query.
	 * @param query
	 * @return query notification rules for query
	 */
	@SuppressWarnings("unchecked")
	public static List<SushiNotificationRuleForQuery> findByQuery(SushiQuery query){
		Query q = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiNotificationRule WHERE QUERY_ID = '" + query.getID() + "'", SushiNotificationRuleForQuery.class);
		return q.getResultList();
	}

}
