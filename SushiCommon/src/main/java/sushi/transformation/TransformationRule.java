package sushi.transformation;

import java.util.HashMap;
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
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import sushi.event.SushiEventType;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;
import sushi.transformation.collection.SushiPatternTree;
import sushi.transformation.element.externalknowledge.ExternalKnowledgeExpressionSet;

/**
 * 
 * Contains all objects that are required for event transformation. 
 * Transformation rules are unique by the event type of the transformed events 
 * and the user-defined title of the transformation rule.
 *
 */
@Entity
@Table(
	name = "TransformationRule", 
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"eventType", "title"})
	}
)
public class TransformationRule extends Persistable {
	
	private static final long serialVersionUID = 6601905480228417174L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transformationRuleID")
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
	
	@OneToMany(mappedBy = "transformationRule", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@MapKey(name = "attributeExpression")
	private Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersAndExternalKnowledgeExpressionSets;
	
	public TransformationRule() {
		this.ID = 0;
		this.title = "";
		this.eventType = null;
		this.query = "";
		this.patternTree = null;
		this.attributeIdentifiersAndExpressions = new HashMap<String, String>();
		this.attributeIdentifiersAndExternalKnowledgeExpressionSets = new HashMap<String, ExternalKnowledgeExpressionSet>();
	}
	
	/**
	 * Constructor for transformation rules created in the basic rule editor.
	 * 
	 * @param eventType event type of the transformed events
	 * @param title name of the transformation rule
	 * @param query Esper query that is used to listen for incoming events and to create the transformed events
	 */
	public TransformationRule(SushiEventType eventType, String title, String query) {
		this();
		this.eventType = eventType;
		this.title = title;
		this.query = query;
	}
	
	/**
	 * Constructor for transformation rules created in the advanced rule editor.
	 * 
	 * @param eventType event type of the transformed events
	 * @param title name of the transformation rule
	 * @param patternTree pattern that is used to listen for events, built up from the provided elements
	 * @param attributeIdentifiersAndExpressions pairs of attribute identifiers and expressions - determines what values are stored in the transformed events
	 * @param attributeIdentifiersAndExpressionSets pairs of attribute identifiers and sets of expressions determining the fetch of external knowledge
	 */
	public TransformationRule(SushiEventType eventType, String title, SushiPatternTree patternTree, Map<String, String> attributeIdentifiersAndExpressions, Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersAndExpressionSets) {
		this(eventType, title, null);
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
		if (query == null) {
			query = EsperTransformationRuleParser.getInstance().parseRule(patternTree, attributeIdentifiersAndExpressions, attributeIdentifiersAndExternalKnowledgeExpressionSets); 
		}
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

	public void setAttributeIdentifiersAndExpressions(Map<String, String> attributeIdentifiersAndExpressions) {
		this.attributeIdentifiersAndExpressions = attributeIdentifiersAndExpressions;
	}

	public Map<String, ExternalKnowledgeExpressionSet> getAttributeIdentifiersWithExternalKnowledge() {
		return attributeIdentifiersAndExternalKnowledgeExpressionSets;
	}

	public void setAttributeIdentifiersAndExpressionSets(Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersAndExpressionSets) {
		this.attributeIdentifiersAndExternalKnowledgeExpressionSets = attributeIdentifiersAndExpressionSets;
	}

	@Override
	public TransformationRule save() {
		for (String attributeIdentifer : attributeIdentifiersAndExternalKnowledgeExpressionSets.keySet()) {
			if (attributeIdentifiersAndExternalKnowledgeExpressionSets.get(attributeIdentifer).getTransformationRule() == null) {
				attributeIdentifiersAndExternalKnowledgeExpressionSets.get(attributeIdentifer).setTransformationRule(this);
			}
		}
		return (TransformationRule) super.save();
	}
	
	@Override
	public TransformationRule remove() {
		return (TransformationRule) super.remove();
	}
	
	public static List<TransformationRule> findAll() {
		Query q = Persistor.getEntityManager().createQuery("SELECT t FROM TransformationRule t", TransformationRule.class);
		return q.getResultList();
	}
	
	public static List<TransformationRule> findByEventType(SushiEventType eventType) {
		EntityManager em = Persistor.getEntityManager();
		em.clear();
		/* the ID of the eventType is stored in the database, not the whole SushiEventType */
		Query query = em.createNativeQuery("SELECT * FROM TransformationRule WHERE EventType = " + eventType.getID(), TransformationRule.class);
		try {
			return (List<TransformationRule>) query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public static TransformationRule findByEventTypeAndTitle(String eventTypeName, String title) {
		EntityManager em = Persistor.getEntityManager();
		/* the ID of the eventType is stored in the database, not the whole SushiEventType */
		em.clear();
		Query query = em.createNativeQuery("SELECT * FROM TransformationRule WHERE EventType = (SELECT ID FROM EventType WHERE TypeName = '" + eventTypeName + "') AND Title = '" + title + "'", TransformationRule.class);
		try {
			return (TransformationRule) query.getResultList().get(0);
		} catch (Exception e) {
			return null;
		}
	}
}
