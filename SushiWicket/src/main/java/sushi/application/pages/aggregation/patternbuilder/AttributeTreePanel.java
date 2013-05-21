package sushi.application.pages.aggregation.patternbuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.table.TreeColumn;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import sushi.aggregation.element.externalknowledge.ExternalKnowledgeExpressionSet;
import sushi.application.components.form.WarnOnExitForm;
import sushi.application.components.tree.SushiAttributeTreeExpansion;
import sushi.application.components.tree.SushiAttributeTreeExpansionModel;
import sushi.application.components.tree.SushiAttributeTreeProvider;
import sushi.application.components.tree.SushiLabelTreeTable;
import sushi.application.pages.aggregation.AdvancedRuleEditorPanel;
import sushi.application.pages.aggregation.patternbuilder.model.AttributeSelectionPanel;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTypeEnum;

public class AttributeTreePanel extends Panel {
	
	private static final long serialVersionUID = -3517674159437927655L;
	private Form<Void> layoutForm;
	private DropDownChoice<SushiEventType> eventTypeDropDownChoice;
	private SushiAttributeTreeProvider attributeTreeTableProvider;
	private SushiLabelTreeTable<SushiAttribute, String> attributeTreeTable;
	private AdvancedRuleEditorPanel advancedRuleEditorPanel;
	private Map<String, String> attributeIdentifiersAndExpressions;
	private Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersWithExternalKnowledge;

	public AttributeTreePanel(String id, final AdvancedRuleEditorPanel advancedRuleEditorPanel) {
		super(id);
		
		this.advancedRuleEditorPanel = advancedRuleEditorPanel;
		attributeIdentifiersAndExpressions = new HashMap<String, String>();
		attributeIdentifiersWithExternalKnowledge = new HashMap<String, ExternalKnowledgeExpressionSet>();
		
		layoutForm = new WarnOnExitForm("layoutForm");
		add(layoutForm);
		
		buildEventTypeDropDownChoice();
		buildAttributeTreeTable();
	}
	
