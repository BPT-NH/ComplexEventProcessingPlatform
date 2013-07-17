package sushi.correlation;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.persistence.Transient;

import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;
import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;

/**
 * 
 * Container object for pairs of attributes. Used for event correlation.
 * Attributes must be from the same type, but may have different names and may belong to different event types.
 * Related to a process.
 *
 */
@Entity
@Table(name = "CorrelationRule")
public class CorrelationRule extends Persistable {

	private static final long serialVersionUID = -1261406813387858839L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	
	@ManyToOne
	@JoinColumn(name = "ProcessID")
	private SushiProcess process;
	
	@ManyToOne
	@JoinColumn(name = "firstAttributeID")
	private SushiAttribute firstAttribute;
	
	@ManyToOne
	@JoinColumn(name = "secondAttributeID")
	private SushiAttribute secondAttribute;
	
	@Transient
	private SushiEventType eventTypeOfFirstAttribute;
	
	@Transient
	private SushiEventType eventTypeOfSecondAttribute;
	
	public CorrelationRule() {
		this.ID = 0;
	}
	
	/**
	 * 
	 * Constructor. Checks for validity of the rule and throws an exception if required.
	 * 
	 * @param firstAttribute
	 * @param secondAttribute
	 * @throws RuntimeException
	 */
	public CorrelationRule(SushiAttribute firstAttribute, SushiAttribute secondAttribute) throws RuntimeException {
		this();
		if (firstAttribute == null && secondAttribute == null) {
			throw new RuntimeException("Correlation rule attributes must not be null.");
		} else if (firstAttribute.getType() != secondAttribute.getType()) {
			throw new RuntimeException("Types of correlation rule attributes are not equal.");
		}
		this.firstAttribute = firstAttribute;
		this.secondAttribute = secondAttribute;
	}
	
	public SushiProcess getProcess() {
		return process;
	}

	public void setProcess(SushiProcess process) {
		this.process = process;
	}

	public SushiAttribute getFirstAttribute() {
		return firstAttribute;
	}

	public void setFirstAttribute(SushiAttribute firstAttribute) {
		this.firstAttribute = firstAttribute;
	}

	public SushiAttribute getSecondAttribute() {
		return secondAttribute;
	}

	public void setSecondAttribute(SushiAttribute secondAttribute) {
		this.secondAttribute = secondAttribute;
	}

	public SushiEventType getEventTypeOfFirstAttribute() {
		return eventTypeOfFirstAttribute;
	}

	public void setEventTypeOfFirstAttribute(SushiEventType eventTypeOfFirstAttribute) {
		this.eventTypeOfFirstAttribute = eventTypeOfFirstAttribute;
	}

	public SushiEventType getEventTypeOfSecondAttribute() {
		return eventTypeOfSecondAttribute;
	}

	public void setEventTypeOfSecondAttribute(SushiEventType eventTypeOfSecondAttribute) {
		this.eventTypeOfSecondAttribute = eventTypeOfSecondAttribute;
	}
	
	public static List<CorrelationRule> findAll() {
		Query query = Persistor.getEntityManager().createQuery("select t from CorrelationRule t", CorrelationRule.class);
		return query.getResultList();
	}

	@Override
	public CorrelationRule save() {
		firstAttribute.addToCorrelationRulesFirst(this);
		secondAttribute.addToCorrelationRulesSecond(this);
		return (CorrelationRule) super.save();
	}
	
	@Override
	public String toString() {
		return firstAttribute.getQualifiedAttributeName() + "=" + secondAttribute.getQualifiedAttributeName();
	}

	@Override
	public int getID() {
		return ID;
	}
}
