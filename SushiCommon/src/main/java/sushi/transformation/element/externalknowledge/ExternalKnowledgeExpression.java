package sushi.transformation.element.externalknowledge;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
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

/**
 * An external knowledge expression determines the value to be fetched from the database.
 * External knowledge must be stored in an event format before usage.
 * Values from occurred events may be retrieved by a external knowledge expression as well.
 * The value is determined by a event type, the attribute of the value and criteria attributes and values 
 * to find the right value.
 *
 */
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
	
	/**
	 * Constructor.
	 * 
	 * @param eventType the events of the event type serve as external knowledge
	 * @param desiredAttribute the value of the desired attribute belonging to the event type will be fetched
	 * @param criteriaAttributesAndValues pairs of attributes and values that narrow down the choice of events from which the value can be fetched
	 */
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
}
