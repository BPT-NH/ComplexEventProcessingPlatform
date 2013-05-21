package sushi.application.pages.aggregation.patternbuilder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import sushi.aggregation.collection.SushiPatternTree;
import sushi.application.components.tree.TreeTableProvider;
import sushi.application.pages.aggregation.patternbuilder.PatternBuilderPanel;
import sushi.event.collection.SushiTreeElement;

public class ElementOptionsPanel extends Panel {
	
	private static final long serialVersionUID = 1L;

	public ElementOptionsPanel(String id, final SushiTreeElement<Serializable> element, final PatternBuilderPanel patternBuilderPanel) {
		super(id);
		
		Form<Void> layoutForm = new Form<Void>("layoutForm");
		
		final SushiPatternTree tree = patternBuilderPanel.getPatternTree();
		final PatternElementTreeTable table = patternBuilderPanel.getPatternTreeTable();
		final TreeTableProvider<Serializable> provider = patternBuilderPanel.getPatternTreeTableProvider();
		
		AjaxButton moveUpButton = new AjaxButton("moveUpButton", layoutForm) {
			private static final long serialVersionUID = -3745820767717288739L;
			private List<SushiTreeElement<Serializable>> relatedElements = new ArrayList<SushiTreeElement<Serializable>>();
			@Override
			public boolean isVisible() {
				if (element.hasParent()) {
					relatedElements = element.getParent().getChildren();
				} else {
					return false;
				}
				return relatedElements.indexOf(element) > 0;
			}
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Collections.swap(relatedElements, relatedElements.indexOf(element) - 1, relatedElements.indexOf(element));
				element.getParent().setChildren(relatedElements);
				target.add(table);
			}
		};
		layoutForm.add(moveUpButton);
		
		AjaxButton moveDownButton = new AjaxButton("moveDownButton", layoutForm) {
			private static final long serialVersionUID = -3745820767717288739L;
			private List<SushiTreeElement<Serializable>> relatedElements = new ArrayList<SushiTreeElement<Serializable>>();
			@Override
			public boolean isVisible() {
				if (element.hasParent()) {
					relatedElements = element.getParent().getChildren();
				} else {
					return false;
				}
				return (relatedElements.size() > 1) && (relatedElements.indexOf(element) < relatedElements.size() - 1);
			}
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Collections.swap(relatedElements, relatedElements.indexOf(element), relatedElements.indexOf(element) + 1);
					element.getParent().setChildren(relatedElements);
				target.add(table);
			}
		};
		layoutForm.add(moveDownButton);

		AjaxButton removeElementButton = new AjaxButton("removeElementButton", layoutForm) {
			private static final long serialVersionUID = 5743864457433235849L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				tree.removeElement(element);
				table.getSelectedElements().remove(element);
				if (!element.hasParent()) {
					provider.setRootElements(tree.getRoots());
//				} else {
//					if (!element.getParent().hasChildren()) {
						// TODO: implement intelligent removal of parent elements
//					}
				}
				target.add(table);
				patternBuilderPanel.updateOnTreeElementSelection(target);
	        }
	    };
		layoutForm.add(removeElementButton);
		
		add(layoutForm);
	}
	
	
}
