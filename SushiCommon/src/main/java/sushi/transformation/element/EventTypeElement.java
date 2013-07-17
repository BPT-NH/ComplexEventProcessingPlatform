package sushi.transformation.element;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import sushi.event.SushiEventType;
import sushi.event.collection.SushiTreeElement;

/**
 * Representation of a tree node for event types.
 * Each event type element holds an alias that is unique for each pattern tree.
 */
@Entity
@DiscriminatorValue("ET")
public class EventTypeElement extends SushiTreeElement<Serializable> implements Serializable {
	
	private static final long serialVersionUID = -2184890213473132784L;
	
	@Column(name = "Alias")
	private String alias;
	
	public EventTypeElement() {
		super();
		this.alias = new String();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param id the identifier
	 * @param contentevent type
	 */
	public EventTypeElement(int id, SushiEventType content) {
		super(id, content);
		this.alias = new String();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param parent
	 * @param id the identifier
	 * @param content event type
	 */
	public EventTypeElement(SushiTreeElement<Serializable> parent, int id, SushiEventType content) {
		super(parent, id, content);
		this.alias = new String();
	}
	
	public boolean hasAlias() {
		return alias != null && !alias.isEmpty();
	}

	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
}
