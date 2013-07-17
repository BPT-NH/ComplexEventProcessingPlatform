package sushi.application.pages.eventrepository.model;

import java.io.Serializable;

/**
 * This class centralizes methods to filter elements in the EventRepository.
 * @author micha
 */
public abstract class AbstractFilter implements Serializable {

	private static final long serialVersionUID = 1L;
	protected String filterValue;
	protected String filterCriteria;
	protected String filterCondition;
	
	/**
	 * Constructor for the filter with the filter values.
	 * @param filterCriteria
	 * @param filterCondition
	 * @param filterValue
	 */
	public AbstractFilter(String filterCriteria, String filterCondition, String filterValue) {
		super();
		this.filterCondition = filterCondition;
		this.filterCriteria = filterCriteria;
		this.filterValue = filterValue;
	}
	
	/**
	 * Constructor for the filter without filter values.
	 * @param filterCriteria
	 * @param filterCondition
	 * @param filterValue
	 */
	public AbstractFilter() {
		super();
	}
	
	/**
	 * Returns the filter value, which is a specific value for an attribute of the current element.
	 * @return
	 */
	public String getFilterValue() {
		return filterValue;
	}
	
	/**
	 * Sets the filter value, which is a specific value for an attribute of the current element.
	 * @return
	 */
	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}
	
	/**
	 * Returns the filter criteria, which is a specific attribute of the current element.
	 * @return
	 */
	public String getFilterCriteria() {
		return filterCriteria;
	}
	
	/**
	 * Sets the filter criteria, which is a specific attribute of the current element.
	 * @return
	 */
	public void setFilterCriteria(String filterCriteria) {
		this.filterCriteria = filterCriteria;
	}
	
	/**
	 * Returns the filter condition, which is a comparison operator like =, < or >.
	 * @return
	 */
	public String getFilterCondition() {
		return filterCondition;
	}
	
	/**
	 * Sets the filter condition, which is a comparison operator like =, < or >.
	 * @return
	 */
	public void setFilterCondition(String filterCondition) {
		this.filterCondition = filterCondition;
	}

}
