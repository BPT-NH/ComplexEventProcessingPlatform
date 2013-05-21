package sushi.aggregation.element;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import sushi.event.SushiEventType;
import sushi.event.collection.SushiTreeElement;

/**
 * representation of a tree node for pattern operators
 *
 * @param <T> type of content to be stored
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
	 * creates a root node
	 * 
	 * @param id the identifier
	 * @param content the content to be stored in the new node
	 */
	public EventTypeElement(int id, SushiEventType content) {
		super(id, content);
		this.alias = new String();
	}
	
	/**
	 * creates a root node
	 * 
	 * @param parent
	 * @param id the identifier
	 * @param content the content to be stored in the new node
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
