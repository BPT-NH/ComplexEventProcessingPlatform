package sushi.transformation.element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import sushi.event.collection.SushiTreeElement;

/**
 * Representation of a tree node for pattern operators.
 * A range element is required for REPEAT and UNTIL pattern operators.
 * Distinct attributes are required for the EVERY-DISTINCT pattern operator.
 * 
 * @see PatternOperatorEnum
 */
@Entity
@DiscriminatorValue("PO")
public class PatternOperatorElement extends SushiTreeElement<Serializable> implements Serializable  {
	
	private static final long serialVersionUID = -9184705192436364035L;
	
	@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="RangeElement_ID")
	private RangeElement rangeElement;
	
	@ElementCollection
	@CollectionTable(name = "DistinctAttributes", joinColumns = @JoinColumn(name = "PatternOperatorElement_ID"))
	@Column(name = "attribute")
	private List<String> distinctAttributes;
	
	public PatternOperatorElement() {
		super();
		this.rangeElement = new RangeElement();
		this.distinctAttributes = new ArrayList<String>();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param id the identifier
	 * @param content pattern operator
	 */
	public PatternOperatorElement(int id, PatternOperatorEnum content) {
		super(id, content);
		this.rangeElement = new RangeElement();
		this.distinctAttributes = new ArrayList<String>();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param parent
	 * @param id the identifier
	 * @param content pattern operator
	 */
	public PatternOperatorElement(SushiTreeElement<Serializable> parent, int id, PatternOperatorEnum content) {
		super(parent, id, content);
		this.rangeElement = new RangeElement();
		this.distinctAttributes = new ArrayList<String>();
	}

	public RangeElement getRangeElement() {
		return rangeElement;
	}

	public void setRangeElement(RangeElement rangeElement) {
		this.rangeElement = rangeElement;
	}

	public List<String> getDistinctAttributes() {
		return distinctAttributes;
	}

	public void setDistinctAttributes(List<String> distinctAttributes) {
		this.distinctAttributes = distinctAttributes;
	}
	
}
