package sushi.notification;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;

import sushi.event.SushiEvent;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;

/**
 * This class represents a condition for an event, saying that a certain attribute equals a certain value.
 * The event either matches a condition or not.
 */
@Entity
@Table(name = "SushiCondition")
public class SushiCondition extends Persistable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int ID;
	
	@Column(name = "attribute")
	private String attribute;
	@Column(name = "value")
	private String value;
	
	/**
	 * Creates a condition.
	 * @param attribute
	 * @param conditionValue
	 */
	public SushiCondition(String attribute, String conditionValue) {
		this.attribute = attribute;
		this.value = conditionValue;
	}
	
	/**
	 * Default constructor for JPA
	 */
	public SushiCondition() {
		this.attribute = null;
		this.value = null;
	}

	/**
	 * Checks whether an event matches a condition
	 * @param event
	 * @return whether the condition matches the event
	 */
	public boolean matches(SushiEvent event) {
		Serializable eventValue = event.getValues().get(attribute);
		try {
			String stringValue = (String) eventValue;
			return stringValue.equals(value);
		}
		catch (Exception e) {
			try {
				//try for integer
				int intValue = (Integer) eventValue;
				return intValue == Integer.parseInt(value);
			}
			catch (Exception e1) {
				return false;		
			}
		}
	}

	/**
	 * generates a string representing the condition
	 * @return string representation of condition
	 */
	public String getConditionString() {
		if (attribute== null || value == null || attribute == "" || value == "") return "";
		return attribute + "=" + value;
	}

	@Override
	public int getID() {
		return ID;
	}

	
	//JPA-Methods
	
	/**
	 * retrieves all conditions from database
	 * @return all conditions
	 */
	public static List<SushiCondition> findAll() {
		Query q = Persistor.getEntityManager().createQuery("SELECT t FROM SushiCondition t");
		return q.getResultList();
	}
	
	/**
	 * finds condition with ID
	 * @param ID
	 * @return condition
	 */
	public static SushiCondition findByID(int ID){
		return Persistor.getEntityManager().find(SushiCondition.class, ID);
	}
		
}
