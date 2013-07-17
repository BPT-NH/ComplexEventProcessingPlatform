package sushi.visualisation;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityTransaction;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Table;

import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.persistence.Persistable;
import sushi.persistence.Persistor;

/**
 * This class saves the configurations for a chart that visualizes event values of an attribute of a certain eventtype.
 * This class is given as an parameterObject to the ChartCreationClass, at the moment either SushiSplatterChartOptions or SushiBarChartOptions.
 */
@Entity
@Table(name = "SushiChartConfiguration")
public class SushiChartConfiguration  extends Persistable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ID;
	
	@JoinColumn(name = "EventType")
	@ManyToOne(cascade = CascadeType.PERSIST)
	private SushiEventType eventType;
	
	@Column(name = "attributeName")
	private String attributeName;

	@Column(name = "attributeType")
	@Enumerated(EnumType.STRING)
	private SushiAttributeTypeEnum attributeType;

	@Column(name = "title")
	private String title;
	
	@Column(name = "TYPE")
	@Enumerated(EnumType.STRING)
	private SushiChartTypeEnum type;

	@Column(name = "rangeSize")
	private Integer rangeSize = 1;

	/**
	 * Default Constructor for JPA
	 */
	public SushiChartConfiguration() {
	}
	
	/**
	 * Creates a new chart configuration.
	 * For integer values (therefore the rangesize).
	 * @param eventType
	 * @param attributeName
	 * @param attributeType
	 * @param title
	 * @param type
	 * @param rangeSize
	 */
	public SushiChartConfiguration(SushiEventType eventType, String attributeName, SushiAttributeTypeEnum attributeType, String title, SushiChartTypeEnum type, Integer rangeSize) {
		this.eventType = eventType;
		this.attributeName = attributeName;
		this.attributeType = attributeType;
		this.title = title;
		this.type = type;
		this.rangeSize = rangeSize;
	}

	/**
	 * Creates a new chart configuration.
	 * For string values (therefore without rangesize).
	 * @param eventType
	 * @param attributeName
	 * @param attributeType
	 * @param title
	 * @param type
	 */
	public SushiChartConfiguration(SushiEventType eventType, String attributeName, SushiAttributeTypeEnum attributeType, String title, SushiChartTypeEnum type) {
		this.eventType = eventType;
		this.attributeName = attributeName;
		this.attributeType = attributeType;
		this.title = title;
		this.type = type;
	}
	
	//Getter and Setter
	
	public Integer getRangeSize() {
		return rangeSize;
	}

	public void setRangeSize(Integer rangeSize) {
		this.rangeSize = rangeSize;
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

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public SushiChartTypeEnum getType() {
		return type;
	}

	public void setType(SushiChartTypeEnum type) {
		this.type = type;
	}
	
	public SushiAttributeTypeEnum getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(SushiAttributeTypeEnum attributeType) {
		this.attributeType = attributeType;
	}

	//JPA-Methods
	
	/**
	 * Finds all chart configurations from database.
	 * @return all chart configurations
	 */
	public static List<SushiChartConfiguration> findAll() {
		Query q = Persistor.getEntityManager().createQuery("SELECT t FROM SushiChartConfiguration t");
		return q.getResultList();
	}
	
	/**
	 * Finds chart configurations by attribute.
	 * @param columnName
	 * @param value
	 * @return chart configurations that matche the attribute condition
	 */
	private static List<SushiChartConfiguration> findByAttribute(String columnName, String value){
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiChartConfiguration WHERE " + columnName + " = '" + value + "'", SushiChartConfiguration.class);
		return query.getResultList();
	}

	/**
	 * Finds all chart configurations for an event type from database.
	 * @param eventType
	 * @return all chart configuration for an event type
	 */
	public static List<SushiChartConfiguration> findByEventType(SushiEventType eventType){
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiChartConfiguration WHERE EventType = " + eventType.getID(), SushiChartConfiguration.class);
		return query.getResultList();
	}

	/**
	 * Finds chart configuration by ID from database.
	 * @param ID
	 * @return chart configuration
	 */
	public static SushiChartConfiguration findByID(int ID){
		List<SushiChartConfiguration> list = SushiChartConfiguration.findByAttribute("ID", new Integer(ID).toString());
		if(list.size() > 0){
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Deletes all chart configuration from the database.
	 */
	public static void removeAll() {
		try {
			EntityTransaction entr = Persistor.getEntityManager().getTransaction();
			entr.begin();
			Query query = Persistor.getEntityManager().createQuery("DELETE FROM SushiChartConfiguration");
			int deleteRecords = query.executeUpdate();
			entr.commit();
			System.out.println(deleteRecords + " records are deleted.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
}
