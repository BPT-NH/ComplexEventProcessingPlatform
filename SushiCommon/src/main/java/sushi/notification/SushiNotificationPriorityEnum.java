package sushi.notification;

/**
 * This enumeration encapsulates the priorities for a notification rule.
 */
public enum SushiNotificationPriorityEnum {
	LOW ("low"),
	HIGH ("high");

	private String type;
	
	SushiNotificationPriorityEnum(String type){
		this.type = type;
	}

	public String toString() {
		return type;
	}
		
}


