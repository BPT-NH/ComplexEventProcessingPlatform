package sushi.application.pages.monitoring.visualisation;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import sushi.application.pages.AbstractSushiPage;
import sushi.visualisation.SushiChartConfiguration;

import com.googlecode.wickedcharts.wicket6.highcharts.Chart;

/**
 * 	This page offers shows a percentage chart of the events by event type, offers a possibility to create an attribute chart
	and shows the attribute charts already configured.
 */
public class AttributeChartPage extends AbstractSushiPage {
	
	private static final long serialVersionUID = 1L;
	private AjaxButton addButton;
	private Form<Void> form;
	public ListView listview;
	public AddChartModal addChartModal;

	//loads all SushiChartConfiguration-Objects from the database
	@SuppressWarnings("serial")
	private IModel<List<SushiChartConfiguration>> options = new LoadableDetachableModel<List<SushiChartConfiguration>>() {
        @Override
        protected List<SushiChartConfiguration> load() {
            return SushiChartConfiguration.findAll();
        }
    };
	
	public AttributeChartPage() {
		super();
		
		//pie chart of percentage of events by event type
	    add(new Chart("eventTypeChart", new EventTypePercentageDiagramm()));
	  	    
		// Create the modal window for attribute chart creation.
	    addChartModal = new AddChartModal("addChartModal", this);
        add(addChartModal);
	    
        form = new Form<Void>("form");
		form.add(addAddChartButton());
	    			  
	    add(form);
	    
	    //add attribute charts from database
	    addCharts();
	}
	
	/**
	 * creates the button that opens the addChartModal
	 * @return AjaxButton, that opens the addChartModal
	 */
	private Component addAddChartButton() {
		addButton = new AjaxButton("addChartButton") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				target.prependJavaScript("Wicket.Window.unloadConfirmation = false;");
				addChartModal.show(target);
			}
		};
		return addButton;
	}
	
	/**
	 * adds all attribute charts from database to page
	 */
	@SuppressWarnings({ "unchecked" })
	private void addCharts() {
		listview = new ListView("listview", getOptions()) {
		    protected void populateItem(final ListItem item) {
		    	//prepare and add chart
		    	final SushiChartConfiguration currentOptions = (SushiChartConfiguration) item.getModelObject();
		    	item.add(addChart(currentOptions));
		        //prepare and add removeButton
		        AjaxButton removeButton = new AjaxButton("removeChartButton") {
					private static final long serialVersionUID = 1L;

					public void onSubmit(AjaxRequestTarget target, Form form) {
						currentOptions.remove();
						getOptions().detach();
						target.add(listview.getParent());
					}
		        };
		        Form<Void> removeform = new Form<Void>("form");
		        removeform.add(removeButton);
		        item.add(removeform);
		    }

		};
		listview.setOutputMarkupId(true);
		
		add(listview);
	}

	/**
	 * Forms the chartConfiguration into a chart-Component, that can be displayed
	 * @param chartConfiguration
	 * @return chart-component
	 */
	private Component addChart(SushiChartConfiguration currentOptions) {
		try {
	    	switch (currentOptions.getType()) {
	    	case SPLATTER : {
	    		return new Chart("chart", new SushiSplatterChartOptions(currentOptions));
	    		}
	    	case COLUMN : {
	    		 return new Chart("chart", new SushiColumnChartOptions(currentOptions));
	    		}
	    	}
    	}catch(Exception e) {
    		e.printStackTrace();
    		return new Label("chart", currentOptions.getTitle() + " : This Chart could not be built. Sorry.");
    	}
		return new Label("chart", currentOptions.getTitle() + " : Unsupported Chart type. Sorry.");
	}

	
	public IModel<List<SushiChartConfiguration>> getOptions() {
		return options;
	}

	public void setOptions(IModel<List<SushiChartConfiguration>> options) {
		this.options = options;
	}
}
