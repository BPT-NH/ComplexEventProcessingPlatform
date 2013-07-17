package sushi.monitoring.querycreation;

/**
 * This interface should be implemented from classes, which tests the creation of pattern queries.
 * @author micha
 */
public interface IQueryCreationTest {
	
	/**
	 * Tests the import of a BPMN process model for the current test.
	 */
	public void testImport();
	
	/**
	 * Tests the creation of queries for the current BPMN process model.
	 */
	public void testQueryCreation();
	
}
