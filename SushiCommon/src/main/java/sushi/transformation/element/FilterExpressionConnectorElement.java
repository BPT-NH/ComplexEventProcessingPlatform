package sushi.transformation.element;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import sushi.event.collection.SushiTreeElement;

/**
 * Representation of a tree node for filter expression connectors.
 * 
 * @see FilterExpressionConnectorEnum
 */
@Entity
@DiscriminatorValue("FEC")
public class FilterExpressionConnectorElement extends SushiTreeElement<Serializable> implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	public FilterExpressionConnectorElement() {
		super();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param id the identifier
	 * @param content filter expression connector
	 */
	public FilterExpressionConnectorElement(int id, FilterExpressionConnectorEnum content) {
		super(id, content);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param parent
	 * @param id the identifier
	 * @param content filter expression connector
	 */
	public FilterExpressionConnectorElement(SushiTreeElement<Serializable> parent, int id, FilterExpressionConnectorEnum content) {
		super(parent, id, content);
	}
	
}
