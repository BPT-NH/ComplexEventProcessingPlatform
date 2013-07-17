package sushi.application.pages.transformation.patternbuilder.model;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import sushi.application.components.tree.TreeTableProvider;
import sushi.application.pages.transformation.patternbuilder.PatternBuilderPanel;
import sushi.event.SushiEventType;
import sushi.transformation.collection.SushiPatternTree;
import sushi.transformation.element.EventTypeElement;
import sushi.transformation.element.FilterExpressionElement;
import sushi.transformation.element.FilterExpressionOperatorEnum;


public class EventTypeElementOptionsPanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	private Form<Void> layoutForm;
	private EventTypeElement element;
	private TreeTableProvider<Serializable> provider;
	private SushiPatternTree tree;
	private PatternElementTreeTable table;
	private PatternBuilderPanel panel;
	
	public EventTypeElementOptionsPanel(String id, EventTypeElement element, SushiPatternTree tree, TreeTableProvider<Serializable> provider, PatternElementTreeTable table, PatternBuilderPanel panel) {
		super(id);
		
		this.element = element;
		this.provider = provider;
		this.tree = tree;
		this.table = table;
		this.panel = panel;
		
		layoutForm = new Form<Void>("layoutForm");
		buildAddFilterExpressionButton();
//		buildEventTypeDropDownChoice();
//		buildAddEventTypeElementButton();
		
		add(layoutForm);
	}

	private void buildAddFilterExpressionButton() {
		AjaxButton addFilterExpressionButton = new AjaxButton("addFilterExpressionButton", layoutForm) {
			private static final long serialVersionUID = -2611608162033482853L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				FilterExpressionElement newFilterExpressionElement = new FilterExpressionElement(((TreeTableProvider<Serializable>) table.getProvider()).getNextID(), FilterExpressionOperatorEnum.EQUALS);
				newFilterExpressionElement.setParent(element);
				tree.addElement(newFilterExpressionElement);
				provider.setRootElements(tree.getRoots());
				table.getSelectedElements().clear();
				target.add(table);
				panel.updateOnTreeElementSelection(target);
			}
		};
		layoutForm.add(addFilterExpressionButton);
	}

//	private void buildEventTypeDropDownChoice() {
//		List<SushiEventType> eventTypes = SushiEventType.findAll();
//		eventTypeDropDownChoice = new DropDownChoice<SushiEventType>("eventTypeDropDownChoice", new Model<SushiEventType>(), eventTypes);
//		eventTypeDropDownChoice.setOutputMarkupId(true);
//		layoutForm.add(eventTypeDropDownChoice);
//	}
//	
//	private void buildAddEventTypeElementButton() {
//		AjaxButton addEventTypeElementButton = new AjaxButton("addEventTypeElementButton", layoutForm) {
//			private static final long serialVersionUID = -2611608162033482853L;
//			@Override
//			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
//				if (eventTypeDropDownChoice.getModelObject() != null) {
//					EventTypeElement newEventTypeElement = new EventTypeElement(((TreeTableProvider<Serializable>) table.getProvider()).getNextID(), eventTypeDropDownChoice.getModelObject());
//					newEventTypeElement.setParent(element);
//					tree.addElement(newEventTypeElement);
//					provider.setRootElements(tree.getRoots());
//					table.getSelectedElements().clear();
//					target.add(table);
//				}
//			}
//		};
//		layoutForm.add(addEventTypeElementButton);
//	}
}
