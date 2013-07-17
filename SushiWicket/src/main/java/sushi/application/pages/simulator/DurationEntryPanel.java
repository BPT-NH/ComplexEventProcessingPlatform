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
import sushi.simulation.DerivationType;

public class DurationEntryPanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	private TextField<String> durationTextField, derivationTextField;
	private SushiLabelTreeTable<SimulationTreeTableElement<Object>, String> treeTable;
	private DerivationType derivationType;

	public DurationEntryPanel(String id, final int entryId, final SimulationTreeTableProvider<Object> simulationTreeTableProvider) {
		super(id);
		this.derivationType = DerivationType.FIXED;
		Form<Void> form = new Form<Void>("form");
		
		durationTextField = new TextField<String>("durationTextField", Model.of(simulationTreeTableProvider.getDurationForEntry(entryId)));
		durationTextField.setOutputMarkupPlaceholderTag(true);
		durationTextField.setOutputMarkupId(true);
		durationTextField.add(new AjaxFormComponentUpdatingBehavior("onchange"){ 
			
			private static final long serialVersionUID = 1L;

			@Override 
			protected void onUpdate(AjaxRequestTarget target) {
				
				simulationTreeTableProvider.setDurationForEntry(getMeanDurationFromField(), entryId);
			}
		});
		
		form.add(durationTextField);
		
		derivationTextField = new TextField<String>("derivationTextField", Model.of(simulationTreeTableProvider.getDerivationForEntry(entryId)));
		derivationTextField.setOutputMarkupPlaceholderTag(true);
		derivationTextField.setOutputMarkupId(true);
		derivationTextField.add(new AjaxFormComponentUpdatingBehavior("onchange"){ 
			
			private static final long serialVersionUID = 1L;

			@Override 
			protected void onUpdate(AjaxRequestTarget target) {
				
				simulationTreeTableProvider.setDerivationForEntry(getMeanDurationFromField(), entryId);
			}
		});
		DerivationType derivationType = simulationTreeTableProvider.getDerivationTypeForEntry(entryId);
		if(!(DerivationType.NORMAL.equals(derivationType))){
			derivationTextField.setVisible(false);
		}
		form.add(derivationTextField);
		
		add(form);
		simulationTreeTableProvider.registerDurationInputAtEntry(this, entryId);
	}

	public void setTable(SushiLabelTreeTable<SimulationTreeTableElement<Object>, String> treeTable) {
		this.treeTable = treeTable;
	}
	
	private String getMeanDurationFromField(){
		return durationTextField.getValue();
	}
	
	private String getDerivationFromField(){
		return durationTextField.getValue();
	}

	public void setDerivationType(DerivationType derivationType) {
		if(derivationType.equals(DerivationType.NORMAL)){
			derivationTextField.setVisible(true);
		}
		else{
			derivationTextField.setVisible(false);
		}
	}
}
