package sushi.transformation;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiTreeElement;
import sushi.transformation.collection.SushiPatternTree;
import sushi.transformation.element.EventTypeElement;
import sushi.transformation.element.FilterExpressionConnectorElement;
import sushi.transformation.element.FilterExpressionConnectorEnum;
import sushi.transformation.element.FilterExpressionElement;
import sushi.transformation.element.FilterExpressionOperatorEnum;
import sushi.transformation.element.PatternOperatorElement;
import sushi.transformation.element.PatternOperatorEnum;
import sushi.transformation.element.RangeElement;
import sushi.transformation.element.externalknowledge.ExternalKnowledgeExpression;
import sushi.transformation.element.externalknowledge.ExternalKnowledgeExpressionSet;

/**
 * Provides methods to parse queries in Esper EPL language from a transformation rule.
 */
public class EsperTransformationRuleParser extends TransformationRuleParser {
	
	private static EsperTransformationRuleParser instance = null;
	  
	public static EsperTransformationRuleParser getInstance() {
		if (instance == null) {
			instance = new EsperTransformationRuleParser();
		}
		return instance;
	}
	
	/**
	 * Parses an Esper EPL query from the given parameters.
	 * 
	 * @param attributeIdentifiersAndExpressions pairs of attribute identifiers and expressions - determines what values are stored in the transformed events
	 * @param patternTree pattern that is used to listen for events, built up from the provided elements
	 * @return Esper EPL query
	 */
	public String parseRule(SushiPatternTree patternTree, Map<String, String> attributeIdentifiersAndExpressions, Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersAndExpressionSets) {
		assert(patternTree.getRoots().size() == 1);
		
		StringBuffer query = new StringBuffer();
		
		// SELECT part
		query.append("SELECT");
		
		String valueSelection = buildValueSelectionString(attributeIdentifiersAndExpressions, attributeIdentifiersAndExpressionSets);
		query.append(valueSelection);
		
		// FROM PATTERN part
		
		SushiTreeElement<Serializable> rootElement = patternTree.getRoots().get(0);
		String pattern = buildPatternString(rootElement);
		
		query.append(" FROM Pattern [" + pattern + "]");
		
		return query.toString();
	}

	protected String buildPatternString(SushiTreeElement<Serializable> element) {
		if (element instanceof PatternOperatorElement) {
			PatternOperatorElement poElement = ((PatternOperatorElement) element);
			PatternOperatorEnum poType = (PatternOperatorEnum) poElement.getValue();
			if (poElement.getChildren().size() == 2) {
				String leftHandSideExpression = buildPatternString(poElement.getChildren().get(0));
				String rightHandSideExpression = buildPatternString(poElement.getChildren().get(1));
				if (poType == PatternOperatorEnum.UNTIL) {
					RangeElement rangeElement = poElement.getRangeElement();
					StringBuffer sb = new StringBuffer();
					sb.append("(");
					if (rangeElement.getLeftEndpoint() >= 0 || rangeElement.getRightEndpoint() >= 0) {
						sb.append("[");
						sb.append(rangeElement.getLeftEndpoint() < 0 ? "" : rangeElement.getLeftEndpoint());
						sb.append(":");
						sb.append(rangeElement.getRightEndpoint() < 0 ? "" : rangeElement.getRightEndpoint());
						sb.append("] ");
					}
					sb.append(leftHandSideExpression + " UNTIL " + rightHandSideExpression);
					sb.append(")");
					return sb.toString();
				} else if (poType == PatternOperatorEnum.AND) {
					return "(" + leftHandSideExpression + " AND " + rightHandSideExpression + ")";
				} else if (poType == PatternOperatorEnum.OR) {
					return "(" + leftHandSideExpression + " OR " + rightHandSideExpression + ")";
				} else if (poType == PatternOperatorEnum.FOLLOWED_BY) {
					return "(" + leftHandSideExpression + " -> " + rightHandSideExpression + ")";
				}
			} else if (poElement.getChildren().size() == 1) {
				String expression = buildPatternString(poElement.getChildren().get(0));
				if (poType == PatternOperatorEnum.EVERY) {
					return "(EVERY " + expression + ")";
				} else if (poType == PatternOperatorEnum.EVERY_DISTINCT) {
					StringBuffer sb = new StringBuffer();
					sb.append("(EVERY-DISTINCT(");
					Iterator<String> iteratorForDistinctAttributes = poElement.getDistinctAttributes().iterator();
					while (iteratorForDistinctAttributes.hasNext()) {
						String distinctAttribute = iteratorForDistinctAttributes.next();
						sb.append(distinctAttribute);
						if (iteratorForDistinctAttributes.hasNext()) {
							sb.append(", ");
						}
					}
					sb.append(") " + expression + ")");
					return sb.toString();
				} else if (poType == PatternOperatorEnum.REPEAT) {
					RangeElement rangeElement = poElement.getRangeElement();
					return "([" + rangeElement.getLeftEndpoint() + "] " + expression + ")";
				} else if (poType == PatternOperatorEnum.NOT) {
					return "(NOT " + expression + ")";
				}
			}
		} else if (element instanceof EventTypeElement) {
			EventTypeElement etElement = ((EventTypeElement) element);
			StringBuffer sb = new StringBuffer();
			if (etElement.hasAlias()) {
				sb.append("(" + etElement.getAlias() + "=" + ((SushiEventType) etElement.getValue()).getTypeName());
			} else {
				sb.append("(" + ((SushiEventType) etElement.getValue()).getTypeName());
			}
			if (etElement.hasChildren()) {
				sb.append("(");
				Iterator<SushiTreeElement<Serializable>> iterator = element.getChildren().iterator();
				while (iterator.hasNext()) {
					SushiTreeElement<Serializable> currentElement = iterator.next();
					sb.append(buildPatternString(currentElement));
					if (iterator.hasNext()) {
						sb.append(", ");
					}
				}
				sb.append(")");
			}
			sb.append(")");
			return sb.toString();
		} else if (element instanceof FilterExpressionConnectorElement) {
			FilterExpressionConnectorElement fecElement = (FilterExpressionConnectorElement) element;
			FilterExpressionConnectorEnum fecType = (FilterExpressionConnectorEnum) fecElement.getValue();
			if (fecType == FilterExpressionConnectorEnum.AND) {
				String leftHandSideExpression = buildPatternString(fecElement.getChildren().get(0));
				String rightHandSideExpression = buildPatternString(fecElement.getChildren().get(1));
				return "(" + leftHandSideExpression + " AND " + rightHandSideExpression + ")";
			} else if (fecType == FilterExpressionConnectorEnum.OR) {
				String leftHandSideExpression = buildPatternString(fecElement.getChildren().get(0));
				String rightHandSideExpression = buildPatternString(fecElement.getChildren().get(1));
				return "(" + leftHandSideExpression + " OR " + rightHandSideExpression + ")";
			} else if (fecType == FilterExpressionConnectorEnum.NOT) {
				String expression = buildPatternString(fecElement.getChildren().get(0));
				return "NOT (" + expression + ")";
			}
		} else if (element instanceof FilterExpressionElement) {
			FilterExpressionElement feElement = (FilterExpressionElement) element;
			FilterExpressionOperatorEnum feType = (FilterExpressionOperatorEnum) feElement.getValue();
			StringBuffer sb = new StringBuffer();
			sb.append("(");
			sb.append("(" + feElement.getLeftHandSideExpression() + ") " + feType.getValue() + " ");
			if (feType == FilterExpressionOperatorEnum.IN || feType == FilterExpressionOperatorEnum.NOT_IN) {
				if (feElement.isRightHandSideRangeBased()) {
					RangeElement rangeElement = feElement.getRightHandSideRangeOfValues();
					sb.append(rangeElement.isLeftEndpointOpen() ? "(" : "[");
					sb.append(rangeElement.getLeftEndpoint() + ":" + rangeElement.getRightEndpoint());
					sb.append(rangeElement.isRightEndpointOpen() ? ")" : "]");
				} else {
					sb.append("(");
					Iterator<String> iterator = feElement.getRightHandSideListOfValues().iterator();
					while (iterator.hasNext()) {
						sb.append(iterator.next());
						if (iterator.hasNext()) {
							sb.append(", ");
						}
					}
					sb.append(")");
				}
			} else {
				sb.append("(" + feElement.getRightHandSideExpression() + ")");
			}
			sb.append(")");
			return sb.toString();
		}
		return "";
	}

