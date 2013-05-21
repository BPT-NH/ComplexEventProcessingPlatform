package sushi.application.pages.aggregation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.tree.AbstractTree;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import sushi.aggregation.SushiAggregation;
import sushi.aggregation.SushiAggregationRule;
import sushi.aggregation.collection.SushiPatternTree;
import sushi.aggregation.element.externalknowledge.ExternalKnowledgeExpressionSet;
import sushi.application.components.form.WarnOnExitForm;
import sushi.application.components.tree.SushiSelectTree;
import sushi.application.components.tree.TreeExpansion;
import sushi.application.components.tree.TreeExpansionModel;
import sushi.application.components.tree.TreeProvider;
import sushi.application.pages.aggregation.patternbuilder.AttributeTreePanel;
import sushi.application.pages.aggregation.patternbuilder.PatternBuilderPanel;
import sushi.event.SushiEventType;
import sushi.event.collection.SushiTree;
import sushi.event.collection.SushiTreeElement;

import com.espertech.esper.client.EPException;

import de.agilecoders.wicket.markup.html.bootstrap.tabs.Collapsible;

public class AdvancedRuleEditorPanel extends Panel {
	
	private static final long serialVersionUID = -3517674159437927655L;
	private TextField<String> aggregationRuleNameTextField;
	private String aggregationRuleNameFromTree;
	private Form<Void> layoutForm;
//	private TextArea<String> aggregationRuleTextArea;
	private SushiSelectTree<SushiTreeElement<String>> aggregationRuleTree;
	private SushiTree<String> aggregationRuleTreeStructure;
	private AggregationPage aggregationPage;
	private AdvancedRuleEditorPanel advancedRuleEditorPanel;
	protected AttributeTreePanel attributeTreePanel;
	protected PatternBuilderPanel patternBuilderPanel;

	public AdvancedRuleEditorPanel(String id, final AggregationPage aggregationPage) {
		super(id);
		
		this.aggregationPage = aggregationPage;
		this.advancedRuleEditorPanel = this;
		
		layoutForm = new WarnOnExitForm("layoutForm");
		add(layoutForm);
		
		addTabs();
		buildTextFields();
		buildAggregationRuleTree();
		buildButtons();
	}
	
	private void buildTextFields() {
		aggregationRuleNameTextField = new TextField<String>("aggregationRuleNameTextField", new PropertyModel<String>(this, "aggregationRuleNameFromTree"));
		aggregationRuleNameTextField.setOutputMarkupId(true);
		layoutForm.add(aggregationRuleNameTextField);
		
//		List<String> eventTypes = SushiEventType.getAllTypeNames();
//		eventTypeDropDownChoice = new DropDownChoice<String>("eventTypeDropDownChoice", new PropertyModel<String>(this, "selectedEventTypeName"), eventTypes);
//		eventTypeDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onUpdate(AjaxRequestTarget target) {
//				updateOnChangeOfDropDownChoice(target);
//			}
//		});
//		layoutForm.add(eventTypeDropDownChoice);
		
//		aggregationRuleTextArea = new TextArea<String>("aggregationRuleTextArea", new PropertyModel<String>(this, "aggregationRule"));
//		aggregationRuleTextArea.setOutputMarkupId(true);	
//		layoutForm.add(aggregationRuleTextArea);
	}

	private void buildButtons() {
		
		AjaxButton editButton = new AjaxButton("editButton") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				
				if (aggregationRuleTree.getSelectedElement() != null) {
					String eventTypeNameFromTree = aggregationRuleTree.getSelectedElement().getParent().getValue().toString();
					aggregationRuleNameFromTree = aggregationRuleTree.getSelectedElement().getValue().toString();
					SushiAggregationRule aggregationRule = SushiAggregationRule.findByEventTypeAndTitle(eventTypeNameFromTree, aggregationRuleNameFromTree);
					patternBuilderPanel.setPatternTree(new SushiPatternTree(aggregationRule.getPatternTree().getElements()));
					patternBuilderPanel.updatePatternTreeTable(target);
					target.add(aggregationRuleNameTextField);
					SushiEventType selectedEventType = aggregationRule.getEventType();
					attributeTreePanel.setSelectedEventType(selectedEventType);
					target.add(attributeTreePanel.getEventTypeDropDownChoice());
					attributeTreePanel.updateAttributeTreeTable(target, aggregationRule.getAttributeIdentifiersAndExpressions(), aggregationRule.getAttributeIdentifiersWithExternalKnowledge());
				}
	        }
	    };
	    layoutForm.add(editButton);
