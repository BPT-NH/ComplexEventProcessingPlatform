package sushi.query;

/**
 * This enumeration encapsulates the types of @see SushiQuery s.
 * LIVE: The query is saved in the database and activated. It is executed on event streams.
 * ONDEMAND: The query is saved in the database and can be executed by the user whenever he wants.
 *
 */
public enum SushiQueryTypeEnum {
	LIVE, ONDEMAND
}