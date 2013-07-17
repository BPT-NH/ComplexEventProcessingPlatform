package sushi.application.pages.transformation.patternbuilder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.model.IModel;

import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.event.collection.SushiTreeElement;
import sushi.transformation.collection.SushiPatternTree;
import sushi.transformation.element.EventTypeElement;

public class AttributeExpressionTextField extends AutoCompleteTextField<String> {

	private static final long serialVersionUID = -565229104833806474L;
	private SushiPatternTree patternTree;

	public AttributeExpressionTextField(String id, IModel<String> model, SushiPatternTree patternTree) {
		super(id, model);
		this.patternTree = patternTree;
	}

	@Override
	protected Iterator<String> getChoices(String input) {
		return generateExpressionSuggestions(input);
	}
	
	public Iterator<String> generateExpressionSuggestions(String input) {
//		int lastWhitespaceOfInput = input.lastIndexOf(" ");
		String partialInput;
//		if (lastWhitespaceOfInput < 0) {
			partialInput = input;
//		} else {
//			partialInput = input.substring(lastWhitespaceOfInput + 1);
//		}
		
		Set<String> matchedExpressions = new HashSet<String>();
		for (SushiTreeElement<Serializable> element : patternTree.getElements()) {
			if (element instanceof EventTypeElement) {
				EventTypeElement eventTypeElement = (EventTypeElement) element;
				if (eventTypeElement.hasAlias()) {
					SushiEventType eventType = ((SushiEventType) eventTypeElement.getValue());
					Set<String> expressionsToAdd = new HashSet<String>();
					expressionsToAdd.add(eventTypeElement.getAlias() + "." + "Timestamp");
					expressionsToAdd.add(eventTypeElement.getAlias() + "." + "Timestamp.getTime()");
					for (String expressionWithAlias : expressionsToAdd) {
						if (partialInput == null || partialInput.isEmpty() || expressionWithAlias.toUpperCase().startsWith(partialInput.toUpperCase())) {
							matchedExpressions.add(expressionWithAlias);
							if (matchedExpressions.size() >= 10) {
	                            break;
	                        }
						}
					}
					for (SushiAttribute attribute : eventType.getValueTypes()) {
						expressionsToAdd = new HashSet<String>();
						expressionsToAdd.add(eventTypeElement.getAlias() + "." + attribute.getAttributeExpression());
						if (attribute.getType() == SushiAttributeTypeEnum.DATE) {
							expressionsToAdd.add(eventTypeElement.getAlias() + "." + attribute.getAttributeExpression() + ".getTime()");
						}
						for (String expressionWithAlias : expressionsToAdd) {
							if (partialInput == null || partialInput.isEmpty() || expressionWithAlias.toUpperCase().startsWith(partialInput.toUpperCase())) {
								matchedExpressions.add(expressionWithAlias);
								if (matchedExpressions.size() >= 10) {
		                            break;
		                        }
							}
						}
					}
				}
			}
		}
		return matchedExpressions.iterator();
	}

}
