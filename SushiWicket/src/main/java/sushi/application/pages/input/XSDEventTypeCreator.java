package sushi.application.pages.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.markup.html.repeater.tree.AbstractTree;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.io.IClusterable;

import sushi.FileUtils;
import sushi.application.components.form.WarnOnExitForm;
import sushi.application.components.tree.SushiAttributeTreeExpansion;
import sushi.application.components.tree.SushiAttributeTreeExpansionModel;
import sushi.application.components.tree.SushiAttributeTreeProvider;
import sushi.application.components.tree.SushiMultiSelectTree;
import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.main.MainPage;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.event.attribute.SushiAttributeTree;
import sushi.event.attribute.SushiAttributeTypeEnum;
import sushi.eventhandling.Broker;
import sushi.xml.importer.XMLParsingException;
import sushi.xml.importer.XSDParser;

public class XSDEventTypeCreator extends AbstractSushiPage{

	private TextField<String> eventTypeNameTextField;
	private String filePath;
	private SushiEventType importedEventType;
	private SushiAttributeTree eventTypeAttributesTree;
	private List<String> leafPathes = new ArrayList<String>();
	private DropDownChoice<String> timestampDropDownChoice;
	private String timestampXPath;
	private SushiMultiSelectTree<SushiAttribute> tree;
	private TextFieldDefaultValues textFieldDefaultValues;
	private Form<Void> layoutForm;
	private AbstractSushiPage xsdEventTypeCreator;
	private SushiAttribute timestamp;
	private AjaxCheckBox importTimeCheckBox;
	private Boolean eventTypeUsingImportTime = false;

	@SuppressWarnings("serial")
	public XSDEventTypeCreator(PageParameters parameters) {
		
		super();
		this.xsdEventTypeCreator = this;
		
		layoutForm = new WarnOnExitForm("layoutForm");
		add(layoutForm);
		
		filePath = parameters.get("filePath").toString();
		String schemaName = FileUtils.getFileNameWithoutExtension(filePath);

		try {
			importedEventType = XSDParser.generateEventTypeFromXSD(filePath, schemaName);
		} catch (XMLParsingException e1) {
			getFeedbackPanel().error(e1.getMessage());
		} catch (RuntimeException e2) {
			getFeedbackPanel().error(e2.getMessage());
		}
		eventTypeAttributesTree = importedEventType.getValueTypeTree();
		
		// timestamp must be a root attribute
		for (SushiAttribute element : eventTypeAttributesTree.getLeafAttributes()) {
			if (element.getType() == SushiAttributeTypeEnum.DATE) {
				leafPathes.add(element.getXPath());
			}
		}
		if (leafPathes.isEmpty()) {
			eventTypeUsingImportTime = true;
		} else {
			timestampXPath = leafPathes.get(0);
		}
		
		textFieldDefaultValues = new TextFieldDefaultValues();
		setDefaultModel(new CompoundPropertyModel<TextFieldDefaultValues>(textFieldDefaultValues));
		eventTypeNameTextField = new TextField<String>("eventTypeNameTextField");
		
		layoutForm.add(eventTypeNameTextField);
		
		importTimeCheckBox = new AjaxCheckBox("importTimeCheckBox", new PropertyModel<Boolean>(this, "eventTypeUsingImportTime")) {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
                if (eventTypeUsingImportTime) {
                	tree.getSelectedElements().remove(timestamp);
                	timestampDropDownChoice.setEnabled(false);
                } else {
                	tree.getSelectedElements().add(timestamp);
                	timestampDropDownChoice.setEnabled(true);
                }
                target.add(tree);
                target.add(timestampDropDownChoice);
            }
        };
		timestampDropDownChoice = new DropDownChoice<String>("timestampDropDownChoice", new PropertyModel<String>(this, "timestampXPath"), leafPathes);
		timestampDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				
				tree.getSelectedElements().remove(timestamp);
				if (timestamp.hasParent()) {
					deselectTreeElement(timestamp.getParent());
				}
				
				timestamp = eventTypeAttributesTree.getAttributeByXPath(timestampXPath);
				
