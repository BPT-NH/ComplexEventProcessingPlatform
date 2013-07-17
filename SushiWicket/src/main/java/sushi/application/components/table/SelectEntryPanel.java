package sushi.application.components.table;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import sushi.application.components.table.model.AbstractDataProvider;

/**
 * This class renders a checkbox inside of a form.
 * The checkbox informs the given {@link AbstractDataProvider} about ajax state changes.
 * @author micha
 */
public class SelectEntryPanel extends Panel {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for a checkbox inside of a form.
	 * The checkbox informs the given {@link AbstractDataProvider} about ajax state changes.
	 * @param id
	 * @param entryId
	 * @param dataProvider
	 */
	public SelectEntryPanel(String id, final int entryId, final AbstractDataProvider dataProvider) {
		super(id);
		Form<Void> form = new Form<Void>("form");
		
		CheckBox checkBox = new CheckBox("checkBoxID", Model.of(dataProvider.isEntrySelected(entryId)));
		checkBox.add(new AjaxFormComponentUpdatingBehavior("onchange"){ 
			
			private static final long serialVersionUID = 1L;

			@Override 
			protected void onUpdate(AjaxRequestTarget target) {
				if(dataProvider.isEntrySelected(entryId)){
					dataProvider.deselectEntry(entryId);
				} else if(!dataProvider.isEntrySelected(entryId)){
					dataProvider.selectEntry(entryId);
				}
			}
		});
		
		form.add(checkBox);
		
		add(form);
	}
}
