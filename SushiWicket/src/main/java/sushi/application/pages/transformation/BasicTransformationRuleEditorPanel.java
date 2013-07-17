package sushi.application.pages.transformation;

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
import sushi.transformation.TransformationManager;
import sushi.transformation.TransformationRule;

import com.espertech.esper.client.EPException;

public class BasicTransformationRuleEditorPanel extends Panel {
	
	private static final long serialVersionUID = -3517674159437927655L;
	private String selectedEventTypeName;
	private SushiEventType selectedEventType;
	private String transformationRule;
	private TextField<String> transformationRuleNameTextField;
	private String transformationRuleName;
	private Form<Void> layoutForm;
	private TextArea<String> transformationRuleTextArea;
	private SushiSelectTree<SushiTreeElement<String>> transformationRuleTree;
	private SushiTree<String> transformationRuleTreeStructure;
	private DropDownChoice<String> eventTypeDropDownChoice;
	private TransformationPage transformationPage;
	private TransformationManager transformationManager;

	public BasicTransformationRuleEditorPanel(String id, final TransformationPage transformationPage) {
		super(id);
		
		this.transformationPage = transformationPage;
		this.transformationManager = transformationPage.getTransformationManager();
		
		layoutForm = new WarnOnExitForm("layoutForm");
		add(layoutForm);
		
		buildTextFields();
		buildTransformationRuleTree();
		buildButtons();
	}
	
	private void buildTextFields() {
		transformationRuleNameTextField = new TextField<String>("transformationRuleNameTextField", new PropertyModel<String>(this, "transformationRuleName"));
		transformationRuleNameTextField.setOutputMarkupId(true);
		layoutForm.add(transformationRuleNameTextField);
		
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
		
		transformationRuleTextArea = new TextArea<String>("transformationRuleTextArea", new PropertyModel<String>(this, "transformationRule"));
		transformationRuleTextArea.setOutputMarkupId(true);	
		layoutForm.add(transformationRuleTextArea);
	}
	
	protected void updateOnChangeOfDropDownChoice(AjaxRequestTarget target) {
		selectedEventType = SushiEventType.findByTypeName(selectedEventTypeName);
		
		if (selectedEventType != null) {
			
			// inform user about rule schema
			
			List<SushiAttribute> selectedEventTypeAttributes = selectedEventType.getValueTypes();
			
			StringBuffer transformationSuggestionBuffer = new StringBuffer();
			transformationSuggestionBuffer.append("Your transformation rule may start with: SELECT ");
			if (selectedEventType.getTimestampName() != null) {
				transformationSuggestionBuffer.append("[...] AS Timestamp, ");
				
			}
			
			for (int i = 0; i < selectedEventTypeAttributes.size(); i++) {
				transformationSuggestionBuffer.append("[...] AS " + selectedEventTypeAttributes.get(i).getAttributeExpression());
				if (i == selectedEventTypeAttributes.size() - 1) {
					transformationSuggestionBuffer.append(" ");
				} else {
					transformationSuggestionBuffer.append(", ");
				}
			}
			transformationPage.getFeedbackPanel().info(transformationSuggestionBuffer.toString());
			target.add(transformationPage.getFeedbackPanel());
		} else {
			target.add(transformationPage.getFeedbackPanel());
		}
	}

