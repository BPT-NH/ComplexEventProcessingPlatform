package sushi.aggregation.element;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import sushi.event.collection.SushiTreeElement;

/**
 * representation of a tree node for pattern operators
 */
@Entity
@DiscriminatorValue("FEC")
public class FilterExpressionConnectorElement extends SushiTreeElement<Serializable> implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	public FilterExpressionConnectorElement() {
		super();
	}
	
	/**
	 * creates a root node
	 * 
	 * @param id the identifier
	 * @param content the content to be stored in the new node
	 */
	public FilterExpressionConnectorElement(int id, FilterExpressionConnectorEnum content) {
		super(id, content);
	}
	
	/**
	 * creates a root node
	 * 
	 * @param parent
	 * @param id the identifier
	 * @param content the content to be stored in the new node
	 */
	public FilterExpressionConnectorElement(SushiTreeElement<Serializable> parent, int id, FilterExpressionConnectorEnum content) {
		super(parent, id, content);
	}
	
}
