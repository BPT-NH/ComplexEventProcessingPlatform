package sushi.application.pages.input.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import sushi.application.components.table.SushiProvider;
import sushi.event.attribute.SushiAttribute;

/**
 * This class is the provider for event attributes.
 */
public class EventAttributeProvider extends SushiProvider<SushiAttribute> {
	
	private static final long serialVersionUID = 1L;
	private String timestampName;
	
	/**
	 * Constructor for providing event attributes.
	 */
	public EventAttributeProvider(List<SushiAttribute> attributes) {
		super(attributes);
		this.timestampName = new String();
	}
	
	public EventAttributeProvider(List<SushiAttribute> attributes, String timestampName) {
		this(attributes);
		this.timestampName = timestampName;
	}
	
	public EventAttributeProvider(List<SushiAttribute> attributes, List<SushiAttribute> selectedAttributes) {
		super(attributes, selectedAttributes);
	}
	
	@Override
	public void detach() {
//		attributes = null;
	}
	
	@Override
	public IModel<SushiAttribute> model(SushiAttribute attribute) {
		return Model.of(attribute);
	}

		
	public List<String> getSelectedColumnNames() {
		ArrayList<String> selectedColumnNames = new ArrayList<String>();
		selectedColumnNames.add(timestampName);
		for (SushiAttribute attribute : entities) {
			if (selectedEntities.contains(attribute) && !attribute.getName().equals(timestampName)) {
				selectedColumnNames.add(attribute.getName());
			}
		}
		return selectedColumnNames;
	}
	
	public List<String> getSelectedAttributeExpressions() {
		ArrayList<String> selectedAttributeExpressions = new ArrayList<String>();
		for (SushiAttribute attribute : selectedEntities) {
			selectedAttributeExpressions.add(attribute.getAttributeExpression());
		}
		return selectedAttributeExpressions;
	}

	public void setTimestampName(String timestampName) {
		this.timestampName = timestampName;
	}
	
}
