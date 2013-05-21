package sushi.application.pages.input.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import sushi.application.components.table.model.AbstractDataProvider;
import sushi.event.attribute.SushiAttribute;

public class EventAttributeProvider extends AbstractDataProvider implements ISortableDataProvider<SushiAttribute, String> {
	
	private static final long serialVersionUID = 1L;
	private List<SushiAttribute> attributes;
	private List<SushiAttribute> selectedAttributes;
	private ISortState<String> sortState = new SingleSortState<String>();
	private String timestampName;
	
	public EventAttributeProvider(List<SushiAttribute> attributes) {
		this.attributes = attributes;
		this.selectedAttributes = new ArrayList<SushiAttribute>();
		this.timestampName = new String();
	}
	
	public EventAttributeProvider(List<SushiAttribute> attributes, String timestampName) {
		this(attributes);
		this.timestampName = timestampName;
	}
	
	public EventAttributeProvider(List<SushiAttribute> attributes, List<SushiAttribute> selectedAttributes) {
		this(attributes);
		this.selectedAttributes = selectedAttributes;
	}
	
	@Override
	public void detach() {
//		attributes = null;
	}
	
	@Override
	public Iterator<? extends SushiAttribute> iterator(long first, long count) {
		List<SushiAttribute> data = attributes;
		Collections.sort(data, new Comparator<SushiAttribute>() {
			public int compare(SushiAttribute e1, SushiAttribute e2) {
				return (new Integer(e1.getID()).compareTo(e2.getID()));
			}
		});
		return data.subList((int)first, (int)Math.min(first + count, data.size())).iterator();
	}

	@Override
	public IModel<SushiAttribute> model(SushiAttribute attribute) {
		return Model.of(attribute);
	}

	@Override
	public long size() {
		return attributes.size();
	}

	public List<SushiAttribute> getAttributes() {
		return attributes;
	}
	
	public List<SushiAttribute> getSelectedAttributes(){
		return selectedAttributes;
	}

	public void setAttributes(List<SushiAttribute> attributeList) {
		attributes = attributeList;
	}
	
	public List<String> getSelectedColumnNames() {
		ArrayList<String> selectedColumnNames = new ArrayList<String>();
		selectedColumnNames.add(timestampName);
		for (SushiAttribute attribute : attributes) {
			if (selectedAttributes.contains(attribute) && !attribute.getName().equals(timestampName)) {
				selectedColumnNames.add(attribute.getName());
			}
		}
		return selectedColumnNames;
	}
	
	public List<String> getSelectedAttributeExpressions() {
		ArrayList<String> selectedAttributeExpressions = new ArrayList<String>();
		for (SushiAttribute attribute : selectedAttributes) {
			selectedAttributeExpressions.add(attribute.getAttributeExpression());
		}
		return selectedAttributeExpressions;
	}

	public void setTimestampName(String timestampName) {
		this.timestampName = timestampName;
	}

	@Override
	public ISortState<String> getSortState() {
		return sortState;
	}
	
	@Override
	public void selectEntry(int entryId) {
		for (Iterator<SushiAttribute> iter = attributes.iterator(); iter.hasNext(); ) {
			SushiAttribute attribute = iter.next();
			if(attribute.getID() == entryId) {
				selectEntry(attribute);
				return;
			}
		}
	}
	
	public void selectEntry(SushiAttribute attribute) {
		selectedAttributes.add(attribute);
	}
	
	@Override
	public void deselectEntry(int entryId) {
		for (Iterator<SushiAttribute> iter = attributes.iterator(); iter.hasNext(); ) {
			SushiAttribute attribute = iter.next();
			if (attribute.getID() == entryId) {
				deselectEntry(attribute);
				return;
			}
		}
	}
	
	public void deselectEntry(SushiAttribute attribute) {
		selectedAttributes.remove(attribute);
	}
	
	@Override
	public boolean isEntrySelected(int entryId) {
		for (SushiAttribute attribute : selectedAttributes) {
			if (attribute.getID() == entryId) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isEntrySelected(SushiAttribute attribute) {
		if (selectedAttributes.contains(attribute)) {
			return true;
		}
		return false;
	}

	public void deleteSelectedEntries() {
		for (SushiAttribute attribute : selectedAttributes) {
			attributes.remove(attribute);
			attribute.remove();
		}
	}

	public void selectAllEntries() {
		for (SushiAttribute attribute : attributes) {
			selectedAttributes.add(attribute);
		}
	}
	
	@Override
	public Object getEntry(int entryId) {
		for(SushiAttribute attribute : attributes){
			if(attribute.getID() == entryId){
				return attribute;
			}
		}
		return null;
	}

}
