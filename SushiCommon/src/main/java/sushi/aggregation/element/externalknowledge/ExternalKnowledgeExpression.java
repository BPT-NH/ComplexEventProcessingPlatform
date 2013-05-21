package sushi.aggregation.element.externalknowledge;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.persistence.Persistable;

@Entity
@Table(name = "ExternalKnowledgeExpression")
public class ExternalKnowledgeExpression extends Persistable {

	private static final long serialVersionUID = -7637140960882882120L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	
	@ManyToOne
	@JoinColumn(name = "EventType")
	private SushiEventType eventType;
	
	@ManyToOne
	@JoinColumn(name = "DesiredAttribute")
	private SushiAttribute desiredAttribute;
	
	@ElementCollection
	@MapKeyColumn(name = "attribute")
	@Column(name = "value")
	@CollectionTable(name="criteriaAttributesAndValues", joinColumns = @JoinColumn(name="criteriaAttributesAndValuesID"))
	private Map<String, String> criteriaAttributesAndValues;
	
	public ExternalKnowledgeExpression() {
		this.ID = 0;
		this.eventType = null;
		this.desiredAttribute = null;
		this.criteriaAttributesAndValues = new HashMap<String, String>();
	}
	
	public ExternalKnowledgeExpression(SushiEventType eventType, SushiAttribute desiredAttribute, Map<String, String> criteriaAttributesAndValues) {
		this();
		this.eventType = eventType;
		this.desiredAttribute = desiredAttribute;
		this.criteriaAttributesAndValues = criteriaAttributesAndValues;
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

	public SushiAttribute getDesiredAttribute() {
		return desiredAttribute;
	}

	public void setDesiredAttribute(SushiAttribute desiredAttribute) {
		this.desiredAttribute = desiredAttribute;
	}

	public Map<String, String> getCriteriaAttributesAndValues() {
		return criteriaAttributesAndValues;
	}

	public void setCriteriaAttributesAndValues(Map<String, String> criteriaAttributesAndValues) {
		this.criteriaAttributesAndValues = criteriaAttributesAndValues;
	}

	@Override
	public ExternalKnowledgeExpression save() {
		return (ExternalKnowledgeExpression) super.save();
	}
	
	@Override
	public ExternalKnowledgeExpression remove() {
		return (ExternalKnowledgeExpression) super.remove();
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
