package sushi.transformation.element.externalknowledge;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.persistence.Persistable;
import sushi.transformation.TransformationRule;

/**
 * Container object for external knowledge expressions.
 * References the attribute (using its attribute expression) of the event type for transformed events.
 * It also contains the type of the attribute and a default value.
 * The default value is used if no external knowledge expression can retrieve a value for the specified attribute.
 *
 */
@Entity
@Table(name = "ExternalKnowledgeExpressionSet")
public class ExternalKnowledgeExpressionSet extends Persistable {

	private static final long serialVersionUID = -7637140960882882120L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ExpressionSetID")
	private int ID;
	
	@Column(name = "attributeExpression")
	private String attributeExpression;
	
	@ManyToOne
	private TransformationRule transformationRule;
	
	@Column(name = "resultingType")
	private SushiAttributeTypeEnum resultingType;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="ExpressionSetID", referencedColumnName="ExpressionSetID")
	private List<ExternalKnowledgeExpression> externalKnowledgeExpressions;
	
	@Column(name = "defaultValue")
	private String defaultValue;
	
	public ExternalKnowledgeExpressionSet() {
		this.ID = 0;
		this.attributeExpression = null;
		this.resultingType = null;
		this.externalKnowledgeExpressions = new ArrayList<ExternalKnowledgeExpression>();
		this.defaultValue = null;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param resultingType the event type for the transformed events
	 * @param attributeExpression the attribute expression of the attribute of the event type for the transformed events
	 */
	public ExternalKnowledgeExpressionSet(SushiAttributeTypeEnum resultingType, String attributeExpression) {
		this();
		this.resultingType = resultingType;
		this.attributeExpression = attributeExpression;
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public SushiAttributeTypeEnum getResultingType() {
		return resultingType;
	}

	public void setResultingType(SushiAttributeTypeEnum resultingType) {
		this.resultingType = resultingType;
	}

	public List<ExternalKnowledgeExpression> getExternalKnowledgeExpressions() {
		return externalKnowledgeExpressions;
	}

	public void setExternalKnowledgeExpressions(List<ExternalKnowledgeExpression> externalKnowledgeExpressions) {
		this.externalKnowledgeExpressions = externalKnowledgeExpressions;
	}
	
	public String getAttributeExpression() {
		return attributeExpression;
	}

	public void setAttributeExpression(String attributeExpression) {
		this.attributeExpression = attributeExpression;
	}

	public TransformationRule getTransformationRule() {
		return transformationRule;
	}

	public void setTransformationRule(TransformationRule transformationRule) {
		this.transformationRule = transformationRule;
	}

	public boolean addExpression(ExternalKnowledgeExpression expression) {
		return externalKnowledgeExpressions.add(expression);
	}
	
	public boolean removeExpression(ExternalKnowledgeExpression expression) {
		return externalKnowledgeExpressions.remove(expression);
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public ExternalKnowledgeExpressionSet save() {
		return (ExternalKnowledgeExpressionSet) super.save();
	}
	
	@Override
	public ExternalKnowledgeExpressionSet remove() {
		return (ExternalKnowledgeExpressionSet) super.remove();
	}
}