	protected String buildValueSelectionString(Map<String, String> attributeIdentifiersAndExpressions, Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersAndExpressionSets) {
		StringBuffer sb = new StringBuffer();
		Iterator<String> iteratorForAttributeIdentifiers = attributeIdentifiersAndExpressions.keySet().iterator();
		while (iteratorForAttributeIdentifiers.hasNext()) {
			String attributeIdentifier = iteratorForAttributeIdentifiers.next();
			if (attributeIdentifiersAndExpressions.get(attributeIdentifier) != null && !attributeIdentifiersAndExpressions.get(attributeIdentifier).isEmpty()) {
				sb.append(" (" + attributeIdentifiersAndExpressions.get(attributeIdentifier) + ") AS " + attributeIdentifier);
				if (iteratorForAttributeIdentifiers.hasNext()) {
					sb.append(",");
				}
			} else if (attributeIdentifiersAndExpressionSets.get(attributeIdentifier) != null) {
				sb.append(" (" + buildValueSelectionStringFromExternalKnowledge(attributeIdentifiersAndExpressionSets.get(attributeIdentifier)) + ") AS " + attributeIdentifier);
				if (iteratorForAttributeIdentifiers.hasNext()) {
					sb.append(",");
				}
			}
		}
		return sb.toString();
	}
	
	protected String buildValueSelectionStringFromExternalKnowledge(ExternalKnowledgeExpressionSet externalKnowledge) {
		String externalKnowledgeFetchMethodName = null;
		if (externalKnowledge.getResultingType() == SushiAttributeTypeEnum.STRING) {
			externalKnowledgeFetchMethodName  = "stringValueFromEvent";
		} else if (externalKnowledge.getResultingType() == SushiAttributeTypeEnum.INTEGER) {
			externalKnowledgeFetchMethodName  = "integerValueFromEvent";
		} else if (externalKnowledge.getResultingType() == SushiAttributeTypeEnum.DATE) {
			externalKnowledgeFetchMethodName  = "dateValueFromEvent";
		}
		StringBuffer sb = new StringBuffer();
		sb.append("coalesce(");
		for (ExternalKnowledgeExpression expression : externalKnowledge.getExternalKnowledgeExpressions()) {
			sb.append(externalKnowledgeFetchMethodName + "(");
			sb.append("'" + expression.getEventType().getTypeName() + "', ");
			sb.append("'" + expression.getDesiredAttribute().getAttributeExpression() + "', {");
			Iterator<String> iterator = expression.getCriteriaAttributesAndValues().keySet().iterator();
			while (iterator.hasNext()) {
				String criteriaAttributeExpression = iterator.next();
				sb.append("'" + criteriaAttributeExpression + "', " + expression.getCriteriaAttributesAndValues().get(criteriaAttributeExpression));
				if (iterator.hasNext()) {
					sb.append(", ");
				}
			}
			sb.append("}), ");
		}
		sb.append(externalKnowledge.getDefaultValue());
		sb.append(")");
		return sb.toString();
	}
}
