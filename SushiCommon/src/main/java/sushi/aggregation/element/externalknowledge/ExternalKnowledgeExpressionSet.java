package sushi.aggregation.element.externalknowledge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import sushi.aggregation.SushiAggregationRule;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.persistence.Persistable;

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
	private SushiAggregationRule aggregationRule;
	
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

	public SushiAggregationRule getAggregationRule() {
		return aggregationRule;
	}

	public void setAggregationRule(SushiAggregationRule aggregationRule) {
		this.aggregationRule = aggregationRule;
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
	
	public String generateExpressionForAggregationRule() {
		String externalKnowledgeFetchMethodName = null;
		if (resultingType == SushiAttributeTypeEnum.STRING) {
			externalKnowledgeFetchMethodName  = "stringValueFromEvent";
		} else if (resultingType == SushiAttributeTypeEnum.INTEGER) {
			externalKnowledgeFetchMethodName  = "integerValueFromEvent";
		} else if (resultingType == SushiAttributeTypeEnum.DATE) {
			externalKnowledgeFetchMethodName  = "dateValueFromEvent";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("coalesce(");
		for (ExternalKnowledgeExpression expression : externalKnowledgeExpressions) {
			sb.append(externalKnowledgeFetchMethodName + "(");
			sb.append("'" + expression.getEventType().getTypeName() + "', ");
			sb.append("'" + expression.getDesiredAttribute().getAttributeExpression() + "', {");
			Iterator<String> iterator = expression.getCriteriaAttributesAndValues().keySet().iterator();
			while (iterator.hasNext()) {
				String criteriaAttributeExpression = iterator.next();
				sb.append("'" + criteriaAttributeExpression + "', " + expression.getCriteriaAttributesAndValues().get(criteriaAttributeExpression));
				if (iterator.hasNext()) {
					sb.append(", ");
				}
			}
			sb.append("}), ");
		}
		sb.append(defaultValue);
		sb.append(")");
		return sb.toString();
	}
	
//	public static List<ExternalKnowledgeCondition> findAll() {
//		Query q = Persistor.getEntityManager().createQuery("SELECT t FROM ExternalKnowledgeCondition t", ExternalKnowledgeCondition.class);
//		return q.getResultList();
//	}
//	
//	public static List<ExternalKnowledgeCondition> findByEventType(SushiEventType eventType) {
//		EntityManager em = Persistor.getEntityManager();
//		em.clear();
//		/* the ID of the eventType is stored in the database, not the whole SushiEventType */
//		Query query = em.createNativeQuery("SELECT * FROM ExternalKnowledgeCondition WHERE EventType = " + eventType.getID(), ExternalKnowledgeCondition.class);
//		try {
//			return (List<ExternalKnowledgeCondition>) query.getResultList();
//		} catch (Exception e) {
//			return null;
//		}
//	}
//
//	public static ExternalKnowledgeCondition findByEventTypeAndTitle(String eventTypeName, String title) {
//		EntityManager em = Persistor.getEntityManager();
//		/* the ID of the eventType is stored in the database, not the whole SushiEventType */
//		em.clear();
//		Query query = em.createNativeQuery("SELECT * FROM SushiAggregationRule WHERE EventType = (SELECT ID FROM EventType WHERE TypeName = '" + eventTypeName + "') AND Title = '" + title + "'", ExternalKnowledgeCondition.class);
//		try {
//			return (ExternalKnowledgeCondition) query.getResultList().get(0);
//		} catch (Exception e) {
//			return null;
//		}
//	}
}
