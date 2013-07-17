package sushi.application.pages.monitoring.eventviews;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import sushi.event.SushiEventType;
import sushi.notification.SushiNotificationPriorityEnum;
import sushi.user.SushiUser;
import sushi.visualisation.SushiEventView;
import sushi.visualisation.SushiTimePeriodEnum;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;

/**
 * This panel is used to configure and save a new @see SushiEventView Object
 */
public class ViewConfigurationPanel extends Panel{
	
	private static final long serialVersionUID = 1L;
		
	private EventViewPage parentPage;
	private ViewConfigurationPanel panel;	
	private NotificationPanel feedbackPanel;

	private Form<Void> layoutForm;
	private ListMultipleChoice<SushiEventType> eventTypeSelect;
	private DropDownChoice<SushiTimePeriodEnum> timePeriodSelect;
	private DropDownChoice<SushiUser> userSelect; 

	private ArrayList<SushiEventType> selectedEventTypes = new ArrayList<SushiEventType>();	
	private static final List<SushiTimePeriodEnum> TIMEPERIODS = Arrays.asList(SushiTimePeriodEnum.values());
	private IModel<SushiTimePeriodEnum> selectedTimePeriod = Model.of(SushiTimePeriodEnum.INF);
	private SushiUser selectedUser = null;
	
	public ViewConfigurationPanel(String id, EventViewPage visualisationPanel){
		super(id);
		this.panel = this;		
		this.parentPage = visualisationPanel;
		
		layoutForm = new Form<Void>("layoutForm");
		add(layoutForm);
		addFeedbackPanel(layoutForm);
		
		layoutForm.add(addEventTypeSelect());
		layoutForm.add(addTimePeriodSelect());
		layoutForm.add(addUserSelect());
		
		addButtonsToForm(layoutForm);
	}
			
	private void addFeedbackPanel(Form<Void> layoutForm) {
		feedbackPanel = new NotificationPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		feedbackPanel.setOutputMarkupPlaceholderTag(true);
        layoutForm.add(feedbackPanel);
	}
	
	private Component addEventTypeSelect() {
		eventTypeSelect = new ListMultipleChoice<SushiEventType>("eventTypeSelect", new Model(selectedEventTypes), SushiEventType.findAll());
		eventTypeSelect.setOutputMarkupId(true);
		return eventTypeSelect;
	}
	
	private Component addUserSelect() {
		userSelect = new DropDownChoice<SushiUser>("userSelect", new PropertyModel<SushiUser>(this, "selectedUser"), SushiUser.findAll());
		userSelect.setOutputMarkupId(true);
		return userSelect;
	}
	
	private Component addTimePeriodSelect() {
		timePeriodSelect = new DropDownChoice<SushiTimePeriodEnum>("timePeriodSelect", selectedTimePeriod, TIMEPERIODS);
		timePeriodSelect.setOutputMarkupId(true);
		return timePeriodSelect;
	}
	
	private void addButtonsToForm(Form<Void> layoutForm) {
		
	    AjaxButton createButton = new AjaxButton("createButton") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				panel.getFeedbackPanel().setVisible(true);
				boolean error = false;
								
				if(selectedEventTypes.isEmpty()){
					panel.getFeedbackPanel().error("Choose at least one event type!"); 
					panel.getFeedbackPanel().setVisible(true);
					target.add(panel.getFeedbackPanel());
					error = true;
				};

				if(selectedUser == null){
					panel.getFeedbackPanel().error("Choose a user"); 
					panel.getFeedbackPanel().setVisible(true);
					target.add(panel.getFeedbackPanel());
					error = true;
				};
				
				if (error == false) {
					//create new EventView configuration
					SushiEventView view = new SushiEventView(selectedUser, selectedEventTypes, selectedTimePeriod.getObject());
					view.save();
					EventViewPage visualisation = (EventViewPage) parentPage;
					visualisation.views.detach();
					target.add(visualisation.listview.getParent());
					//close this Panel
					visualisation.addViewModal.close(target);
				};
			};
		};
		
		layoutForm.add(createButton);
	}
	
	public NotificationPanel getFeedbackPanel() {
		return feedbackPanel;
	} 

}