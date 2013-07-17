package sushi.application.pages.transformation;

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

import sushi.application.components.form.WarnOnExitForm;
import sushi.application.components.tree.SushiSelectTree;
import sushi.application.components.tree.TreeExpansion;
import sushi.application.components.tree.TreeExpansionModel;
import sushi.application.components.tree.TreeProvider;
import sushi.application.pages.transformation.patternbuilder.AttributeTreePanel;
import sushi.application.pages.transformation.patternbuilder.PatternBuilderPanel;
import sushi.event.SushiEventType;
import sushi.event.collection.SushiTree;
import sushi.event.collection.SushiTreeElement;
import sushi.transformation.TransformationManager;
import sushi.transformation.TransformationRule;
import sushi.transformation.collection.SushiPatternTree;
import sushi.transformation.element.externalknowledge.ExternalKnowledgeExpressionSet;

import com.espertech.esper.client.EPException;

import de.agilecoders.wicket.markup.html.bootstrap.tabs.Collapsible;

public class AdvancedTransformationRuleEditorPanel extends Panel {
	
	private static final long serialVersionUID = -3517674159437927655L;
	private TextField<String> transformationRuleNameTextField;
	private String transformationRuleNameFromTree;
	private Form<Void> layoutForm;
	private SushiSelectTree<SushiTreeElement<String>> transformationRuleTree;
	private SushiTree<String> transformationRuleTreeStructure;
	private TransformationPage transformationPage;
	private TransformationManager transformationManager;
	private AdvancedTransformationRuleEditorPanel advancedRuleEditorPanel;
	protected AttributeTreePanel attributeTreePanel;
	protected PatternBuilderPanel patternBuilderPanel;

	public AdvancedTransformationRuleEditorPanel(String id, final TransformationPage transformationPage) {
		super(id);
		
		this.transformationPage = transformationPage;
		this.transformationManager = transformationPage.getTransformationManager();
		this.advancedRuleEditorPanel = this;
		
		layoutForm = new WarnOnExitForm("layoutForm");
		add(layoutForm);
		
		addTabs();
		buildTextFields();
		buildTransformationRuleTree();
		buildButtons();
	}
	
	private void buildTextFields() {
		transformationRuleNameTextField = new TextField<String>("transformationRuleNameTextField", new PropertyModel<String>(this, "transformationRuleNameFromTree"));
		transformationRuleNameTextField.setOutputMarkupId(true);
		layoutForm.add(transformationRuleNameTextField);
	}

	private void buildButtons() {
		
		AjaxButton editButton = new AjaxButton("editButton") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				
				if (transformationRuleTree.getSelectedElement() != null) {
					String eventTypeNameFromTree = transformationRuleTree.getSelectedElement().getParent().getValue().toString();
					transformationRuleNameFromTree = transformationRuleTree.getSelectedElement().getValue().toString();
					TransformationRule transformationRule = TransformationRule.findByEventTypeAndTitle(eventTypeNameFromTree, transformationRuleNameFromTree);
					patternBuilderPanel.setPatternTree(new SushiPatternTree(transformationRule.getPatternTree().getElements()));
					patternBuilderPanel.updatePatternTreeTable(target);
					target.add(transformationRuleNameTextField);
					SushiEventType selectedEventType = transformationRule.getEventType();
					attributeTreePanel.setSelectedEventType(selectedEventType);
					target.add(attributeTreePanel.getEventTypeDropDownChoice());
					attributeTreePanel.updateAttributeTreeTable(target, transformationRule.getAttributeIdentifiersAndExpressions(), transformationRule.getAttributeIdentifiersWithExternalKnowledge());
				}
	        }
	    };
	    layoutForm.add(editButton);
//	    
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
				SushiEventType selectedEventType = attributeTreePanel.getSelectedEventType();
				Map<String, String> attributeIdentifiersAndExpressions = attributeTreePanel.getAttributeIdentifiersAndExpressions();
				Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersWithExternalKnowledge = attributeTreePanel.getAttributeIdentifiersWithExternalKnowledge();
				SushiPatternTree patternTree = patternBuilderPanel.getPatternTree();
				try {
					transformationManager.checkForValidity(selectedEventType, transformationRuleNameFromTree, attributeIdentifiersAndExpressions, attributeIdentifiersWithExternalKnowledge, patternTree);
					saveTransformationRule(target, selectedEventType, transformationRuleNameFromTree, attributeIdentifiersAndExpressions, attributeIdentifiersWithExternalKnowledge, patternTree);
					clearFields(target);
				} catch (EPException e) { 
					transformationPage.getFeedbackPanel().error("Transformation rule could not be saved. Please check if you have provided the correct attributes for the pattern and in the attribute selection. Full error message: " + e.getMessage());
					target.add(transformationPage.getFeedbackPanel());
				} catch (RuntimeException e) {
					transformationPage.getFeedbackPanel().error(e.getMessage());
					target.add(transformationPage.getFeedbackPanel());
				}
	        }
	    };
	    layoutForm.add(saveButton);
	}

	private void clearFields(AjaxRequestTarget target) {
		transformationRuleNameFromTree = "";
		target.add(transformationRuleNameTextField);
		
		patternBuilderPanel.clear(target);
		attributeTreePanel.clear(target);
	}
	
	private void saveTransformationRule(AjaxRequestTarget target, SushiEventType selectedEventType, String transformationRuleName, Map<String, String> attributeIdentifiersAndExpressions, Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersWithExternalKnowledge, SushiPatternTree patternTree) {
		if (TransformationRule.findByEventTypeAndTitle(selectedEventType.getTypeName(), transformationRuleName) != null) {
			removeTransformationRule(selectedEventType.getTypeName(), transformationRuleName);
		}
		addTransformationRule(selectedEventType, transformationRuleName, attributeIdentifiersAndExpressions, attributeIdentifiersWithExternalKnowledge, patternTree);
		renderOrUpdateTransformationRuleTree();
		target.add(transformationRuleTree);
		transformationPage.getFeedbackPanel().success("Saved transformation rule '" + transformationRuleName + "' for event type '" + selectedEventType.getTypeName() + "'.");
		target.add(transformationPage.getFeedbackPanel());
	}
	
	protected void addTransformationRule(SushiEventType selectedEventType, String transformationRuleName, Map<String, String> attributeIdentifiersAndExpressions, Map<String, ExternalKnowledgeExpressionSet> attributeIdentifiersWithExternalKnowledge, SushiPatternTree patternTree) throws EPException {
		TransformationRule transformationRule = transformationManager.createTransformationRule(selectedEventType, transformationRuleName, patternTree, attributeIdentifiersAndExpressions, attributeIdentifiersWithExternalKnowledge);
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
		transformationRuleTree = new SushiSelectTree<SushiTreeElement<String>>("transformationRuleTree", new TreeProvider(generateNodesOfTransformationRuleTree()), new TreeExpansionModel()) {
			
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

	public TransformationPage getTransformationPage() {
		return transformationPage;
	}
}
