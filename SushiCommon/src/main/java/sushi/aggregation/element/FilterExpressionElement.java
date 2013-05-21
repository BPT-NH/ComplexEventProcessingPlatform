package sushi.aggregation.element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import sushi.event.collection.SushiTreeElement;

/**
 * representation of a tree node for filter expressions
 */
@Entity
@DiscriminatorValue("FE")
public class FilterExpressionElement extends SushiTreeElement<Serializable> implements Serializable  {
	
	private static final long serialVersionUID = -7712173256600654317L;

	@Column(name = "LeftHandSideExpression")
	private String leftHandSideExpression;
	
	@Column(name = "RightHandSideExpression")
	private String rightHandSideExpression;
	
	@ElementCollection
	private List<String> rightHandSideListOfValues;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name="RangeElement_ID")
	private RangeElement rightHandSideRangeOfValues;
	
	@Column(name = "RightHandSideRangeBased")
	private boolean rightHandSideRangeBased;
	
	public FilterExpressionElement() {
		super();
		init();
	}
	
	/**
	 * creates a root node
	 * 
	 * @param id the identifier
	 * @param content the content to be stored in the new node
	 */
	public FilterExpressionElement(int id, FilterExpressionOperatorEnum content) {
		super(id, content);
		init();
	}

	/**
	 * creates a root node
	 * 
	 * @param parent
	 * @param id the identifier
	 * @param content the content to be stored in the new node
	 */
	public FilterExpressionElement(SushiTreeElement<Serializable> parent, int id, FilterExpressionOperatorEnum content) {
		super(parent, id, content);
		init();
	}
	
	public FilterExpressionElement(SushiTreeElement<Serializable> parent, int id, FilterExpressionOperatorEnum content, String leftHandSideExpression, String rightHandSideExpression) {
		super(parent, id, content);
		init();
		this.leftHandSideExpression = leftHandSideExpression;
		this.rightHandSideExpression = rightHandSideExpression;
	}
	
	public FilterExpressionElement(SushiTreeElement<Serializable> parent, int id, FilterExpressionOperatorEnum content, String leftHandSideExpression, ArrayList<String> rightHandSideListOfValues) {
		super(parent, id, content);
		assert((content == FilterExpressionOperatorEnum.IN) || (content == FilterExpressionOperatorEnum.NOT_IN));
		this.leftHandSideExpression = leftHandSideExpression;
		this.rightHandSideListOfValues = rightHandSideListOfValues;
	}
	
	public FilterExpressionElement(SushiTreeElement<Serializable> parent, int id, FilterExpressionOperatorEnum content, String leftHandSideExpression, RangeElement rightHandSideRangeOfValues) {
		super(parent, id, content);
		assert((content == FilterExpressionOperatorEnum.IN) || (content == FilterExpressionOperatorEnum.NOT_IN));
		this.leftHandSideExpression = leftHandSideExpression;
		this.rightHandSideRangeOfValues = rightHandSideRangeOfValues;
	}
	
	private void init() {
		this.leftHandSideExpression = new String();
		this.rightHandSideExpression = new String();
		this.rightHandSideListOfValues = new ArrayList<String>();
		this.rightHandSideRangeOfValues = new RangeElement();
		this.rightHandSideRangeBased = true;
	}

	public String getLeftHandSideExpression() {
		return leftHandSideExpression;
	}

	public void setLeftHandSideExpression(String leftHandSideExpression) {
		this.leftHandSideExpression = leftHandSideExpression;
	}

	public String getRightHandSideExpression() {
		return rightHandSideExpression;
	}

	public void setRightHandSideExpression(String rightHandSideExpression) {
		this.rightHandSideExpression = rightHandSideExpression;
	}

	public List<String> getRightHandSideListOfValues() {
		return rightHandSideListOfValues;
	}

	public void setRightHandSideListOfValues(List<String> rightHandSideListOfValues) {
		this.rightHandSideListOfValues = rightHandSideListOfValues;
	}

	public RangeElement getRightHandSideRangeOfValues() {
		return rightHandSideRangeOfValues;
	}

	public void setRightHandSideRangeOfValues(RangeElement rightHandSideRangeOfValues) {
		this.rightHandSideRangeOfValues = rightHandSideRangeOfValues;
	}

	public boolean isRightHandSideRangeBased() {
		return rightHandSideRangeBased;
	}

	public void setRightHandSideRangeBased(boolean rightHandSideRangeBased) {
		this.rightHandSideRangeBased = rightHandSideRangeBased;
	}
	
}
