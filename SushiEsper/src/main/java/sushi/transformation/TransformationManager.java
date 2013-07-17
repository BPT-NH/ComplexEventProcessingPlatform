package sushi.transformation;

import java.util.Iterator;
import java.util.Map;

import sushi.esper.SushiStreamProcessingAdapter;
import sushi.event.SushiEventType;
import sushi.transformation.TransformationRule;
import sushi.transformation.collection.SushiPatternTree;
import sushi.transformation.element.EventTypeElement;
import sushi.transformation.element.FilterExpressionElement;
import sushi.transformation.element.FilterExpressionOperatorEnum;
import sushi.transformation.element.PatternOperatorElement;
import sushi.transformation.element.PatternOperatorEnum;
import sushi.transformation.element.RangeElement;
import sushi.transformation.element.externalknowledge.ExternalKnowledgeExpression;
import sushi.transformation.element.externalknowledge.ExternalKnowledgeExpressionSet;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPException;
import com.espertech.esper.client.EPStatement;

/**
 * Handles the transformation of events. Checks transformations rules for validity 
 * and registers and removes them at/from the stream processing adapter.
 * 
 */
public class TransformationManager {
	
	private static TransformationManager instance = null;
	SushiStreamProcessingAdapter esper;
	
	public TransformationManager() {
		esper = SushiStreamProcessingAdapter.getInstance();
		initializeWithTransformationRulesFromDB();
	}
	
	/**
	 * Registers transformation rules that are stored in the database at the stream processing adapter.
	 */
	public void initializeWithTransformationRulesFromDB() {
		for (TransformationRule transformationRule: TransformationRule.findAll()) {
			register(transformationRule);
			System.out.println("Registered transformation rule '" + transformationRule.getTitle() + "' for event type '" + transformationRule.getEventType().getTypeName() + "' from database.");
		}
	}
	
	public static TransformationManager getInstance() {
		if (instance == null) {
                instance = new TransformationManager();
            }		
		return instance;
	}
	
	public static boolean instanceIsCleared() {
		return (instance == null);
	}
	
	public static void clearInstance() {
		instance = null;
	}
	
	/**
	 * Registers transformation rule at the stream processing adapter.
	 * @param transformationRule the transformation rule
	 * @return transformation listener that receives values composed by Esper and outputs them as newly created events
	 * @throws EPException
	 */
	public TransformationListener register(TransformationRule transformationRule) throws EPException {
		/* NOTE: one statement for each transformation rule
		 * 
		 * other option would be: 
		 * 		if the transformation rule is equal an existing one, 
		 * 		a second listener (probably transforming to another event type but with the same attributes)
		 * 		would be added to the existing statement
		 */
		EPStatement newStatement = esper.createStatement(transformationRule.getQuery(), generateStatementName(transformationRule));
		TransformationListener listener = esper.createTransformationListener(transformationRule.getEventType());
		newStatement.addListener(listener);
		return listener;
	}
	
	/**
	 * Removes transformation rule from the stream processing adapter.
	 * 
	 * @param transformationRule the transformation rule
	 * @return true if transformation rule existed and has now been removed
	 * @throws EPException
	 */
	public boolean removeFromEsper(TransformationRule transformationRule) throws EPException {
		EPStatement statement = esper.getStatement(generateStatementName(transformationRule));
		if (statement != null) {
			// statement has only one listener - see comment in addToEsper(...)
			statement.removeAllListeners();
			statement.destroy();
//			statement.removeListener(transformationRule.getTransformationListener());
//			if (!statement.getUpdateListeners().hasNext()) {
//				statement.destroy();
//			}
			return true;
		} else {
			return false;
		}
	}

	private String generateStatementName(TransformationRule transformationRule) {
		return ("transformation_" + transformationRule.getEventType().getTypeName() + "_" + transformationRule.getTitle()).toLowerCase();
	}

	public SushiStreamProcessingAdapter getEsper() {
		return esper;
	}

	public void setEsper(SushiStreamProcessingAdapter esper) {
		this.esper = esper;
	}

	public EPAdministrator getEsperAdministrator() {
		return esper.getEsperAdministrator();
	}
	
