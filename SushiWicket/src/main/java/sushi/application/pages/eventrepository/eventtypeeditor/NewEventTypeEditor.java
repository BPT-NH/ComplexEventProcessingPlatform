package sushi.application.pages.eventrepository.eventtypeeditor;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import sushi.application.components.form.WarnOnExitForm;
import sushi.application.components.tree.SushiAttributeTreeExpansion;
import sushi.application.components.tree.SushiAttributeTreeExpansionModel;
import sushi.application.components.tree.SushiAttributeTreeProvider;
import sushi.application.components.tree.SushiSelectTree;
import sushi.application.pages.AbstractSushiPage;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.eventhandling.Broker;

public class NewEventTypeEditor extends Panel {

	private static final long serialVersionUID = 1L;
	private TextField<String> eventTypeNameInput;
	private TextField<String> timestampNameInput;
	private String eventTypeName = new String();
	private String timestampName;
	private List<SushiAttributeTypeEnum> attributeTypes = Arrays.asList(SushiAttributeTypeEnum.values());
	private DropDownChoice<SushiAttributeTypeEnum> attributeTypeDropDownChoice;
	private SushiAttributeTypeEnum attributeType;
	private Form<Void> layoutForm;
	private TextField<String> eventTypeAttributeNameInput;
	private String attributeName;
	private SushiSelectTree<SushiAttribute> eventTypeTree;
	private SushiAttributeTree eventTypeAttributesTree = new SushiAttributeTree();
	private AbstractSushiPage abstractSushiPage;
	
	public NewEventTypeEditor (String id, AbstractSushiPage abstractSushiPage) {
 		super(id);
		this.abstractSushiPage = abstractSushiPage;
		
		layoutForm = new WarnOnExitForm("layoutForm");
		add(layoutForm);
		
		layoutForm.add(buildEventTypeNameInput());
		layoutForm.add(buildTimestampNameInput());
		
		layoutForm.add(buildEventTypeAttributeNameInput());
		layoutForm.add(buildEventTypeAttributeTypeDropDownChoice());
		
		buildEventTypeAttributeButtons();
		
		renderOrUpdateTree();
		
		addCreateEventTypeButton();
	}

	private void buildEventTypeAttributeButtons() {
	    AjaxButton editEventTypeAttributeButton = new AjaxButton("editEventTypeAttributeButton", layoutForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				SushiAttribute selectedElement = eventTypeTree.getSelectedElement();
				if (selectedElement != null) {
					attributeName = selectedElement.getName();
					attributeType = selectedElement.getType();
					
				}
				target.add(eventTypeAttributeNameInput);
	        }
	    };
	    
	    layoutForm.add(editEventTypeAttributeButton);
	    
	    AjaxButton deleteEventTypeAttributeButton = new AjaxButton("deleteEventTypeAttributeButton", layoutForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {

				if (eventTypeTree.getSelectedElement() != null) {
					SushiAttribute selectedAttribute = eventTypeTree.getSelectedElement();
					if (!selectedAttribute.hasParent()) {
						eventTypeAttributesTree.removeRoot(selectedAttribute);
					}
					selectedAttribute.removeAttribute();
				}
				renderOrUpdateTree();
				target.add(eventTypeTree);
				attributeName = null;
				target.add(eventTypeAttributeNameInput);
	        }

	    };
		
	    layoutForm.add(deleteEventTypeAttributeButton);
	    
