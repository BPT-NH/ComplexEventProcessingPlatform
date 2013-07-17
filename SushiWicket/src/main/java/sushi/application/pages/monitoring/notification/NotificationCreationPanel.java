package sushi.application.pages.monitoring.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
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
import sushi.eventhandling.Broker;
import sushi.eventhandling.NotificationObservable;
import sushi.notification.SushiNotificationPriorityEnum;
import sushi.notification.SushiNotificationRule;
import sushi.notification.SushiNotificationRuleForEvent;
import sushi.notification.SushiNotificationRuleForQuery;
import sushi.query.SushiQuery;
import sushi.user.SushiUser;
import sushi.visualisation.SushiTimePeriodEnum;
import de.agilecoders.wicket.markup.html.bootstrap.common.NotificationPanel;

/**
 * This panel creates a @see SushiNotificationRule.
 * This can either be a @see SushiNotificationRuleForEvent or a @see SushiNotificationRuleForQuery
 */
public class NotificationCreationPanel extends Panel{
	
	private static final long serialVersionUID = 1L;
	
	List<SushiAttribute> attributes = new ArrayList<SushiAttribute>();
	private NotificationPage visualisationPage;
	private NotificationCreationPanel panel;
	
	private NotificationPanel feedbackPanel;
	private Form<Void> layoutForm;

	private DropDownChoice<String> eventTypeSelect;
	private EventTypeNamesProvider eventTypeNameProvider = new EventTypeNamesProvider();
	private FlexConditionInputPanel conditionInput;
	private DropDownChoice<SushiUser> userSelect;
	private DropDownChoice<String> typeSelect;
	private DropDownChoice<SushiNotificationPriorityEnum> prioritySelect;
	private WebMarkupContainer eventTypeContainer; 
	private WebMarkupContainer queryContainer;
	private DropDownChoice<SushiQuery> querySelect;
	
	private String selectedEventTypeName = null;
	private SushiEventType selectedEventType = null; 
	private SushiUser selectedUser = null;
	private SushiQuery selectedQuery = null;
	private static final List<String> TYPES = Arrays.asList("Event Type", "Query");
	private IModel<String> selectedType = Model.of("Event Type"); 
	private static final List<SushiNotificationPriorityEnum> PRIORITIES = Arrays.asList(SushiNotificationPriorityEnum.values());
	private IModel<SushiNotificationPriorityEnum> selectedPriority = Model.of(SushiNotificationPriorityEnum.LOW);

	public NotificationCreationPanel(String id, NotificationPage notificationPage){
		super(id);
		this.panel = this;
		
		this.visualisationPage = notificationPage;
		
		layoutForm = new Form<Void>("layoutForm");
		add(layoutForm);
		addFeedbackPanel(layoutForm);
		layoutForm.add(addTypeSelect());
		layoutForm.add(addPrioritySelect());
		layoutForm.add(addUserSelect());
		layoutForm.add(addEventDiv());
		layoutForm.add(addQueryDiv());
		addButtonsToForm(layoutForm);
	}

	private Component addTypeSelect() {
		 typeSelect = new DropDownChoice<String>("typeSelect", selectedType, TYPES);
		 typeSelect.add(new AjaxFormComponentUpdatingBehavior("onchange"){ 
				@Override 
				protected void onUpdate(AjaxRequestTarget target) {
					target.add(eventTypeContainer);
					target.add(queryContainer);
					target.add(eventTypeSelect);
					target.add(conditionInput);
				}
		 });
		 typeSelect.setOutputMarkupId(true);
		 return typeSelect;
	}

	private boolean isQueryDivVisible() {
		return selectedType.getObject().equals("Query");
	};
	
	private boolean isEventDivVisible() {
		return !isQueryDivVisible();
	};
	
	private Component addQueryDiv() {
		queryContainer = new WebMarkupContainer("QueryDiv") {
			public boolean isVisible() {
		        return isQueryDivVisible();
			}
		};
		queryContainer.setOutputMarkupPlaceholderTag(true);
		queryContainer.add(addQuerySelect());
		return queryContainer;
	}

	private Component addPrioritySelect() {
		 prioritySelect = new DropDownChoice<SushiNotificationPriorityEnum>("prioritySelect", selectedPriority, PRIORITIES);
		 prioritySelect.setOutputMarkupId(true);
		 return prioritySelect;
	}

	private Component addEventDiv() {
		eventTypeContainer = new WebMarkupContainer("EventDiv") {
			public boolean isVisible() {
		        return isEventDivVisible();
			}
		};
		eventTypeContainer.setOutputMarkupPlaceholderTag(true);
		eventTypeContainer.add(addEventTypeSelect());
		eventTypeContainer.add(addConditionInput());
		return eventTypeContainer;
	}
	
	private Component addEventTypeSelect() {
		eventTypeSelect = new DropDownChoice<String>("eventTypeSelect", new PropertyModel<String>(this, "selectedEventTypeName"), eventTypeNameProvider) {
			public boolean isVisible() {
		        return isEventDivVisible();
			}
		};
		eventTypeSelect.setOutputMarkupPlaceholderTag(true);
		
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

	private Component addConditionInput() {
		conditionInput = new FlexConditionInputPanel("conditionInput") {
			public boolean isVisible() {
		        return isEventDivVisible();
			}
		};
		conditionInput.setOutputMarkupPlaceholderTag(true);
		return conditionInput;
	}
	
	private Component addQuerySelect() {
		querySelect = new DropDownChoice<SushiQuery>("querySelect", new PropertyModel<SushiQuery>(this, "selectedQuery"), SushiQuery.getAllLiveQueries()) {
			public boolean isVisible() {
		        return isQueryDivVisible();
			}
		};
		querySelect.setOutputMarkupPlaceholderTag(true);
		return querySelect;
	}
	
	
	private Component addUserSelect() {
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
				if (selectedType.getObject().equals("Event Type") && selectedEventType == null) {
					panel.getFeedbackPanel().error("Choose an Event Type!");
					panel.getFeedbackPanel().setVisible(true);
					target.add(panel.getFeedbackPanel());
					error = true;
				};
				if (selectedType.getObject().equals("Query") && selectedQuery == null) {
					panel.getFeedbackPanel().error("Choose a Query!");
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
					//create and save notificationRule
					SushiNotificationRule notification;
					if (selectedType.getObject().equals("Event Type")) {
						//create event notificationRule
						notification = new SushiNotificationRuleForEvent(selectedEventType, conditionInput.getCondition(), selectedUser, selectedPriority.getObject());
						Broker.send(notification);
					} else {
						//create query notificationRule
						notification = new SushiNotificationRuleForQuery(selectedQuery, selectedUser, selectedPriority.getObject());
						notification.save();
					}

					//update notificationRuleList in notificationpage
					NotificationPage visualisation = (NotificationPage) visualisationPage;
					visualisation.notificationRulesListview.notificationRuleProvider.addItem(notification);
					visualisation.notificationRulesListview.notificationRuleTable.detach();
					target.add(visualisation.notificationRulesListview.notificationRuleTable);
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
