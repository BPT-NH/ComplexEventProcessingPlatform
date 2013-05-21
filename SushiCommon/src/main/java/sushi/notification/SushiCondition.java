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

@Entity
@Table(name = "SushiCondition")
public class SushiCondition extends Persistable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;

	
	@Column(name = "attribute")
	private String attribute;
	@Column(name = "value")
	private String value;
	
	public SushiCondition(String attribute, String selectedConditionValue) {
		this.attribute = attribute;
		this.value = selectedConditionValue;
	}
	
	/*
	 * Default constructor for JPA
	 */
	public SushiCondition() {
		this.attribute = null;
		this.value = null;
	}

	public boolean matches(SushiEvent event) {
		Serializable eventValue = event.getValues().get(attribute);
		try {
			String stringValue = (String) eventValue;
			return stringValue.equals(value);
		}
		catch (Exception e) {
			try {
				int intValue = (Integer) eventValue;
				return intValue == Integer.parseInt(value);
			}
			catch (Exception e1) {
				return false;		
			}
		}
	}
	
	public static List<SushiCondition> findAll() {
		Query q = Persistor.getEntityManager().createQuery("SELECT t FROM SushiCondition t");
		return q.getResultList();
	}
		
	public static SushiCondition findByID(int ID){
		return Persistor.getEntityManager().find(SushiCondition.class, ID);
	}
		
	public String getConditionString() {
		if (attribute== null || value == null || attribute == "" || value == "") return "";
		return attribute + "=" + value;
	}
	
}