//	    
	    AjaxButton deleteButton = new AjaxButton("deleteButton") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				if (aggregationRuleTree.getSelectedElement() != null) {
					String eventTypeNameFromTree = aggregationRuleTree.getSelectedElement().getParent().getValue().toString();
					String aggregationRuleNameFromTree = aggregationRuleTree.getSelectedElement().getValue().toString();
					removeAggregationRule(eventTypeNameFromTree, aggregationRuleNameFromTree);
					renderOrUpdateAggregationRuleTree();
					target.add(aggregationRuleTree);
					clearFields(target);
				}
			}
		};
		layoutForm.add(deleteButton);
		
		AjaxButton saveButton = new AjaxButton("saveButton", layoutForm) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				try {
					if (aggregationRuleNameFromTree == null || aggregationRuleNameFromTree.isEmpty()) {
						aggregationPage.getFeedbackPanel().error("Please enter an aggregation rule name!");
						target.add(aggregationPage.getFeedbackPanel());
					} else if (attributeTreePanel.getSelectedEventType() == null) {
						aggregationPage.getFeedbackPanel().error("Please select an event type!");
						target.add(aggregationPage.getFeedbackPanel());
					} else if (patternBuilderPanel.getPatternTree().isEmpty()) {
						aggregationPage.getFeedbackPanel().error("Please enter an aggregation rule!");
						target.add(aggregationPage.getFeedbackPanel());
					} else {
						saveAggregationRule(target, attributeTreePanel.getSelectedEventType(), aggregationRuleNameFromTree, attributeTreePanel.getAttributeIdentifiersAndExpressions(), attributeTreePanel.getAttributeIdentifiersWithExternalKnowledge(), patternBuilderPanel.getPatternTree());
						clearFields(target);
					}
				} catch (EPException e) { 
					aggregationPage.getFeedbackPanel().error(e.getMessage());
					target.add(aggregationPage.getFeedbackPanel());
				}
	        }
	    };
	    layoutForm.add(saveButton);
	}

	private void clearFields(AjaxRequestTarget target) {
		aggregationRuleNameFromTree = "";
		target.add(aggregationRuleNameTextField);
		
		patternBuilderPanel.clear(target);
		attributeTreePanel.clear(target);
	}
	
	private void saveAggregationRule(AjaxRequestTarget target, SushiEventType selectedEventType, String aggregationRuleName, Map<String, String> attributeIdentifiersAndExpressions, Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersWithExternalKnowledge, SushiPatternTree patternTree) {
		if (SushiAggregationRule.findByEventTypeAndTitle(selectedEventType.getTypeName(), aggregationRuleName) != null) {
			removeAggregationRule(selectedEventType.getTypeName(), aggregationRuleName);
		}
		addAggregationRule(selectedEventType, aggregationRuleName, attributeIdentifiersAndExpressions, attributeIdentifiersWithExternalKnowledge, patternTree);
		renderOrUpdateAggregationRuleTree();
		target.add(aggregationRuleTree);
		aggregationPage.getFeedbackPanel().success("Saved aggregation rule '" + aggregationRuleName + "' for event type '" + selectedEventType.getTypeName() + "'.");
		target.add(aggregationPage.getFeedbackPanel());
	}
	
	protected void addAggregationRule(SushiEventType selectedEventType, String aggregationRuleName, Map<String, String> attributeIdentifiersAndExpressions, Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersWithExternalKnowledge, SushiPatternTree patternTree) throws EPException {
		SushiAggregationRule sushiAggregationRule = new SushiAggregationRule(selectedEventType, aggregationRuleName, SushiAggregationRule.generateAggregationRule(attributeIdentifiersAndExpressions, patternTree), patternTree, attributeIdentifiersAndExpressions, attributeIdentifiersWithExternalKnowledge);
		SushiAggregation.getInstance().addToEsper(sushiAggregationRule);
		sushiAggregationRule.save();
		String eventTypeName = selectedEventType.getTypeName();
		addAggregationRuleToTreeStructure(eventTypeName, aggregationRuleName);
	}

	protected void removeAggregationRule(String eventTypeName, String title) {
		aggregationRuleTreeStructure.removeChild(eventTypeName, title);
		SushiAggregationRule sushiAggregationRule = SushiAggregationRule.findByEventTypeAndTitle(eventTypeName, title);
		SushiAggregation.getInstance().removeFromEsper(sushiAggregationRule);
		sushiAggregationRule.remove();
	}

	private void addAggregationRuleToTreeStructure(String eventTypeName, String aggregationRuleName) {
		if (!aggregationRuleTreeStructure.containsRootElement(eventTypeName)) {
			aggregationRuleTreeStructure.addRootElement(eventTypeName);
		}
		aggregationRuleTreeStructure.addChild(eventTypeName, aggregationRuleName);
	}

	private void buildAggregationRuleTree() {
		aggregationRuleTreeStructure = new SushiTree<String>();
		List<SushiAggregationRule> aggregationRules = SushiAggregationRule.findAll();
		for (SushiAggregationRule aggregationRule : aggregationRules) {
			addAggregationRuleToTreeStructure(aggregationRule.getEventType().getTypeName(), aggregationRule.getTitle());
		}
		renderOrUpdateAggregationRuleTree();
	}
	
	private void renderOrUpdateAggregationRuleTree() {
		aggregationRuleTree = new SushiSelectTree<SushiTreeElement<String>>("aggregationRuleTree", new TreeProvider(generateNodesOfAggregationRuleTree()), new TreeExpansionModel()) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void select(SushiTreeElement<String> element, AbstractTree<SushiTreeElement<String>> tree, final AjaxRequestTarget target) {
				// only the aggregation rules are selectable
				if (element.hasParent()) {
					super.select(element, tree, target);
				}
			}
		};
        TreeExpansion.get().expandAll();
        aggregationRuleTree.setOutputMarkupId(true);
		layoutForm.addOrReplace(aggregationRuleTree);
	}
	
	private ArrayList<SushiTreeElement<String>> generateNodesOfAggregationRuleTree() {
		ArrayList<SushiTreeElement<String>> treeElements = new ArrayList<SushiTreeElement<String>>();
		List<String> eventTypes = aggregationRuleTreeStructure.getRootElements();
		for (String eventType : eventTypes) {
			SushiTreeElement<String> rootElement = new SushiTreeElement<String>(eventType);
			treeElements.add(rootElement);
			if (aggregationRuleTreeStructure.hasChildren(eventType)) {
				fillTreeLevel(rootElement, aggregationRuleTreeStructure.getChildren(eventType), aggregationRuleTreeStructure);
			}
		}
		return treeElements;
	}
	
	private void fillTreeLevel(SushiTreeElement<String> parent, List<String> children, SushiTree<String> aggregationRuleTreeStructure) {
		for (String newValue : children) {
			SushiTreeElement<String> newElement = new SushiTreeElement<String>(parent, newValue.toString());
			if (aggregationRuleTreeStructure.hasChildren(newValue)) {
				fillTreeLevel(newElement, aggregationRuleTreeStructure.getChildren(newValue), aggregationRuleTreeStructure);
			}
		}
	}
	
	private void addTabs() {
		List<ITab> tabs = new ArrayList<ITab>();
		tabs.add(new AbstractTab(new Model<String>("Build pattern")) {
			public Panel getPanel(String panelId) {
				patternBuilderPanel = new PatternBuilderPanel(panelId, advancedRuleEditorPanel);
				return patternBuilderPanel;
			}
		});
		tabs.add(new AbstractTab(new Model<String>("Select attribute values")) {
			public Panel getPanel(String panelId) {
				attributeTreePanel = new AttributeTreePanel(panelId, advancedRuleEditorPanel);
				return attributeTreePanel;
			}
		});
		layoutForm.add(new Collapsible("collapsible", tabs, Model.of(-1)));
	}
	
	public AttributeTreePanel getAttributeTreePanel() {
		return attributeTreePanel;
	}
	
	public PatternBuilderPanel getPatternBuilderPanel() {
		return patternBuilderPanel;
	}

	public AggregationPage getAggregationPage() {
		return aggregationPage;
	}
}
