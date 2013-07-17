package sushi.application.pages.simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.simulator.model.SimulationTreeTableElement;
import sushi.bpmn.decomposition.ANDComponent;
import sushi.bpmn.decomposition.IPattern;
import sushi.bpmn.decomposition.LoopComponent;
import sushi.bpmn.decomposition.SequenceComponent;
import sushi.bpmn.decomposition.XORComponent;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.correlation.CorrelationRule;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.simulation.ValueRule;
import sushi.simulation.ValueRuleType;
import sushi.util.Tuple;

public class AdvancedValuesPanel extends Panel{

	private SimulationPanel simulationPanel;
//	private DropDownChoice<SushiAttribute> attributeSelect;
//	private DropDownChoice<String> valueSelect;
	private List<SushiAttribute> attributeList;
	private WebMarkupContainer valueRuleMarkupContainer;
	private List<ValueRule> valueRules;
	private ListView<ValueRule> valueRuleListView;
	private final List<ValueRuleType> valueOptionList = new ArrayList<ValueRuleType>(Arrays.asList(ValueRuleType.EQUAL, ValueRuleType.UNEQUAL));

	public AdvancedValuesPanel(String id, final SimulationPanel simulationPanel) {
		super(id);
		this.simulationPanel = simulationPanel;
		this.attributeList = new ArrayList<SushiAttribute>();
		this.setOutputMarkupId(true);
		Form<Void> form = new Form<Void>("form");
		valueRules = new ArrayList<ValueRule>();
		
//		attributeSelect = new DropDownChoice<SushiAttribute>("attributeSelect", new Model<SushiAttribute>(), attributeList);
//		attributeSelect.setOutputMarkupId(true);
//		form.add(attributeSelect);
		
//		valueSelect = new DropDownChoice<String>("valueSelect", new Model<String>(), valueOptionList);
//		form.add(valueSelect);
		
		AjaxButton addButton = new AjaxButton("addButton", form) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				valueRules.add(new ValueRule());
				target.add(valueRuleMarkupContainer);
	        }
	    };
	    form.add(addButton);
	    
	    valueRuleListView = new ListView<ValueRule>("valueRuleListView", valueRules){

			@Override
			protected void populateItem(ListItem<ValueRule> item) {
				
				final ValueRule valueRule = item.getModelObject();
				
				final DropDownChoice<SushiAttribute> attributeSelect = new DropDownChoice<SushiAttribute>("attributeSelect", new Model<SushiAttribute>(), attributeList);
				attributeSelect.setOutputMarkupId(true);
				item.add(attributeSelect);
				attributeSelect.setModelObject(valueRule.getAttribute());
				
				attributeSelect.add(new AjaxFormComponentUpdatingBehavior("onChange") { 

					private static final long serialVersionUID = -4107411122913362658L;

					@Override 
					protected void onUpdate(AjaxRequestTarget target) {
						valueRule.setAttribute(attributeSelect.getModelObject());
						target.add(valueRuleMarkupContainer);
					}
				});
				
				final DropDownChoice<ValueRuleType> valueSelect = new DropDownChoice<ValueRuleType>("valueSelect", new Model<ValueRuleType>(), valueOptionList);
				valueSelect.setOutputMarkupId(true);
				item.add(valueSelect);
				
				valueSelect.setModelObject(valueRule.getRuleType());
				
				valueSelect.add(new AjaxFormComponentUpdatingBehavior("onChange") { 

					private static final long serialVersionUID = -4107411122913362658L;

					@Override 
					protected void onUpdate(AjaxRequestTarget target) {
						valueRule.setRuleType(valueSelect.getModelObject());
						target.add(valueRuleMarkupContainer);
					}
				});
			}
	    	
	    };
	    
	    valueRuleMarkupContainer = new WebMarkupContainer("valueRuleMarkupContainer");
	    valueRuleMarkupContainer.add(valueRuleListView);
	    valueRuleMarkupContainer.setOutputMarkupId(true);
	    form.addOrReplace(valueRuleMarkupContainer);
		add(form);
	}

	public List<Tuple<SushiAttribute, String>> getAdvancedValueRules(){
		List<Tuple<SushiAttribute, String>> advancedValueRules = new ArrayList<Tuple<SushiAttribute, String>>();
		
		return advancedValueRules;
	}
	
	public void refreshAttributeChoice(){
		attributeList.clear();
		attributeList.addAll(simulationPanel.getAttributesFromTable());
	}
	
	public List<ValueRule> getValueRules(){
		return valueRules;
	}
}
