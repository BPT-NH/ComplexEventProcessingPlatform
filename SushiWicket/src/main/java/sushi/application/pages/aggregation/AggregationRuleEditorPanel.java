package sushi.application.pages.aggregation;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.tree.AbstractTree;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import sushi.aggregation.SushiAggregation;
import sushi.aggregation.SushiAggregationRule;
import sushi.application.components.form.WarnOnExitForm;
import sushi.application.components.tree.TreeExpansion;
import sushi.application.components.tree.TreeExpansionModel;
import sushi.application.components.tree.TreeProvider;
import sushi.application.components.tree.SushiSelectTree;
import sushi.application.pages.AbstractSushiPage;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.collection.SushiTree;
import sushi.event.collection.SushiTreeElement;

import com.espertech.esper.client.EPException;

public class AggregationRuleEditorPanel extends Panel {
	
	private static final long serialVersionUID = -3517674159437927655L;
	private String selectedEventTypeName;
	private SushiEventType selectedEventType;
	private String aggregationRule;
	private TextField<String> aggregationRuleNameTextField;
	private String aggregationRuleName;
	private Form<Void> layoutForm;
	private TextArea<String> aggregationRuleTextArea;
	private SushiSelectTree<SushiTreeElement<String>> aggregationRuleTree;
	private SushiTree<String> aggregationRuleTreeStructure;
	private DropDownChoice<String> eventTypeDropDownChoice;
	private AbstractSushiPage abstractSushiPage;

	public AggregationRuleEditorPanel(String id, final AbstractSushiPage abstractSushiPage) {
		super(id);
		
		this.abstractSushiPage = abstractSushiPage;
		
		layoutForm = new WarnOnExitForm("layoutForm");
		add(layoutForm);
		
		buildTextFields();
		buildAggregationRuleTree();
		buildButtons();
	}
	
	private void buildTextFields() {
		aggregationRuleNameTextField = new TextField<String>("aggregationRuleNameTextField", new PropertyModel<String>(this, "aggregationRuleName"));
		aggregationRuleNameTextField.setOutputMarkupId(true);
		layoutForm.add(aggregationRuleNameTextField);
		
		List<String> eventTypes = SushiEventType.getAllTypeNames();
		eventTypeDropDownChoice = new DropDownChoice<String>("eventTypeDropDownChoice", new PropertyModel<String>(this, "selectedEventTypeName"), eventTypes);
		eventTypeDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				updateOnChangeOfDropDownChoice(target);
			}
		});
		layoutForm.add(eventTypeDropDownChoice);
		
		aggregationRuleTextArea = new TextArea<String>("aggregationRuleTextArea", new PropertyModel<String>(this, "aggregationRule"));
		aggregationRuleTextArea.setOutputMarkupId(true);	
		layoutForm.add(aggregationRuleTextArea);
	}
	
	protected void updateOnChangeOfDropDownChoice(AjaxRequestTarget target) {
		selectedEventType = SushiEventType.findByTypeName(selectedEventTypeName);
		
		if (selectedEventType != null) {
			
			// inform user about rule schema
			
			List<SushiAttribute> selectedEventTypeAttributes = selectedEventType.getValueTypes();
			
			StringBuffer aggregationSuggestionBuffer = new StringBuffer();
			aggregationSuggestionBuffer.append("Your aggregation rule may start with: SELECT ");
			if (selectedEventType.getTimestampName() != null) {
				aggregationSuggestionBuffer.append("[...] AS Timestamp, ");
				
			}
			
			for (int i = 0; i < selectedEventTypeAttributes.size(); i++) {
				aggregationSuggestionBuffer.append("[...] AS " + selectedEventTypeAttributes.get(i).getAttributeExpression());
				if (i == selectedEventTypeAttributes.size() - 1) {
					aggregationSuggestionBuffer.append(" ");
				} else {
					aggregationSuggestionBuffer.append(", ");
				}
			}
			abstractSushiPage.getFeedbackPanel().info(aggregationSuggestionBuffer.toString());
			target.add(abstractSushiPage.getFeedbackPanel());
		} else {
			target.add(abstractSushiPage.getFeedbackPanel());
		}
	}

	private void buildButtons() {
		
		AjaxButton editButton = new AjaxButton("editButton") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				
				if (aggregationRuleTree.getSelectedElement() != null) {
					selectedEventTypeName = aggregationRuleTree.getSelectedElement().getParent().getValue().toString();
					aggregationRuleName = aggregationRuleTree.getSelectedElement().getValue().toString();
					aggregationRule = SushiAggregationRule.findByEventTypeAndTitle(selectedEventTypeName, aggregationRuleName).getQuery();
					target.add(aggregationRuleNameTextField);
					target.add(eventTypeDropDownChoice);
					target.add(aggregationRuleTextArea);
					updateOnChangeOfDropDownChoice(target);
				}
	        }
	    };
	    layoutForm.add(editButton);
	    
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
					if (aggregationRuleName == null || aggregationRuleName.isEmpty()) {
						abstractSushiPage.getFeedbackPanel().error("Please enter an aggregation rule name!");
						target.add(abstractSushiPage.getFeedbackPanel());
					} else if (selectedEventType == null) {
						abstractSushiPage.getFeedbackPanel().error("Please select an event type!");
						target.add(abstractSushiPage.getFeedbackPanel());
					} else if (aggregationRule == null || aggregationRule.isEmpty()) {
						abstractSushiPage.getFeedbackPanel().error("Please enter an aggregation rule!");
						target.add(abstractSushiPage.getFeedbackPanel());
					} else {
						saveAggregationRule(target, selectedEventType, aggregationRuleName, aggregationRule);
					}
				} catch (EPException e) { 
					abstractSushiPage.getFeedbackPanel().error(e.getMessage());
					target.add(abstractSushiPage.getFeedbackPanel());
				}
	        }
	    };
	    layoutForm.add(saveButton);
	}

	private void clearFields(AjaxRequestTarget target) {
		aggregationRuleName = "";
		target.add(aggregationRuleNameTextField);
		selectedEventTypeName = null;
		target.add(eventTypeDropDownChoice);
		aggregationRule = "";
		target.add(aggregationRuleTextArea);
	}
	
	private void saveAggregationRule(AjaxRequestTarget target, SushiEventType selectedEventType, String aggregationRuleName, String aggregationRule) {
		if (SushiAggregationRule.findByEventTypeAndTitle(selectedEventTypeName, aggregationRuleName) != null) {
			removeAggregationRule(selectedEventTypeName, aggregationRuleName);
		}
		addAggregationRule(selectedEventType, aggregationRuleName, aggregationRule);
		renderOrUpdateAggregationRuleTree();
		target.add(aggregationRuleTree);
		abstractSushiPage.getFeedbackPanel().success("Saved aggregation rule '" + aggregationRuleName + "' for event type '" + selectedEventTypeName + "'.");
		target.add(abstractSushiPage.getFeedbackPanel());
	}
	
	protected void addAggregationRule(SushiEventType selectedEventType, String aggregationRuleName, String aggregationRule) throws EPException {
		SushiAggregationRule sushiAggregationRule = new SushiAggregationRule(selectedEventType, aggregationRuleName, aggregationRule);
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
		
		aggregationRuleTree = new SushiSelectTree<SushiTreeElement<String>>("aggregationRuleTree", new TreeProvider<String>(generateNodesOfAggregationRuleTree()), new TreeExpansionModel<String>()) {
			
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
}
