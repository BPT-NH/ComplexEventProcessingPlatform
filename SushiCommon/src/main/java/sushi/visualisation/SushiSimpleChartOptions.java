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
@Table(name = "SushiSimpleChartOptions")
public class SushiSimpleChartOptions  extends Persistable {

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
	
	//TODO: save user
	
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

	//Default Constructor for JPA
	public SushiSimpleChartOptions() {
	}
	
	public SushiSimpleChartOptions(SushiEventType eventType, String attributeName, SushiAttributeTypeEnum attributeType, String title, SushiChartTypeEnum type, Integer rangeSize) {
		this.eventType = eventType;
		this.attributeName = attributeName;
		this.attributeType = attributeType;
		this.title = title;
		this.type = type;
		this.rangeSize = rangeSize;
	}

	public SushiSimpleChartOptions(SushiEventType eventType, String attributeName, SushiAttributeTypeEnum attributeType, String title, SushiChartTypeEnum type) {
		this.eventType = eventType;
		this.attributeName = attributeName;
		this.attributeType = attributeType;
		this.title = title;
		this.type = type;
	}

	
	//JPA-Methods
	
	public SushiAttributeTypeEnum getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(SushiAttributeTypeEnum attributeType) {
		this.attributeType = attributeType;
	}

	public static List<SushiSimpleChartOptions> findAll() {
		Query q = Persistor.getEntityManager().createQuery("SELECT t FROM SushiSimpleChartOptions t");
		return q.getResultList();
	}
	
	private static List<SushiSimpleChartOptions> findByAttribute(String columnName, String value){
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiSimpleChartOptions WHERE " + columnName + " = '" + value + "'", SushiSimpleChartOptions.class);
		return query.getResultList();
	}

	public static List<SushiSimpleChartOptions> findByEventType(SushiEventType eventType){
		Query query = Persistor.getEntityManager().createNativeQuery("SELECT * FROM SushiSimpleChartOptions WHERE EventType = " + eventType.getID(), SushiSimpleChartOptions.class);
		return query.getResultList();
	}

	
	public static SushiSimpleChartOptions findByID(int ID){
		List<SushiSimpleChartOptions> list = SushiSimpleChartOptions.findByAttribute("ID", new Integer(ID).toString());
		if(list.size() > 0){
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Deletes all users from the database.
	 */
	public static void removeAll() {
		try {
			EntityTransaction entr = Persistor.getEntityManager().getTransaction();
			entr.begin();
			Query query = Persistor.getEntityManager().createQuery("DELETE FROM SushiSimpleChartOptions");
			int deleteRecords = query.executeUpdate();
			entr.commit();
			System.out.println(deleteRecords + " records are deleted.");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	
}
