package sushi.application.pages.monitoring.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import sushi.application.components.form.FlexConditionInputPanel;
import sushi.application.pages.eventrepository.eventtypeeditor.model.EventTypeNamesProvider;
import sushi.event.SushiEventType;
import sushi.event.attribute.SushiAttribute;
import sushi.eventhandling.NotificationObservable;
import sushi.notification.SushiNotificationPriorityEnum;
import sushi.notification.SushiNotificationRule;
import sushi.user.SushiUser;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;

public class NotificationCreationPanel extends Panel{
	
	private static final long serialVersionUID = 1L;

	private static final List<SushiNotificationPriorityEnum> PRIORITIES = Arrays.asList(SushiNotificationPriorityEnum.values());
	
	List<SushiAttribute> attributes = new ArrayList<SushiAttribute>();
	private NotificationPage visualisationPage;
	private NotificationCreationPanel panel;
	
	private NotificationPanel feedbackPanel;
	private Form<Void> layoutForm;

	private DropDownChoice<String> eventTypeSelect;
	private EventTypeNamesProvider eventTypeNameProvider = new EventTypeNamesProvider();
	private FlexConditionInputPanel conditionInput;
	private DropDownChoice<SushiUser> userSelect;
	
	private String selectedEventTypeName = null;
	private SushiEventType selectedEventType = null; 
	private SushiUser selectedUser = null;

	private RadioChoice<SushiNotificationPriorityEnum> priorityGroup;
	
	private IModel<SushiNotificationPriorityEnum> selectedPriority = Model.of(SushiNotificationPriorityEnum.LOW); 
	
	public NotificationCreationPanel(String id, NotificationPage notificationPage){
		super(id);
		this.panel = this;
		
		this.visualisationPage = notificationPage;
		
		layoutForm = new Form<Void>("layoutForm");
		add(layoutForm);
		addFeedbackPanel(layoutForm);
		layoutForm.add(addEventTypeSelect());
		layoutForm.add(addConditionInput());
		layoutForm.add(addPriorityGroup());
		layoutForm.add(addEmailInput());
		
		addButtonsToForm(layoutForm);
	}
	
	private Component addPriorityGroup() {
		 priorityGroup = new RadioChoice<SushiNotificationPriorityEnum>("priorityGroup", selectedPriority, PRIORITIES);
		 priorityGroup.setSuffix("");
		 priorityGroup.setPrefix("");
		 return priorityGroup;
	}

	private Component addConditionInput() {
		conditionInput = new FlexConditionInputPanel("conditionInput");
		conditionInput.setOutputMarkupId(true);
		return conditionInput;
	}

	private Component addEventTypeSelect() {
		eventTypeSelect = new DropDownChoice<String>("eventTypeSelect", new PropertyModel<String>(this, "selectedEventTypeName"), eventTypeNameProvider);
		eventTypeSelect.setOutputMarkupId(true);
		
		eventTypeSelect.add(new AjaxFormComponentUpdatingBehavior("onchange"){ 

			@Override 
			protected void onUpdate(AjaxRequestTarget target) {
				selectedEventType = SushiEventType.findByTypeName(selectedEventTypeName);
				conditionInput.clearSelectedEventType();
				conditionInput.addSelectedEventType(selectedEventType);
				target.add(conditionInput.getConditionAttributeSelect());
				target.add(conditionInput.getConditionValueSelect());
			}
		});
		return eventTypeSelect;
	}
	
	private Component addEmailInput() {
		userSelect = new DropDownChoice<SushiUser>("userSelect", new PropertyModel<SushiUser>(this, "selectedUser"), SushiUser.findAll());
		return userSelect;
	}

	private void addFeedbackPanel(Form<Void> layoutForm) {
		feedbackPanel = new NotificationPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		feedbackPanel.setOutputMarkupPlaceholderTag(true);
        layoutForm.add(feedbackPanel);
	}
	
	private void addButtonsToForm(Form<Void> layoutForm) {
		
	    AjaxButton createButton = new AjaxButton("createButton") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				panel.getFeedbackPanel().setVisible(true);
				boolean error = false;
				
				//get User for Email
				
				if (selectedEventType == null) {
					panel.getFeedbackPanel().error("Choose an Event Type!");
					panel.getFeedbackPanel().setVisible(true);
					target.add(panel.getFeedbackPanel());
					error = true;
				};
				if(selectedUser == null){
					panel.getFeedbackPanel().error("Choose a user!"); 
					panel.getFeedbackPanel().setVisible(true);
					target.add(panel.getFeedbackPanel());
					error = true;
				};
				
				if (error == false) {
					//create and save notification
					SushiNotificationRule notification = new SushiNotificationRule(selectedEventType, conditionInput.getCondition(), selectedUser, selectedPriority.getObject());
					notification.save();
					NotificationObservable.getInstance().addNotificationObserver(notification);
					visualisationPage.notificationRules.detach();
					target.add(visualisationPage.listview.getParent());
					//close this Panel
					visualisationPage.addNotificationModal.close(target);
					//TODO: after closing, the page is still disabled/overlayed
				}
			};
	    };
		
			layoutForm.add(createButton);
	}
	
	public NotificationPanel getFeedbackPanel() {
		return feedbackPanel;
	} 

}