	private void buildEventTypeDropDownChoice() {
		
		List<SushiEventType> eventTypes = SushiEventType.findAll();
		eventTypeDropDownChoice = new DropDownChoice<SushiEventType>("eventTypeDropDownChoice", new Model<SushiEventType>(), eventTypes);
		eventTypeDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				
				if (eventTypeDropDownChoice.getModelObject() != null) {
					List<SushiAttribute> rootAttributes = new ArrayList<SushiAttribute>();
					SushiAttribute timestampAttribute = new SushiAttribute(eventTypeDropDownChoice.getModelObject().getTimestampName(), SushiAttributeTypeEnum.DATE);
					timestampAttribute.setTimestamp(true);
					rootAttributes.add(timestampAttribute);
					rootAttributes.addAll(eventTypeDropDownChoice.getModelObject().getRootLevelValueTypes());
					attributeTreeTableProvider = new SushiAttributeTreeProvider(rootAttributes);
					attributeIdentifiersAndExpressions.keySet().clear();
					attributeIdentifiersAndExpressions.put("Timestamp", "");
					for (String attributeIdentifier : eventTypeDropDownChoice.getModelObject().getAttributeExpressionsWithoutTimestampName()) {
						attributeIdentifiersAndExpressions.put(attributeIdentifier, "");
					}
					renderOrUpdateAttributeTreeTable();
					target.add(attributeTreeTable);
				} else {
					attributeTreeTableProvider = new SushiAttributeTreeProvider();
					renderOrUpdateAttributeTreeTable();
					target.add(attributeTreeTable);
				}
			}
		});
		eventTypeDropDownChoice.setOutputMarkupId(true);
		layoutForm.add(eventTypeDropDownChoice);
	}
	
	private void buildAttributeTreeTable() {
		attributeTreeTableProvider = new SushiAttributeTreeProvider();
		renderOrUpdateAttributeTreeTable();
	}

	public DropDownChoice<SushiEventType> getEventTypeDropDownChoice() {
		return eventTypeDropDownChoice;
	}

	public SushiEventType getSelectedEventType() {
		return eventTypeDropDownChoice.getModelObject();
	}

	public void setSelectedEventType(SushiEventType selectedEventType) {
		eventTypeDropDownChoice.setChoices(SushiEventType.findAll());
		eventTypeDropDownChoice.setModelObject(selectedEventType);
	}

	public Map<String, String> getAttributeIdentifiersAndExpressions() {
		return attributeIdentifiersAndExpressions;
	}
	
	public SushiLabelTreeTable<SushiAttribute, String> getAttributeTreeTable() {
		return attributeTreeTable;
	}
	
	public SushiAttributeTreeProvider getAttributeTreeTableProvider() {
		return attributeTreeTableProvider;
	}

	public void setAttributeTreeTableProvider(SushiAttributeTreeProvider attributeTreeTableProvider) {
		this.attributeTreeTableProvider = attributeTreeTableProvider;
	}

	public Map<String, ExternalKnowledgeExpressionSet> getAttributeIdentifiersWithExternalKnowledge() {
		return attributeIdentifiersWithExternalKnowledge;
	}

	public void setAttributeIdentifiersWithExternalKnowledge(Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersWithExternalKnowledge) {
		this.attributeIdentifiersWithExternalKnowledge = attributeIdentifiersWithExternalKnowledge;
	}

	private void renderOrUpdateAttributeTreeTable() {
		
		List<IColumn<SushiAttribute, String>> columns = createColumns();
		
		attributeTreeTable = new SushiLabelTreeTable<SushiAttribute, String>(
					"attributeTreeTable", 
					columns, 
					attributeTreeTableProvider, 
					Integer.MAX_VALUE, 
					new SushiAttributeTreeExpansionModel());
		
		attributeTreeTable.setOutputMarkupId(true);
		SushiAttributeTreeExpansion.get().expandAll();
		attributeTreeTable.getTable().addTopToolbar(new HeadersToolbar<String>(attributeTreeTable.getTable(), attributeTreeTableProvider));
		
		layoutForm.addOrReplace(attributeTreeTable);
	}
	
	public void updateAttributeTreeTable(AjaxRequestTarget target, Map<String, String> attributeIdentifiersAndExpressions, Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersWithExternalKnowledge) {
		List<SushiAttribute> rootAttributes = new ArrayList<SushiAttribute>();
		SushiAttribute timestampAttribute = new SushiAttribute(eventTypeDropDownChoice.getModelObject().getTimestampName(), SushiAttributeTypeEnum.DATE);
		timestampAttribute.setTimestamp(true);
		rootAttributes.add(timestampAttribute);
		rootAttributes.addAll(eventTypeDropDownChoice.getModelObject().getRootLevelValueTypes());
		attributeTreeTableProvider = new SushiAttributeTreeProvider(rootAttributes);
		this.attributeIdentifiersAndExpressions = attributeIdentifiersAndExpressions;
		this.attributeIdentifiersWithExternalKnowledge = attributeIdentifiersWithExternalKnowledge;
		renderOrUpdateAttributeTreeTable();
		target.add(attributeTreeTable);
	}
	
	private List<IColumn<SushiAttribute, String>> createColumns() {
		List<IColumn<SushiAttribute, String>> columns = new ArrayList<IColumn<SushiAttribute, String>>();
    
		columns.add(new PropertyColumn<SushiAttribute, String>(Model.of("ID"), "ID"));
		columns.add(new TreeColumn<SushiAttribute, String>(Model.of("Attributes")));
		
		columns.add(new AbstractColumn<SushiAttribute, String>(new Model("Select")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				SushiAttribute attribute = ((SushiAttribute) rowModel.getObject());
				cellItem.add(new AttributeSelectionPanel(componentId, attribute, attributeIdentifiersAndExpressions, attributeIdentifiersWithExternalKnowledge, advancedRuleEditorPanel.getPatternBuilderPanel()));
				
			}
		});

		return columns;
	}
	
	public void clear(AjaxRequestTarget target) {
		eventTypeDropDownChoice.setModelObject(null);
		target.add(eventTypeDropDownChoice);
		
		attributeTreeTableProvider = new SushiAttributeTreeProvider(new ArrayList<SushiAttribute>());
		attributeIdentifiersAndExpressions.keySet().clear();
		renderOrUpdateAttributeTreeTable();
		target.add(attributeTreeTable);
	}
}
