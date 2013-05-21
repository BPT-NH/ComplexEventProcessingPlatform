package sushi.application.pages.simulator;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import sushi.application.components.tree.SushiLabelTreeTable;
import sushi.application.pages.simulator.model.SimulationTreeTableElement;
import sushi.application.pages.simulator.model.SimulationTreeTableProvider;
import sushi.simulation.DerivationType;

public class DerivationTypeDropDownChoicePanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	private List<DerivationType> derivationTypes = Arrays.asList(DerivationType.values());
	private SushiLabelTreeTable<SimulationTreeTableElement<Object>, String> treeTable;
	protected DerivationType derivationType; 
	
	public DerivationTypeDropDownChoicePanel(String id, final int entryId, final SimulationTreeTableProvider<Object> simulationTreeTableProvider) {
		super(id);
		Form<Void> layoutForm = new Form<Void>("layoutForm");

		
		final DropDownChoice<DerivationType> derivationTypeDropDownChoice = new DropDownChoice<DerivationType>("derivationTypeDropDownChoice", Model.of(simulationTreeTableProvider.getDerivationTypeForEntry(entryId)), derivationTypes);
		derivationTypeDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			
			private static final long serialVersionUID = 1L;
	
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				simulationTreeTableProvider.setDerivationTypeForEntry(derivationTypeDropDownChoice.getModelObject(), entryId);
				
				if(treeTable != null){
					target.add(treeTable);
				}
				else{
					target.add(getPage());
				}
			}
		});
		
		derivationTypeDropDownChoice.setEnabled(true);
		layoutForm.add(derivationTypeDropDownChoice);		
		add(layoutForm);
	}
	
	public void setTable(SushiLabelTreeTable<SimulationTreeTableElement<Object>, String> treeTable) {
		this.treeTable = treeTable;
	}

}
