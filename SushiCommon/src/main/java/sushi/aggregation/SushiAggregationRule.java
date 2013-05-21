package sushi.aggregation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sushi.aggregation.collection.SushiPatternTree;
import sushi.aggregation.element.EventTypeElement;
import sushi.aggregation.element.FilterExpressionConnectorElement;
import sushi.aggregation.element.FilterExpressionConnectorEnum;
import sushi.aggregation.element.FilterExpressionElement;
import sushi.aggregation.element.FilterExpressionOperatorEnum;
import sushi.aggregation.element.PatternOperatorElement;
import sushi.aggregation.element.PatternOperatorEnum;
import sushi.aggregation.element.RangeElement;
import sushi.aggregation.element.externalknowledge.ExternalKnowledgeExpressionSet;
import sushi.event.SushiEventType;
import sushi.event.collection.SushiMapTree;
import sushi.event.collection.SushiTreeElement;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;

@Entity
@Table(
	name = "SushiAggregationRule", 
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"eventType", "title"})
	}
)
public class SushiAggregationRule extends Persistable {
	
	private static final long serialVersionUID = 6601905480228417174L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "aggregationRuleID")
	private int ID;
	
	@JoinColumn(name = "EventType")
	@ManyToOne
	private SushiEventType eventType;
	
	@Column(name = "Title")
	private String title;
	
	@Column(name = "Query", length=15000)
	private String query;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="PatternTreeID")
	private SushiPatternTree patternTree;
	
	@ElementCollection
	@MapKeyColumn(name = "attributeIdentifier")
	@CollectionTable(name="AttributeIdentifiersAndExpressions", joinColumns = @JoinColumn(name="AttributeIdentifiersAndExpressionsID"))
	private Map<String, String> attributeIdentifiersAndExpressions;

