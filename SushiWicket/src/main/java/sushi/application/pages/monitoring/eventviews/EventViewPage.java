package sushi.application.pages.monitoring.eventviews;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.monitoring.eventviews.EventViewOptions;
import sushi.event.SushiEvent;
import sushi.event.SushiEventType;
import sushi.user.SushiUser;
import sushi.visualisation.SushiEventView;
import sushi.visualisation.SushiChartConfiguration;

import com.googlecode.wickedcharts.wicket6.highcharts.Chart;

/**
 * This page displays the existing event views and allows to add new ones.
 */
@SuppressWarnings("serial")
public class EventViewPage extends AbstractSushiPage {
	
	private AjaxButton addButton;
	private Form<Void> form;
	public ListView listview;
	public AddViewModal addViewModal;
	
	IModel<List<SushiEventView>> views = new LoadableDetachableModel<List<SushiEventView>>() {
        @Override
        protected List<SushiEventView> load() {
            return SushiEventView.findAll();
        }
    };
	
	public EventViewPage() {
		super();
          
		// Create the modal window.
	    addViewModal = new AddViewModal("addViewModal", this);
        add(addViewModal);
	    
        form = new Form<Void>("form");
		form.add(addAddButton());
	    add(form);
	    
	    addViews();
	}
	
	private Component addAddButton() {
		addButton = new AjaxButton("addViewButton") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				target.prependJavaScript("Wicket.Window.unloadConfirmation = false;");
				addViewModal.show(target);
			}
		};
		return addButton;
	}
	
	@SuppressWarnings({ "unchecked" })
	private void addViews() {
		listview = new ListView("listview", views) {
		    protected void populateItem(final ListItem item) {
		    	//prepare and add view
		    	final SushiEventView viewOptions = (SushiEventView) item.getModelObject();
		    	WebMarkupContainer view = new WebMarkupContainer("view"); 
		    	try {
		    		//build view
		    		EventViewOptions options = new EventViewOptions(viewOptions);
		    		view.add(new Chart("view", options));
		    		view.add(new Label("sub", options.getExplanationString()));
		    	}catch(Exception e) {
		    		e.printStackTrace();
		    		//if chart could not be build, display error message
		    		view.add(new Label("view", "This View could not be built."));
		    		view.add(new Label("sub", "Sorry for the inconvenience"));
		    	}
		        item.add(view);
		        //prepare and add removeButton
		        AjaxButton removeButton = new AjaxButton("removeViewButton") {
					private static final long serialVersionUID = 1L;

					public void onSubmit(AjaxRequestTarget target, Form form) {
						viewOptions.remove();
						views.detach();
						target.add(listview.getParent());
					}
		        };
		        Form<Void> removeform = new Form<Void>("removeform");
		        removeform.add(removeButton);
		        item.add(removeform);
		    }
		};
		listview.setOutputMarkupId(true);
		
		add(listview);
	}
	
}
