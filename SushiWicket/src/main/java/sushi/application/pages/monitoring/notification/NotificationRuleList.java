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
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import freemarker.ext.beans.StringModel;

import sushi.application.SushiAuthenticatedSession;
import sushi.application.components.form.BlockingAjaxButton;
import sushi.application.components.form.DeleteButtonPanel;
import sushi.application.components.form.WarnOnExitForm;
import sushi.application.components.table.SelectEntryPanel;
import sushi.application.components.table.SushiProvider;
import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.eventrepository.eventview.EventViewModal;
import sushi.eventhandling.NotificationObservable;
import sushi.notification.SushiNotificationForEvent;
import sushi.notification.SushiNotificationRule;
import sushi.notification.SushiNotificationRuleForEvent;
import sushi.notification.SushiNotificationRuleForQuery;

/**
 * This class is a List to display @see SushiNotificationRule on the @see NotificationPage
 */
public class NotificationRuleList extends Panel{
	
	private AbstractSushiPage abstractSushiPage;
	private EventViewModal eventViewModal;
	private Form<Void> notificationForm;
	private ArrayList<IColumn<SushiNotificationRule, String>> columns;
	public DefaultDataTable<SushiNotificationRule, String> notificationRuleTable;
	public SushiProvider<SushiNotificationRule> notificationRuleProvider;
	
	public NotificationRuleList(String id, final AbstractSushiPage abstractSushiPage) {
		super(id);
				
		this.abstractSushiPage = abstractSushiPage;
	
	    notificationForm = new Form<Void>("notificationForm");
	    notificationForm.add(addNotificationRules());
	    add(notificationForm); 
	}
		
	@SuppressWarnings({ "unchecked" })
	private Component addNotificationRules() {
		
		notificationRuleProvider = new SushiProvider<SushiNotificationRule>(SushiNotificationRule.findAll());
		
		columns = new ArrayList<IColumn<SushiNotificationRule, String>>();
		columns.add(new PropertyColumn<SushiNotificationRule, String>(Model.of("ID"), "ID"));
		columns.add(new AbstractColumn<SushiNotificationRule, String>(Model.of("Trigger"), "trigger") {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				// notification rule for event
				if (rowModel.getObject() instanceof SushiNotificationRuleForEvent) {
					SushiNotificationRuleForEvent rule = (SushiNotificationRuleForEvent) rowModel.getObject();
					Label label = new Label(componentId, rule.getEventType() + " : " + rule.getCondition().getConditionString());
					cellItem.add(label);
				}
				// notification rule query
				else if (rowModel.getObject() instanceof SushiNotificationRuleForQuery) {
					SushiNotificationRuleForQuery rule = (SushiNotificationRuleForQuery) rowModel.getObject();
					Label label = new Label(componentId, rule.getQuery().toString());
					cellItem.add(label);
				} 
		}});
		columns.add(new PropertyColumn<SushiNotificationRule, String>(Model.of("User"), "user"));
		columns.add(new PropertyColumn<SushiNotificationRule, String>(Model.of("Priority"), "priority"));
		columns.add(new AbstractColumn(new Model("Delete")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				final SushiNotificationRule rule = (SushiNotificationRule) rowModel.getObject();
				 AjaxButton removeButton = new AjaxButton("button") {
						private static final long serialVersionUID = 1L;

						public void onSubmit(AjaxRequestTarget target, Form form) {
							rule.remove();
							if (rule instanceof SushiNotificationRuleForEvent) {
								SushiNotificationRuleForEvent eventRule = (SushiNotificationRuleForEvent) rule;
								NotificationObservable.getInstance().removeNotificationObserver(eventRule);								
							}
							notificationRuleProvider.removeItem(rule);
							notificationRuleTable.detach();
							target.add(notificationRuleTable);
						}
			        };

				WebMarkupContainer buttonPanel = new WebMarkupContainer(componentId);
				try {
					buttonPanel = new DeleteButtonPanel(componentId, removeButton);
				} catch (Exception e) {
					e.printStackTrace();
				}
				buttonPanel.setOutputMarkupId(true);
				cellItem.add(buttonPanel);
			};			
		});

		notificationRuleTable = new DefaultDataTable<SushiNotificationRule, String>("notificationRules", columns, notificationRuleProvider, 20);
		notificationRuleTable.setOutputMarkupId(true);
		
		return notificationRuleTable;
	}
}

