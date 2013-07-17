package sushi.application.pages.monitoring.notification;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import sushi.application.SushiAuthenticatedSession;
import sushi.application.pages.AbstractSushiPage;
import sushi.notification.SushiNotification;
import sushi.notification.SushiNotificationForEvent;
import sushi.notification.SushiNotificationRule;
import sushi.notification.SushiNotificationRuleForEvent;

import sushi.user.SushiUser;

/**
 * This page displays notifications for logged in users, existing notification rules and
 * allows to create new notification rules via a @see AddNotificationModal
 */
public class NotificationPage extends AbstractSushiPage {
	
	private static final long serialVersionUID = 1L;
	private ListView notificationListView;
	private AjaxButton addButton;
	private Form<Void> form;
	public AddNotificationModal addNotificationModal;

	public NotificationRuleList notificationRulesListview;
	    
	public NotificationPage() {
		super();
		
		// Create the modal window.
	    addNotificationModal = new AddNotificationModal("addNotificationModal", this);
        add(addNotificationModal);
	    
        //add notificationList
        if (((SushiAuthenticatedSession)Session.get()).getUser() != null) {
        	//logged in users see their notifications
	        NotificationList notificationList= new NotificationList("notificationList", this);
	        notificationList.setOutputMarkupId(true);
	        add(notificationList);
        } else {
        	Label notificationListLabel = new Label("notificationList", "Log in to check your notifications.");
        	notificationListLabel.setOutputMarkupId(true);
        	add(notificationListLabel);
        }
        
        form = new Form<Void>("form");
		form.add(addAddButton());
	    add(form);
	    
	    addNotificationRules();
	}
	
	
	IModel<List<SushiNotificationRule>> notificationRules = new LoadableDetachableModel<List<SushiNotificationRule>>() {
        @Override
        protected List<SushiNotificationRule> load() {
            return SushiNotificationRule.findAll();
        }
    };

	IModel<List<SushiNotification>> notifications = new LoadableDetachableModel<List<SushiNotification>>() {
        @Override
        protected List<SushiNotification> load() {
        	//get User
        	SushiUser user = ((SushiAuthenticatedSession)Session.get()).getUser();
        	if (user == null) return new ArrayList<SushiNotification>();
        	//get Notifications
            List<SushiNotification> notifications = SushiNotification.findUnseenForUser(user);
            if (notifications == null) return new ArrayList<SushiNotification>();
            return notifications;
        }
    };
	
	private Component addAddButton() {
		addButton = new AjaxButton("addNotificationButton") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				addNotificationModal.show(target);
			}
		};
		return addButton;
	}
	
	@SuppressWarnings({ "unchecked" })
	private void addNotificationRules() {
		notificationRulesListview = new NotificationRuleList("notificationRuleList", this);
		notificationRulesListview.setOutputMarkupId(true);		
		add(notificationRulesListview);
	}
}
