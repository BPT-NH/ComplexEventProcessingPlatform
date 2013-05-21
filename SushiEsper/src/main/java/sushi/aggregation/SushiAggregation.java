package sushi.aggregation;

import sushi.esper.SushiEsper;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EPStatement;

public class SushiAggregation {
	
	private static SushiAggregation instance = null;
	SushiEsper esper;
	EPAdministrator esperAdministrator;
	
	public SushiAggregation() {
		esper = SushiEsper.getInstance();
		esperAdministrator = esper.getEsperAdministrator();
		initializeWithAggregationRulesFromDB();
	}
	
	public static SushiAggregation getInstance() {
		if (instance == null) {
                instance = new SushiAggregation();
            }		
		return instance;
	}
	
	public static boolean instanceIsCleared() {
		return (instance == null);
	}
	
	public static void clearInstance() {
		instance = null;
	}
	
	public SushiAggregationListener addToEsper(SushiAggregationRule aggregationRule) throws EPException {
		//TODO: create query a bit more intelligently

		/* NOTE: one statement for each aggregation rule
		 * 
		 * other option would be: 
		 * 		if the aggregation rule is equal an existing one, 
		 * 		a second listener (probably aggregating to another event type but with the same attributes)
		 * 		would be added to the existing statement
		 */
		EPStatement newStatement = esperAdministrator.createEPL(aggregationRule.getQuery(), generateStatementName(aggregationRule));
		SushiAggregationListener listener = new SushiAggregationListener(esper, aggregationRule.getEventType());
		newStatement.addListener(listener);
		return listener;
	}
	
	public boolean removeFromEsper(SushiAggregationRule aggregationRule) throws EPException {
		EPStatement statement = esperAdministrator.getStatement(generateStatementName(aggregationRule));
		if (statement != null) {
			// statement has only one listener - see comment in addToEsper(...)
			statement.removeAllListeners();
			statement.destroy();
//			statement.removeListener(aggregationRule.getAggregationListener());
//			if (!statement.getUpdateListeners().hasNext()) {
//				statement.destroy();
//			}
			return true;
		} else {
			return false;
		}
	}

	private String generateStatementName(SushiAggregationRule aggregationRule) {
		return ("aggregation_" + aggregationRule.getEventType().getTypeName() + "_" + aggregationRule.getTitle()).toLowerCase();
	}

	public SushiEsper getEsper() {
		return esper;
	}

	public void setEsper(SushiEsper esper) {
		this.esper = esper;
	}

	public EPAdministrator getEsperAdministrator() {
		return esperAdministrator;
	}

	public void setEsperAdministrator(EPAdministrator esperAdministrator) {
		this.esperAdministrator = esperAdministrator;
	}

	public void initializeWithAggregationRulesFromDB() {
		for (SushiAggregationRule aggregationRule: SushiAggregationRule.findAll()) {
			System.out.println("Registered aggregation rule '" + aggregationRule.getTitle() + "' for event type '" + aggregationRule.getEventType().getTypeName() + "' from database.");
			addToEsper(aggregationRule);
		}
	}

}
