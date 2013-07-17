package sushi.application.pages.monitoring.notification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import sushi.application.SushiAuthenticatedSession;
import sushi.application.components.form.BlockingAjaxButton;
import sushi.application.components.form.WarnOnExitForm;
import sushi.application.components.table.SelectEntryPanel;
import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.eventrepository.eventview.EventViewModal;
import sushi.notification.SushiNotification;
import sushi.notification.SushiNotificationForEvent;

/**
 * List to display notifications on the @see NotificationPage
 */
@SuppressWarnings("serial")
public class NotificationList extends Panel{
	
	private AbstractSushiPage abstractSushiPage;
	private EventViewModal eventViewModal;
	private Form<Void> notificationForm;
	private ArrayList<IColumn<SushiNotification, String>> columns;
	public DefaultDataTable<SushiNotification, String> notificationTable;
	private NotificationProvider notificationProvider;
	
	public NotificationList(String id, final AbstractSushiPage abstractSushiPage) {
		super(id);
				
		this.abstractSushiPage = abstractSushiPage;

		//add modal
		eventViewModal = new EventViewModal("eventViewModal");
		eventViewModal.setOutputMarkupId(true);
		add(eventViewModal);

		add(addFilterComponents());
	
	    notificationForm = new Form<Void>("notificationForm");
	    notificationForm.add(addNotifications());
	    add(notificationForm); 

	}
	
	private Component addFilterComponents() {
		Form<Void> buttonForm = new WarnOnExitForm("buttonForm");
		
		List<String> notificationFilterCriteriaList = new ArrayList<String>(Arrays.asList(new String[] {"ID", "Timestamp", "NotificationRule (ID)"}));
		String selectedNotificationCriteria = "ID";

		final DropDownChoice<String> notificationFilterCriteriaSelect = new DropDownChoice<String>("notificationFilterCriteria", new Model<String>(selectedNotificationCriteria), notificationFilterCriteriaList);
		buttonForm.add(notificationFilterCriteriaSelect);
		
		List<String> conditions = new ArrayList<String>(Arrays.asList(new String[] { "<", "=", ">" }));
		String selectedCondition = "=";

		final DropDownChoice<String> eventFilterConditionSelect = new DropDownChoice<String>("notificationFilterCondition", new Model<String>(selectedCondition), conditions);
		buttonForm.add(eventFilterConditionSelect);
		
		final TextField<String> searchValueInput = new TextField<String>("searchValueInput", Model.of(""));
		buttonForm.add(searchValueInput);
		
		AjaxButton filterButton = new AjaxButton("filterButton", buttonForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				String notificationFilterCriteria = notificationFilterCriteriaSelect.getChoices().get(Integer.parseInt(notificationFilterCriteriaSelect.getValue()));
				String notificationFilterCondition = eventFilterConditionSelect.getChoices().get(Integer.parseInt(eventFilterConditionSelect.getValue()));
				String filterValue = searchValueInput.getValue();
				notificationProvider.setNotificationFilter(new NotificationFilter(notificationFilterCriteria, notificationFilterCondition, filterValue));
				target.add(notificationTable);
	        	}
	    };
	    buttonForm.add(filterButton);
	    
	    AjaxButton resetButton = new BlockingAjaxButton("resetButton", buttonForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);;
				notificationProvider.setNotificationFilter(new NotificationFilter());
				target.add(notificationTable);
	        }
	    };
	    buttonForm.add(resetButton);
		
	    AjaxButton markSeenButton = new BlockingAjaxButton("seenButton", buttonForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);;
				notificationProvider.markSeenSelectedEntries();
				notificationProvider.clearSelectedEntities();
				notificationProvider.update();
				target.add(notificationTable);
			}
	    };
	    buttonForm.add(markSeenButton);
	    
	    AjaxButton selectAllButton = new AjaxButton("selectAllButton", buttonForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				notificationProvider.selectAllEntries();
				target.add(notificationTable);
	        }
	    };
	    buttonForm.add(selectAllButton);
	    
	    return buttonForm;
	}
	
	/**
	 * prepares list of notifications
	 * @return list of notifications
	 */
	@SuppressWarnings({ "unchecked" })
	private Component addNotifications() {
		
		//collect all notifications for logged in user
		notificationProvider = new NotificationProvider(((SushiAuthenticatedSession)Session.get()).getUser());
		
		columns = new ArrayList<IColumn<SushiNotification, String>>();
		columns.add(new PropertyColumn<SushiNotification, String>(Model.of("ID"), "ID"));
		columns.add(new PropertyColumn<SushiNotification, String>(Model.of("Timestamp"), "timestamp"));
		columns.add(new PropertyColumn<SushiNotification, String>(Model.of("Notification Rule"), "notificationRule"));
		columns.add(new AbstractColumn<SushiNotification, String>(Model.of("Trigger"), "trigger") {
			@Override
			public void populateItem(final Item<ICellPopulator<SushiNotification>> item, final String componentId,
				final IModel<SushiNotification> rowModel)
			{
				String shortenedValues = ((SushiNotification) rowModel.getObject()).getTriggeringText();
				if  (shortenedValues.length() > 200) {
					shortenedValues = shortenedValues.substring(0, 200) + "...";
				}
				Label label = new Label(componentId, shortenedValues);
				
				//for events add event view modal
				if (rowModel.getObject() instanceof SushiNotificationForEvent) {
					final SushiNotificationForEvent notification = (SushiNotificationForEvent) rowModel.getObject();
					label.add(new AjaxEventBehavior("onclick") {
			             protected void onEvent(AjaxRequestTarget target) {
			                 //on click open Event View Modal
			            	 eventViewModal.getPanel().setEvent(notification.getEvent());
			            	 eventViewModal.getPanel().detach();
			            	 target.add(eventViewModal.getPanel());
			            	 eventViewModal.show(target);
			             }
			         });
				}
				item.add(label);
			}
		}
		);
		columns.add(new AbstractColumn(new Model("Select")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				int entryId = ((SushiNotification) rowModel.getObject()).getID();
				cellItem.add(new SelectEntryPanel(componentId, entryId, notificationProvider));
			};			
		});

		notificationTable = new DefaultDataTable<SushiNotification, String>("notifications", columns, notificationProvider, 20);
		notificationTable.setOutputMarkupId(true);
				
		return notificationTable;
	}
}

