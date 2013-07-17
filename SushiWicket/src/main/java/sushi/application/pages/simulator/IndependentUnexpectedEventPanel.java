package sushi.application.pages.simulator;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
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
import sushi.event.SushiEventType;


public class IndependentUnexpectedEventPanel extends Panel{

	private final List<String> effectList = new ArrayList<String>(Arrays.asList("Delay", "Cancel", "None"));
	private final List<SushiEventType> listOfAllEventTypes = SushiEventType.findAll(); 
	private WebMarkupContainer unexpectedEventMarkupContainer;
	private List<Object> unexpectedEvents;
	private ListView<Object> unexpectedEventsListView;

	
	public IndependentUnexpectedEventPanel(String id) {
		super(id);
		this.setOutputMarkupId(true);
		this.unexpectedEvents = new ArrayList<Object>();
		
		Form<Void> form = new Form<Void>("form");
		
		AjaxButton addButton = new AjaxButton("addButton", form) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				unexpectedEvents.add(new Object());
				target.add(unexpectedEventMarkupContainer);
	        }
	    };
	    form.add(addButton);
		
		unexpectedEventsListView = new ListView<Object>("unexpectedEventListView", unexpectedEvents){

			@Override
			protected void populateItem(ListItem<Object> item) {
				final TextField<String> occurenceInput = new TextField<String>("occurenceInput", new Model<String>());
				item.add(occurenceInput);
				final DropDownChoice<String> effectSelect = new DropDownChoice<String>("effectSelect", new Model<String>(), effectList);
				item.add(effectSelect);
				final DropDownChoice<SushiEventType> additionalEventTypeSelect = new DropDownChoice<SushiEventType>("additionalEventTypeSelect", new Model<SushiEventType>(), listOfAllEventTypes);
				item.add(additionalEventTypeSelect);
			}
	    	
	    };
	    unexpectedEventMarkupContainer = new WebMarkupContainer("unexpectedEventMarkupContainer");
	    unexpectedEventMarkupContainer.add(unexpectedEventsListView);
	    unexpectedEventMarkupContainer.setOutputMarkupId(true);
	    form.addOrReplace(unexpectedEventMarkupContainer);
		add(form);
	}
}
