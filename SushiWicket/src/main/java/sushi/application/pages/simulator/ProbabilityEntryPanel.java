package sushi.application.pages.simulator;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import sushi.application.components.tree.SushiLabelTreeTable;
import sushi.application.pages.simulator.model.SimulationTreeTableElement;
import sushi.application.pages.simulator.model.SimulationTreeTableProvider;

public class ProbabilityEntryPanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	private TextField<String> textField;
	private SushiLabelTreeTable<SimulationTreeTableElement<Object>, String> treeTable;

	public ProbabilityEntryPanel(String id, final int entryId, final SimulationTreeTableProvider<Object> simulationTreeTableProvider) {
		super(id);
		Form<Void> form = new Form<Void>("form");
		
		textField = new TextField<String>("textFieldID", Model.of(simulationTreeTableProvider.getInputForEntry(entryId)));
		textField.setOutputMarkupPlaceholderTag(true);
		textField.setOutputMarkupId(true);
		textField.add(new AjaxFormComponentUpdatingBehavior("onchange"){ 
			
			private static final long serialVersionUID = 1L;

			@Override 
			protected void onUpdate(AjaxRequestTarget target) {
				
				simulationTreeTableProvider.setInputForEntry(textField.getValue(), entryId);
				simulationTreeTableProvider.updateAllEqualInputFields(textField.getValue(), entryId);
				if(treeTable != null){
					target.add(treeTable);
				}
				else{
					target.add(getPage());
				}
			}
		});
		
		form.add(textField);
		
		add(form);
	}

	public void setTable(SushiLabelTreeTable<SimulationTreeTableElement<Object>, String> treeTable) {
		this.treeTable = treeTable;
	}
}
