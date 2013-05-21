package sushi.application.pages.user;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

import sushi.application.SushiAuthenticatedSession;
import sushi.application.components.form.BlockingAjaxButton;
import sushi.application.components.form.WarnOnExitForm;
import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.main.MainPage;

public class LoginPage extends AbstractSushiPage {

	private static final long serialVersionUID = -7896431319431474548L;
	private Form<Void> loginForm;
	private TextField<String> nameInput;
	private PasswordTextField passwordInput;
	private Form<?> logoutForm;

	public LoginPage() {
		super();
		buildMainLayout();
	}
	
	private void buildMainLayout() {
		loginForm = new WarnOnExitForm("loginForm");
		
		nameInput = new TextField<String>("nameInput", Model.of(""));
		loginForm.add(nameInput);
		
		passwordInput = new PasswordTextField("passwordInput", Model.of(""));
		loginForm.add(passwordInput);
		
		addLoginButton();
		addRegisterLink();
		
		add(loginForm);
		
		logoutForm = new Form<Object>("logoutForm");
		
		addLogoutButton();
		
		add(logoutForm);
		
		if(((SushiAuthenticatedSession)Session.get()).getUser() != null){
			loginForm.setVisible(false);
		} else {
			logoutForm.setVisible(false);
		}
	}

	private void addRegisterLink() {
		loginForm.add(new Link<Object>("registerLink"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(RegisterPage.class);
			};
		});
		
	}

	private void addLoginButton() {
		Button applyButton = new BlockingAjaxButton("loginButton", loginForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				String userName = nameInput.getValue();
				String password = passwordInput.getValue();
				if(login(userName, password)){
					setResponsePage(LoginPage.class);
				}
			}

	    };
	    loginForm.add(applyButton);
	}
	
	private void addLogoutButton() {
		Button applyButton = new BlockingAjaxButton("logoutButton", logoutForm) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				getSession().invalidate();
				setResponsePage(MainPage.class);
			}

	    };
	    logoutForm.add(applyButton);
	}

	private boolean login(String userName, String password){
		
		SushiAuthenticatedSession session = getMySession();
        // Sign the user in
        if(session.signIn(userName, password)){
        	continueToOriginalDestination();
        	return true;
        } else {
            // Register the error message with the feedback panel
            getFeedbackPanel().error("Unable to log you in");
            return false;
        }
	}
	
	private SushiAuthenticatedSession getMySession(){
        return (SushiAuthenticatedSession) getSession();
    }
	
}
