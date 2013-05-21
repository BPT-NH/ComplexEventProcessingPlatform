package sushi.aggregation.element;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import sushi.event.collection.SushiTreeElement;

/**
 * representation of a tree node for pattern operators
 */
@Entity
@DiscriminatorValue("PO")
public class PatternOperatorElement extends SushiTreeElement<Serializable> implements Serializable  {
	
	private static final long serialVersionUID = -9184705192436364035L;
	
	@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="RangeElement_ID")
	private RangeElement rangeElement;
	
	public PatternOperatorElement() {
		super();
		this.rangeElement = new RangeElement();
	}
	
	/**
	 * creates a root node
	 * 
	 * @param id the identifier
	 * @param content the content to be stored in the new node
	 */
	public PatternOperatorElement(int id, PatternOperatorEnum content) {
		super(id, content);
		this.rangeElement = new RangeElement();
	}
	
	/**
	 * creates a root node
	 * 
	 * @param parent
	 * @param id the identifier
	 * @param content the content to be stored in the new node
	 */
	public PatternOperatorElement(SushiTreeElement<Serializable> parent, int id, PatternOperatorEnum content) {
		super(parent, id, content);
		this.rangeElement = new RangeElement();
	}

	public RangeElement getRangeElement() {
		return rangeElement;
	}

	public void setRangeElement(RangeElement rangeElement) {
		this.rangeElement = rangeElement;
	}
	
}