	private void buildButtons() {
		
		AjaxButton editButton = new AjaxButton("editButton") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				
				if (transformationRuleTree.getSelectedElement() != null) {
					selectedEventTypeName = transformationRuleTree.getSelectedElement().getParent().getValue().toString();
					transformationRuleName = transformationRuleTree.getSelectedElement().getValue().toString();
					transformationRule = TransformationRule.findByEventTypeAndTitle(selectedEventTypeName, transformationRuleName).getQuery();
					target.add(transformationRuleNameTextField);
					target.add(eventTypeDropDownChoice);
					target.add(transformationRuleTextArea);
					updateOnChangeOfDropDownChoice(target);
				}
	        }
	    };
	    layoutForm.add(editButton);
	    
	    AjaxButton deleteButton = new AjaxButton("deleteButton") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				if (transformationRuleTree.getSelectedElement() != null) {
					String eventTypeNameFromTree = transformationRuleTree.getSelectedElement().getParent().getValue().toString();
					String transformationRuleNameFromTree = transformationRuleTree.getSelectedElement().getValue().toString();
					removeTransformationRule(eventTypeNameFromTree, transformationRuleNameFromTree);
					renderOrUpdateTransformationRuleTree();
					target.add(transformationRuleTree);
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
					if (transformationRuleName == null || transformationRuleName.isEmpty()) {
						transformationPage.getFeedbackPanel().error("Please enter an transformation rule name!");
						target.add(transformationPage.getFeedbackPanel());
					} else if (selectedEventType == null) {
						transformationPage.getFeedbackPanel().error("Please select an event type!");
						target.add(transformationPage.getFeedbackPanel());
					} else if (transformationRule == null || transformationRule.isEmpty()) {
						transformationPage.getFeedbackPanel().error("Please enter an transformation rule!");
						target.add(transformationPage.getFeedbackPanel());
					} else {
						saveTransformationRule(target, selectedEventType, transformationRuleName, transformationRule);
					}
				} catch (EPException e) { 
					transformationPage.getFeedbackPanel().error(e.getMessage());
					target.add(transformationPage.getFeedbackPanel());
				}
	        }
	    };
	    layoutForm.add(saveButton);
	}

	private void clearFields(AjaxRequestTarget target) {
		transformationRuleName = "";
		target.add(transformationRuleNameTextField);
		selectedEventTypeName = null;
		target.add(eventTypeDropDownChoice);
		transformationRule = "";
		target.add(transformationRuleTextArea);
	}
	
	private void saveTransformationRule(AjaxRequestTarget target, SushiEventType selectedEventType, String transformationRuleName, String transformationRule) {
		if (TransformationRule.findByEventTypeAndTitle(selectedEventTypeName, transformationRuleName) != null) {
			removeTransformationRule(selectedEventTypeName, transformationRuleName);
		}
		addTransformationRule(selectedEventType, transformationRuleName, transformationRule);
		renderOrUpdateTransformationRuleTree();
		target.add(transformationRuleTree);
		transformationPage.getFeedbackPanel().success("Saved transformation rule '" + transformationRuleName + "' for event type '" + selectedEventTypeName + "'.");
		target.add(transformationPage.getFeedbackPanel());
	}
	
	protected void addTransformationRule(SushiEventType selectedEventType, String transformationRuleName, String transformationQuery) throws EPException {
		TransformationRule transformationRule = new TransformationRule(selectedEventType, transformationRuleName, transformationQuery);
		transformationManager.register(transformationRule);
		transformationRule.save();
		String eventTypeName = selectedEventType.getTypeName();
		addTransformationRuleToTreeStructure(eventTypeName, transformationRuleName);
	}

	protected void removeTransformationRule(String eventTypeName, String title) {
		transformationRuleTreeStructure.removeChild(eventTypeName, title);
		TransformationRule transformationRule = TransformationRule.findByEventTypeAndTitle(eventTypeName, title);
		transformationManager.removeFromEsper(transformationRule);
		transformationRule.remove();
	}

	private void addTransformationRuleToTreeStructure(String eventTypeName, String transformationRuleName) {
		if (!transformationRuleTreeStructure.containsRootElement(eventTypeName)) {
			transformationRuleTreeStructure.addRootElement(eventTypeName);
		}
		transformationRuleTreeStructure.addChild(eventTypeName, transformationRuleName);
	}

	private void buildTransformationRuleTree() {
		transformationRuleTreeStructure = new SushiTree<String>();
		List<TransformationRule> transformationRules = TransformationRule.findAll();
		for (TransformationRule transformationRule : transformationRules) {
			addTransformationRuleToTreeStructure(transformationRule.getEventType().getTypeName(), transformationRule.getTitle());
		}
		renderOrUpdateTransformationRuleTree();
	}
	
	private void renderOrUpdateTransformationRuleTree() {
		
		transformationRuleTree = new SushiSelectTree<SushiTreeElement<String>>("transformationRuleTree", new TreeProvider<String>(generateNodesOfTransformationRuleTree()), new TreeExpansionModel<String>()) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void select(SushiTreeElement<String> element, AbstractTree<SushiTreeElement<String>> tree, final AjaxRequestTarget target) {
				// only the transformation rules are selectable
				if (element.hasParent()) {
					super.select(element, tree, target);
				}
			}
		};
        TreeExpansion.get().expandAll();
        transformationRuleTree.setOutputMarkupId(true);
		layoutForm.addOrReplace(transformationRuleTree);
	}
	
	private ArrayList<SushiTreeElement<String>> generateNodesOfTransformationRuleTree() {
		ArrayList<SushiTreeElement<String>> treeElements = new ArrayList<SushiTreeElement<String>>();
		List<String> eventTypes = transformationRuleTreeStructure.getRootElements();
		for (String eventType : eventTypes) {
			SushiTreeElement<String> rootElement = new SushiTreeElement<String>(eventType);
			treeElements.add(rootElement);
			if (transformationRuleTreeStructure.hasChildren(eventType)) {
				fillTreeLevel(rootElement, transformationRuleTreeStructure.getChildren(eventType), transformationRuleTreeStructure);
			}
		}
		return treeElements;
	}
	
	private void fillTreeLevel(SushiTreeElement<String> parent, List<String> children, SushiTree<String> transformationRuleTreeStructure) {
		for (String newValue : children) {
			SushiTreeElement<String> newElement = new SushiTreeElement<String>(parent, newValue.toString());
			if (transformationRuleTreeStructure.hasChildren(newValue)) {
				fillTreeLevel(newElement, transformationRuleTreeStructure.getChildren(newValue), transformationRuleTreeStructure);
			}
		}
	}
}