	/**
	 * Checks components of a transformation rule for validity. 
	 * 
	 * @param selectedEventType event type of the resulting events
	 * @param transformationRuleName name of transformation rule
	 * @param attributeIdentifiersAndExpressions map of attributes and desired values of the resulting events
	 * @param attributeIdentifiersWithExternalKnowledge map of attributes using external knowledge and their sets of external knowledge expressions
	 * @param patternTree pattern determining when a new event is created
	 * @throws RuntimeException exception message may be used to output errors on the user interface
	 */
	public void checkForValidity(SushiEventType selectedEventType, String transformationRuleName, Map<String, String> attributeIdentifiersAndExpressions, Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersWithExternalKnowledge, SushiPatternTree patternTree) throws RuntimeException {
		if (patternTree.isEmpty()) {
			throw new RuntimeException("Pattern builder: Please provide a pattern!");
		}
		for (PatternOperatorElement element : patternTree.getPatternOperatorElements()) {
			if (element.getValue() == PatternOperatorEnum.UNTIL) {
				RangeElement rangeElement = element.getRangeElement();
				if ((rangeElement.getLeftEndpoint() < 0 && rangeElement.getRightEndpoint() < 0) || !(rangeElement.getLeftEndpoint() < rangeElement.getRightEndpoint())) {
					throw new RuntimeException("Pattern builder: Please check the range specification(s) of the UNTIL pattern operator(s)!");
				}
			} else if (element.getValue() == PatternOperatorEnum.EVERY_DISTINCT) {
				if (element.getDistinctAttributes().isEmpty()) {
					throw new RuntimeException("Pattern builder: Please provide distinct attributes!");
				}
			} else if (element.getValue() == PatternOperatorEnum.REPEAT) {
				RangeElement rangeElement = element.getRangeElement();
				if (!(rangeElement.getLeftEndpoint() > 0)) {
					throw new RuntimeException("Pattern builder: Please check the provided number of event occurences for the REPEAT pattern operator(s)!");
				}
			} 
		}
		for (EventTypeElement element : patternTree.getEventTypeElements()) {
			if (!element.hasAlias()) {
				throw new RuntimeException("Pattern builder: An alias is required for each event type!");
			}
		}
		for (FilterExpressionElement element : patternTree.getFilterExpressionElements()) {
			if ((element.getValue() == FilterExpressionOperatorEnum.IN || element.getValue() == FilterExpressionOperatorEnum.NOT_IN)) {
				if (element.isRightHandSideRangeBased()) {
					RangeElement rangeElement = element.getRightHandSideRangeOfValues();
					if (!(rangeElement.getLeftEndpoint() < rangeElement.getRightEndpoint())) {
						throw new RuntimeException("Pattern builder: Please check the range specifications in your filter expressions!");
					}
				} else {
					if (element.getRightHandSideListOfValues().isEmpty()) {
						throw new RuntimeException("Pattern builder: The list of values in your filter expressions is empty!");
					}
				}
			} else {
				if (element.getLeftHandSideExpression() == null || element.getLeftHandSideExpression().isEmpty()
						|| element.getRightHandSideExpression() == null || element.getRightHandSideExpression().isEmpty()) {
					throw new RuntimeException("Pattern builder: Please check your filter expressions for completion!");
				}
			}
		}
		if (selectedEventType == null) {
			throw new RuntimeException("Attribute selection: Please select the event type for the transformed events!");
		}
		Iterator <String> iterator = attributeIdentifiersAndExpressions.keySet().iterator();
		while (iterator.hasNext()) {
			String attributeIdentifier = iterator.next();
			if (attributeIdentifiersWithExternalKnowledge.get(attributeIdentifier) != null) {
				for (ExternalKnowledgeExpression expression : attributeIdentifiersWithExternalKnowledge.get(attributeIdentifier).getExternalKnowledgeExpressions()) {
					if (expression.getCriteriaAttributesAndValues().isEmpty() || expression.getDesiredAttribute() == null || expression.getEventType() == null) {
						throw new RuntimeException("Attribute selection: Incomplete information provided for retrieval of external knowledge - please check!");
					}
				}
			} else if (attributeIdentifiersAndExpressions.get(attributeIdentifier) == null || attributeIdentifiersAndExpressions.get(attributeIdentifier).isEmpty()) {
				throw new RuntimeException("Attribute selection: Please provide values for all attributes!");
			}
		}
		if (transformationRuleName == null || transformationRuleName.isEmpty()) {
			throw new RuntimeException("Please provide a name for your transformation rule!");
		}
	}
	
	/** 
	 * Collects all necessary parameters for a transformation rule and creates it.
	 * 
	 * @param selectedEventType event type of the resulting events
	 * @param transformationRuleName name of transformation rule
	 * @param attributeIdentifiersAndExpressions map of attributes and desired values of the resulting events
	 * @param attributeIdentifiersWithExternalKnowledge map of attributes using external knowledge and their sets of external knowledge expressions
	 * @param patternTree pattern determining when a new event is created
	 * @return newly created transformation rule
	 */
	public TransformationRule createTransformationRule(SushiEventType selectedEventType, String transformationRuleName,	SushiPatternTree patternTree, Map<String, String> attributeIdentifiersAndExpressions, Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersWithExternalKnowledge) {
		return new TransformationRule(selectedEventType, transformationRuleName, patternTree, attributeIdentifiersAndExpressions, attributeIdentifiersWithExternalKnowledge);
	}
}