//	@JoinColumn(name="aggregationRuleID", referencedColumnName="aggregationRuleID")
	@OneToMany(mappedBy = "aggregationRule", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@MapKey(name = "attributeExpression")
	private Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersAndExternalKnowledgeExpressionSets;
	
	public SushiAggregationRule() {
		this.ID = 0;
		this.title = "";
		this.eventType = null;
		this.query = "";
		this.patternTree = null;
		this.attributeIdentifiersAndExpressions = new HashMap<String, String>();
		this.attributeIdentifiersAndExternalKnowledgeExpressionSets = new HashMap<String, ExternalKnowledgeExpressionSet>();
	}
	
	public SushiAggregationRule(SushiEventType eventType, String title, String query) {
		this();
		this.eventType = eventType;
		this.title = title;
		this.query = query;
	}
	
	public SushiAggregationRule(SushiEventType eventType, String title, String query, SushiPatternTree patternTree, Map<String, String> attributeIdentifiersAndExpressions, Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersAndExpressionSets) {
		this(eventType, title, query);
		this.patternTree = patternTree;
		this.attributeIdentifiersAndExpressions = attributeIdentifiersAndExpressions;
		this.attributeIdentifiersAndExternalKnowledgeExpressionSets = attributeIdentifiersAndExpressionSets;
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public SushiEventType getEventType() {
		return eventType;
	}

	public void setEventType(SushiEventType eventType) {
		this.eventType = eventType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	public SushiPatternTree getPatternTree() {
		return patternTree;
	}

	public void setPatternTree(SushiPatternTree patternTree) {
		this.patternTree = patternTree;
	}

	public Map<String, String> getAttributeIdentifiersAndExpressions() {
		return attributeIdentifiersAndExpressions;
	}

	public void setAttributeIdentifiersAndExpressions(
			Map<String, String> attributeIdentifiersAndExpressions) {
		this.attributeIdentifiersAndExpressions = attributeIdentifiersAndExpressions;
	}

	public Map<String, ExternalKnowledgeExpressionSet> getAttributeIdentifiersWithExternalKnowledge() {
		return attributeIdentifiersAndExternalKnowledgeExpressionSets;
	}

	public void setAttributeIdentifiersAndExpressionSets(
			Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersAndExpressionSets) {
		this.attributeIdentifiersAndExternalKnowledgeExpressionSets = attributeIdentifiersAndExpressionSets;
	}

	@Override
	public SushiAggregationRule save() {
		for (String attributeIdentifer : attributeIdentifiersAndExternalKnowledgeExpressionSets.keySet()) {
			if (attributeIdentifiersAndExternalKnowledgeExpressionSets.get(attributeIdentifer).getAggregationRule() == null) {
				attributeIdentifiersAndExternalKnowledgeExpressionSets.get(attributeIdentifer).setAggregationRule(this);
			}
		}
		return (SushiAggregationRule) super.save();
	}
	
	@Override
	public SushiAggregationRule remove() {
		return (SushiAggregationRule) super.remove();
	}
	
	public static List<SushiAggregationRule> findAll() {
		Query q = Persistor.getEntityManager().createQuery("SELECT t FROM SushiAggregationRule t", SushiAggregationRule.class);
		return q.getResultList();
	}
	
	public static List<SushiAggregationRule> findByEventType(SushiEventType eventType) {
		EntityManager em = Persistor.getEntityManager();
		em.clear();
		/* the ID of the eventType is stored in the database, not the whole SushiEventType */
		Query query = em.createNativeQuery("SELECT * FROM SushiAggregationRule WHERE EventType = " + eventType.getID(), SushiAggregationRule.class);
		try {
			return (List<SushiAggregationRule>) query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public static SushiAggregationRule findByEventTypeAndTitle(String eventTypeName, String title) {
		EntityManager em = Persistor.getEntityManager();
		/* the ID of the eventType is stored in the database, not the whole SushiEventType */
		em.clear();
		Query query = em.createNativeQuery("SELECT * FROM SushiAggregationRule WHERE EventType = (SELECT ID FROM EventType WHERE TypeName = '" + eventTypeName + "') AND Title = '" + title + "'", SushiAggregationRule.class);
		try {
			return (SushiAggregationRule) query.getResultList().get(0);
		} catch (Exception e) {
			return null;
		}
	}

	public static String generateAggregationRule(Map<String, String> attributeIdentifiersAndExpressions, SushiPatternTree patternTree) {
		
		assert(patternTree.getRoots().size() == 1);
		
		// TODO: where to put this method?!
		StringBuffer query = new StringBuffer();
		
		// SELECT part
		query.append("SELECT");
		Iterator<String> iteratorForAttributeIdentifiers = attributeIdentifiersAndExpressions.keySet().iterator();
		while (iteratorForAttributeIdentifiers.hasNext()) {
			String attributeIdentifier = iteratorForAttributeIdentifiers.next();
			query.append(" (" + attributeIdentifiersAndExpressions.get(attributeIdentifier) + ") AS " + attributeIdentifier);
			if (iteratorForAttributeIdentifiers.hasNext()) {
				query.append(",");
			}
		}
		
		// FROM PATTERN part
		
		SushiTreeElement<Serializable> rootElement = patternTree.getRoots().get(0);
		String pattern = buildPatternForQuery(rootElement);
		
		query.append(" FROM Pattern [" + pattern + "]");
		
		return query.toString();
	}

	private static String buildPatternForQuery(SushiTreeElement<Serializable> element) {

		if (element instanceof PatternOperatorElement) {
			PatternOperatorElement poElement = ((PatternOperatorElement) element);
			PatternOperatorEnum poType = (PatternOperatorEnum) poElement.getValue();
			if (poElement.getChildren().size() == 2) {
				String leftHandSideExpression = buildPatternForQuery(poElement.getChildren().get(0));
				String rightHandSideExpression = buildPatternForQuery(poElement.getChildren().get(1));
				if (poType == PatternOperatorEnum.UNTIL) {
					RangeElement rangeElement = poElement.getRangeElement();
					StringBuffer sb = new StringBuffer();
					sb.append("(");
					if (rangeElement.getLeftEndpoint() >= 0 || rangeElement.getRightEndpoint() >= 0) {
						sb.append("[");
						sb.append(rangeElement.getLeftEndpoint() < 0 ? "" : rangeElement.getLeftEndpoint());
						sb.append(":");
						sb.append(rangeElement.getRightEndpoint() < 0 ? "" : rangeElement.getRightEndpoint());
						sb.append("] ");
					}
					sb.append(leftHandSideExpression + " UNTIL " + rightHandSideExpression);
					sb.append(")");
					return sb.toString();
				} else if (poType == PatternOperatorEnum.AND) {
					return "(" + leftHandSideExpression + " AND " + rightHandSideExpression + ")";
				} else if (poType == PatternOperatorEnum.OR) {
					return "(" + leftHandSideExpression + " OR " + rightHandSideExpression + ")";
				} else if (poType == PatternOperatorEnum.FOLLOWED_BY) {
					return "(" + leftHandSideExpression + " -> " + rightHandSideExpression + ")";
				}
			} else if (poElement.getChildren().size() == 1) {
				String expression = buildPatternForQuery(poElement.getChildren().get(0));
				if (poType == PatternOperatorEnum.EVERY) {
					return "(EVERY " + expression + ")";
//				} else if (poType == PatternOperatorEnum.EVERY_DISTINCT) {
//					TODO!
//					return "(EVERY-DISTINCT " + buildPatternForQuery(poElement.getChildren().get(0)) + ")";
				} else if (poType == PatternOperatorEnum.REPEAT) {
					RangeElement rangeElement = poElement.getRangeElement();
					return "([" + rangeElement.getLeftEndpoint() + "] " + expression + ")";
				} else if (poType == PatternOperatorEnum.NOT) {
					return "(NOT " + expression + ")";
				}
			}
		} else if (element instanceof EventTypeElement) {
			EventTypeElement etElement = ((EventTypeElement) element);
			StringBuffer sb = new StringBuffer();
			if (etElement.hasAlias()) {
				sb.append("(" + etElement.getAlias() + "=" + ((SushiEventType) etElement.getValue()).getTypeName());
			} else {
				sb.append("(" + ((SushiEventType) etElement.getValue()).getTypeName());
			}
			if (etElement.hasChildren()) {
				sb.append("(");
				Iterator<SushiTreeElement<Serializable>> iterator = element.getChildren().iterator();
				while (iterator.hasNext()) {
					SushiTreeElement<Serializable> currentElement = iterator.next();
					sb.append(buildPatternForQuery(currentElement));
					if (iterator.hasNext()) {
						sb.append(", ");
					}
				}
				sb.append(")");
			}
			sb.append(")");
			return sb.toString();
		} else if (element instanceof FilterExpressionConnectorElement) {
			FilterExpressionConnectorElement fecElement = (FilterExpressionConnectorElement) element;
			FilterExpressionConnectorEnum fecType = (FilterExpressionConnectorEnum) fecElement.getValue();
			if (fecType == FilterExpressionConnectorEnum.AND) {
				String leftHandSideExpression = buildPatternForQuery(fecElement.getChildren().get(0));
				String rightHandSideExpression = buildPatternForQuery(fecElement.getChildren().get(1));
				return "(" + leftHandSideExpression + " AND " + rightHandSideExpression + ")";
			} else if (fecType == FilterExpressionConnectorEnum.OR) {
				String leftHandSideExpression = buildPatternForQuery(fecElement.getChildren().get(0));
				String rightHandSideExpression = buildPatternForQuery(fecElement.getChildren().get(1));
				return "(" + leftHandSideExpression + " OR " + rightHandSideExpression + ")";
			} else if (fecType == FilterExpressionConnectorEnum.NOT) {
				String expression = buildPatternForQuery(fecElement.getChildren().get(0));
				return "NOT (" + expression + ")";
			}
		} else if (element instanceof FilterExpressionElement) {
			FilterExpressionElement feElement = (FilterExpressionElement) element;
			FilterExpressionOperatorEnum feType = (FilterExpressionOperatorEnum) feElement.getValue();
			StringBuffer sb = new StringBuffer();
			sb.append("(");
			sb.append("(" + feElement.getLeftHandSideExpression() + ") " + feType.getValue() + " ");
			if (feType == FilterExpressionOperatorEnum.IN || feType == FilterExpressionOperatorEnum.NOT_IN) {
				if (feElement.isRightHandSideRangeBased()) {
					RangeElement rangeElement = feElement.getRightHandSideRangeOfValues();
					sb.append(rangeElement.isLeftEndpointOpen() ? "(" : "[");
					sb.append(rangeElement.getLeftEndpoint() + ":" + rangeElement.getRightEndpoint());
					sb.append(rangeElement.isRightEndpointOpen() ? ")" : "]");
				} else {
					sb.append("(");
					Iterator<String> iterator = feElement.getRightHandSideListOfValues().iterator();
					while (iterator.hasNext()) {
						sb.append(iterator.next());
						if (iterator.hasNext()) {
							sb.append(", ");
						}
					}
					sb.append(")");
				}
			} else {
				sb.append("(" + feElement.getRightHandSideExpression() + ")");
			}
			sb.append(")");
			return sb.toString();
		}
		return "";
	}
}