		        tree.getSelectedElements().add(timestamp);
		        if (timestamp.hasParent()) {
					selectTreeElement(timestamp.getParent());
				}
				target.add(tree);
			}
		});

        if (leafPathes.isEmpty()) {
        	importTimeCheckBox.setEnabled(false);
        	timestampDropDownChoice.setEnabled(false);
        }
        importTimeCheckBox.setOutputMarkupId(true);
		timestampDropDownChoice.setOutputMarkupId(true);
		layoutForm.add(importTimeCheckBox);
		layoutForm.add(timestampDropDownChoice);
		 
		renderTree();
		buildButtons();
	}

	private void selectTreeElement(SushiAttribute element) {
		tree.getSelectedElements().add(element);
		if (element.hasParent()) {
			selectTreeElement(element);
		}
	}
	
	private void deselectTreeElement(SushiAttribute element) {
		ArrayList<SushiAttribute> children = element.getChildren();
		boolean hasChildren = false;
		for (SushiAttribute child : children) {
			if (tree.getSelectedElements().contains(child)) {
				hasChildren = true;
			}
		}
		if (!hasChildren) {
			tree.getSelectedElements().remove(element);
			if (element.hasParent()) {
				deselectTreeElement(element.getParent());
			}
		}
	}

	protected void renderTree() {
		tree = new SushiMultiSelectTree<SushiAttribute>("eventTypeTree", new SushiAttributeTreeProvider(eventTypeAttributesTree.getRoots()), new SushiAttributeTreeExpansionModel()) {
			
			@Override
			protected void toggle(SushiAttribute element, AbstractTree<SushiAttribute> tree, final AjaxRequestTarget target) {
				if (eventTypeUsingImportTime || !element.equals(timestamp)) {
					if (element.getType() != null) {
						super.toggle(element, tree, target);
					} else {
						ArrayList<SushiAttribute> children = element.getChildren();
						boolean hasChildren = false;
						for (SushiAttribute child : children) {
							if (selectedElements.contains(child)) {
								hasChildren = true;
							}
						}
						if (hasChildren) {
							selectedElements.add(element);
						} else {
							selectedElements.remove(element);
						}
						tree.updateNode(element, target);
					}
					if (element.hasParent()) {
						toggle(element.getParent(), tree, target);
					}
				}
			}
		};
		if (!leafPathes.isEmpty()) {
			timestamp = eventTypeAttributesTree.getAttributeByXPath(leafPathes.get(0));
		    tree.getSelectedElements().add(timestamp);
		}
		SushiAttributeTreeExpansion.get().expandAll();
	    tree.setOutputMarkupId(true);
		layoutForm.add(tree);
	}
	
	private void buildButtons() {
		
		AjaxButton selectAllButton = new AjaxButton("selectAllButton") {
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				tree.getSelectedElements().addAll(eventTypeAttributesTree.getAttributes());
				target.add(tree);
			}
		};
		layoutForm.add(selectAllButton);
		
		AjaxButton unselectAllButton = new AjaxButton("unselectAllButton") {
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				tree.getSelectedElements().clear();
				if (!eventTypeUsingImportTime) {
					tree.getSelectedElements().add(timestamp);
				}
				target.add(tree);
			}
		};
		layoutForm.add(unselectAllButton);
		
		Button confirmButton = new Button("confirmButton") {
			@Override
			public void onSubmit() {
				Set<SushiAttribute> selectedNodes = tree.getSelectedElements();
				eventTypeAttributesTree.retainAllAttributes(selectedNodes);
				try {
					importedEventType.setTypeName(eventTypeNameTextField.getModelObject());
					if (eventTypeUsingImportTime) {
						importedEventType.setTimestampName(XSDParser.GENERATED_TIMESTAMP_COLUMN_NAME);
					} else {
						String timestampName = timestamp.getAttributeExpression();
						importedEventType.setTimestampName(timestampName);
						if (!timestamp.hasParent()) {
							eventTypeAttributesTree.removeRoot(timestamp);
							importedEventType.setValueTypeTree(eventTypeAttributesTree);
						}
						timestamp.removeAttribute();
					}
					if (SushiEventType.findByTypeName(importedEventType.getTypeName()) != null) {
						xsdEventTypeCreator.getFeedbackPanel().error("Event type " + importedEventType.getTypeName() + " already exists");
						return;
					}
					System.out.println(importedEventType);
					Broker.send(importedEventType);
					PageParameters pageParameters = new PageParameters();
					pageParameters.add("successFeedback", "Event type " + importedEventType.getTypeName() + " has been created");
					setResponsePage(MainPage.class, pageParameters);
				} catch (RuntimeException e) {
					e.printStackTrace();
					xsdEventTypeCreator.getFeedbackPanel().error("Event type has not been created. See output console for more details.");
				}
			}
		};
		layoutForm.add(confirmButton);
	}

	private class TextFieldDefaultValues implements IClusterable {
		
		private static final long serialVersionUID = 1L;
		
        public String eventTypeNameTextField = FileUtils.getFileNameWithoutExtension(filePath);
        
        @Override
        public String toString()
        {
            return "eventTypeNameTextField = '" + eventTypeNameTextField + "'";
        }

		public String getEventTypeNameTextField() {
			return eventTypeNameTextField;
		}

		public void setEventTypeNameTextField(String eventTypeNameTextField) {
			this.eventTypeNameTextField = eventTypeNameTextField;
		}
    }
}
