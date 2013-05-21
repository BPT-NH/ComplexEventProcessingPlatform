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
import sushi.visualisation.SushiSimpleChartOptions;

import com.googlecode.wickedcharts.wicket6.highcharts.Chart;

public class VisualisationPage extends AbstractSushiPage {
	
	private static final long serialVersionUID = 1L;
	private AjaxButton addButton;
	private Form<Void> form;
	public ListView listview;
	public AddChartModal addChartModal;

	public VisualisationPage() {
		super();
		
		//event Type Digramm
	    add(new Chart("eventTypeChart", new EventTypePercentageDiagramm()));
	    
		// Create the modal window.
	    addChartModal = new AddChartModal("addChartModal", this);
        add(addChartModal);
	    
        form = new Form<Void>("form");
		form.add(addAddButton());
	    			  
	    add(form);
	    
	    addCharts();
	}
	
	
	
	private IModel<List<SushiSimpleChartOptions>> options = new LoadableDetachableModel<List<SushiSimpleChartOptions>>() {
        @Override
        protected List<SushiSimpleChartOptions> load() {
            return SushiSimpleChartOptions.findAll();
        }
    };
	
	
	private Component addAddButton() {
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
	
	@SuppressWarnings({ "unchecked" })
	private void addCharts() {
		listview = new ListView("listview", getOptions()) {
		    protected void populateItem(final ListItem item) {
		    	//prepare and add chart
		    	final SushiSimpleChartOptions currentOptions = (SushiSimpleChartOptions) item.getModelObject();
		    	try {
			    	switch (currentOptions.getType()) {
			    	case SPLATTER : {
			    		item.add(new Chart("chart", new SushiSplatterChartOptions(currentOptions)));
			    		break;
			    		}
			    	case BAR : {
			    		item.add(new Chart("chart", new SushiColumnChartOptions(currentOptions)));
			    		break;
			    		}
			    	}
		    	}catch(Exception e) {
		    		e.printStackTrace();
		    		item.add(new Label("chart", currentOptions.getTitle() + " : This Chart could not be built. Sorry."));
		    	}
		        
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

	public IModel<List<SushiSimpleChartOptions>> getOptions() {
		return options;
	}

	public void setOptions(IModel<List<SushiSimpleChartOptions>> options) {
		this.options = options;
	}
}
