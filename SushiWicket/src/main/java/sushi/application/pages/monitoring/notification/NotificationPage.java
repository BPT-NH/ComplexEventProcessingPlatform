package sushi.application.pages.monitoring.notification;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import sushi.application.SushiAuthenticatedSession;
import sushi.application.pages.AbstractSushiPage;
import sushi.notification.SushiNotification;
import sushi.notification.SushiNotificationRule;
import sushi.user.SushiUser;

public class NotificationPage extends AbstractSushiPage {
	
	private static final long serialVersionUID = 1L;
	private ListView notificationListView;
	private AjaxButton addButton;
	private Form<Void> form;
	public ListView listview;
	public AddNotificationModal addNotificationModal;

	public NotificationPage() {
		super();
		
		// Create the modal window.
	    addNotificationModal = new AddNotificationModal("addNotificationModal", this);
        add(addNotificationModal);
	    
        addNotifications();
        
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
        	//getUser
        	SushiUser user = ((SushiAuthenticatedSession)Session.get()).getUser();
        	if (user == null) return new ArrayList<SushiNotification>();
            List<SushiNotification> notifies = SushiNotification.findUnseenForUser(user);
            if (notifies == null) return new ArrayList<SushiNotification>();
            return notifies;
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
	private void addNotifications() {
		notificationListView = new ListView("notifications", notifications) {
		    protected void populateItem(final ListItem item) {
		        Form<Void> notificationform = new Form<Void>("form");
		    	//prepare and add notification
		    	final SushiNotification notification = (SushiNotification) item.getModelObject();
		    	notificationform.add(new Label("notificationEvent", notification.toString()));
		        //prepare and add removeButton
		        AjaxButton removeButton = new AjaxButton("markSeenButton") {
					private static final long serialVersionUID = 1L;

					public void onSubmit(AjaxRequestTarget target, Form form) {
						notification.setSeen();
						notifications.detach();
						target.add(notificationListView.getParent());
					}
		        };
		        notificationform.add(removeButton);
		        item.add(notificationform);
		    }
		};
		notificationListView.setOutputMarkupId(true);
		
		add(notificationListView);
	}

	
	@SuppressWarnings({ "unchecked" })
	private void addNotificationRules() {
		listview = new ListView("listview", notificationRules) {
		    protected void populateItem(final ListItem item) {
		        Form<Void> notificationform = new Form<Void>("form");
		    	//prepare and add notification
		    	final SushiNotificationRule notification = (SushiNotificationRule) item.getModelObject();
		    	notificationform.add(new Label("notification", notification.toString()));
		        //prepare and add removeButton
		        AjaxButton removeButton = new AjaxButton("removeNotificationButton") {
					private static final long serialVersionUID = 1L;

					public void onSubmit(AjaxRequestTarget target, Form form) {
						notification.remove();
						notificationRules.detach();
						target.add(listview.getParent());
					}
		        };
		        notificationform.add(removeButton);
		        item.add(notificationform);
		    }
		};
		listview.setOutputMarkupId(true);
		
		add(listview);
	}
}