	    AjaxButton addEventTypeAttributeButton = new AjaxButton("addEventTypeAttributeButton", layoutForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				
				if (attributeName != null) {
					SushiAttribute selectedAttribute = eventTypeTree.getSelectedElement();
					SushiAttribute newAttribute = new SushiAttribute(attributeName, attributeType);
					if (selectedAttribute == null) {
						if (!eventTypeAttributesTree.getRoots().contains(newAttribute)) {
							eventTypeAttributesTree.addRoot(newAttribute);
						}
					} else {
						if (!selectedAttribute.getChildren().contains(newAttribute)) {
							newAttribute.setParent(selectedAttribute);
							attributeName = null;
						} else {
							abstractSushiPage.getFeedbackPanel().error("Attribute with this name already exists in the selected node!");
							target.add(abstractSushiPage.getFeedbackPanel());
						}
					}
				}
				renderOrUpdateTree();
				target.add(eventTypeTree);
				target.add(eventTypeAttributeNameInput);
	        }
	    };
		
	    layoutForm.add(addEventTypeAttributeButton);
	}
	
	private void addCreateEventTypeButton() {
		AjaxButton addEventTypeAttributeButton = new AjaxButton("createEventTypeButton", layoutForm) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				// abfangen, dass Eventtyp nicht angegeben ist
				if (eventTypeName == null || eventTypeName.equals("")) {
					abstractSushiPage.getFeedbackPanel().error("Please provide a name for the event type");
				} else if (!SushiEventType.getAllTypeNames().contains(eventTypeName)) { // abfangen, dass Eventtyp mit dem Namen schon vorhanden ist
					SushiEventType eventType;
//					try {
						// abfangen, dass Timestamp nicht angegeben ist
						if (!(timestampName == null) && !timestampName.equals("")) {
							if (!eventTypeAttributesTree.getAttributesByExpression().contains(timestampName)) {
								System.out.println(eventTypeAttributesTree);
								eventType = new SushiEventType(eventTypeName, eventTypeAttributesTree, timestampName);
								Broker.send(eventType);
								eventTypeAttributesTree = new SushiAttributeTree();
								attributeName = null;
								eventTypeName = null;
								timestampName = null;
								renderOrUpdateTree();
								abstractSushiPage.getFeedbackPanel().success("Event type " + eventType.getTypeName() + " created");
							} else {
								abstractSushiPage.getFeedbackPanel().error("The timestamp should not be equal to one of the attributes in the tree below");
							}
						} else {
							abstractSushiPage.getFeedbackPanel().error("Please provide a name for the timestamp");
						}						
//					} catch (RuntimeException e) {
//						abstractSushiPage.getFeedbackPanel().error(e.getMessage());
//					}
				} else {
					abstractSushiPage.getFeedbackPanel().error("Event type with this name already exists!");
				}
				target.add(abstractSushiPage.getFeedbackPanel());
				target.add(eventTypeTree);
				target.add(eventTypeAttributeNameInput);
				target.add(eventTypeNameInput);
				target.add(timestampNameInput);
	        }
	    };
		
	    layoutForm.add(addEventTypeAttributeButton);
	}

	private Component buildEventTypeAttributeTypeDropDownChoice() {
		attributeTypeDropDownChoice = new DropDownChoice<SushiAttributeTypeEnum>("attributeTypeDropDownChoice", new PropertyModel<SushiAttributeTypeEnum>(this, "attributeType"), attributeTypes);
		attributeType = attributeTypes.get(0);
		attributeTypeDropDownChoice.setOutputMarkupId(true);
		return attributeTypeDropDownChoice;
	}

	private Component buildEventTypeNameInput() {
		eventTypeNameInput = new TextField<String>("eventTypeNameInput", new PropertyModel<String>(this, "eventTypeName"));
		eventTypeNameInput.setOutputMarkupId(true);
		return eventTypeNameInput;
	}

	private Component buildTimestampNameInput() {
		timestampNameInput = new TextField<String>("timestampNameInput", new PropertyModel<String>(this, "timestampName"));
		timestampNameInput.setOutputMarkupId(true);
		return timestampNameInput;
	}

	
	private Component buildEventTypeAttributeNameInput() {
		eventTypeAttributeNameInput = new TextField<String>("eventTypeAttributeNameInput", new PropertyModel<String>(this, "attributeName"));
		eventTypeAttributeNameInput.setOutputMarkupId(true);
		return eventTypeAttributeNameInput;
	}
	
	private void renderOrUpdateTree() {
		
		eventTypeTree = new SushiSelectTree<SushiAttribute>("eventTypeTree", new SushiAttributeTreeProvider(eventTypeAttributesTree.getRoots()), new SushiAttributeTreeExpansionModel());
		SushiAttributeTreeExpansion.get().expandAll();
        eventTypeTree.setOutputMarkupId(true);
		layoutForm.addOrReplace(eventTypeTree);
	}

//	private ArrayList<SushiAttribute> generateNodesOfEventTypeTree() {
//		ArrayList<SushiAttribute> treeElements = new ArrayList<SushiAttribute>();
//		SushiTreeElement<String> rootElement = new SushiTreeElement<String>(eventTypeName);
//		treeElements.add(rootElement);
//		fillTreeLevel(rootElement, eventTypeAttributesTree.getRootElements(), eventTypeAttributesTree);
//		return treeElements;
//	}
//	
//	private void fillTreeLevel(SushiTreeElement<String> parent, List<String> children, SushiTree<String> eventTypeAttributesTree) {
//		for (String newValue : children) {
//			SushiTreeElement<String> newElement = new SushiTreeElement<String>(parent, newValue.toString());
//			if (eventTypeAttributesTree.hasChildren(newValue)) {
//				fillTreeLevel(newElement, eventTypeAttributesTree.getChildren(newValue), eventTypeAttributesTree);
//			}
//		}
//	}
}
