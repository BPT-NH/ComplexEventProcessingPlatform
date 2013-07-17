package sushi.application.pages.user;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

import sushi.application.components.form.WarnOnExitForm;
import sushi.application.pages.AbstractSushiPage;
import sushi.user.UserProvider;

/**
 * A page to render a register form to register new users.
 * @author micha
 */
public class RegisterPage extends AbstractSushiPage {

	private static final long serialVersionUID = -7896431319431474548L;
	private Form<Void> layoutForm;
	private TextField<String> mailInput;
	private TextField<String> nameInput;
	private PasswordTextField passwordInput;
	private PasswordTextField repeatPasswordInput;

	/**
	 * Constructor for a page to render a register form to register new users.
	 */
	public RegisterPage() {
		super();
		buildMainLayout();
	}
	
	private void buildMainLayout() {
		layoutForm = new WarnOnExitForm("layoutForm");
		
		mailInput = new TextField<String>("mailInput", Model.of(""));
		layoutForm.add(mailInput);
		
		nameInput = new TextField<String>("nameInput", Model.of(""));
		layoutForm.add(nameInput);
		
		passwordInput = new PasswordTextField("passwordInput", Model.of(""));
		layoutForm.add(passwordInput);
		
		repeatPasswordInput = new PasswordTextField("repeatPasswordInput", Model.of(""));
		layoutForm.add(repeatPasswordInput);
		
		addRegisterButton();
		
		add(layoutForm);
	}

	private void addRegisterButton() {
		Button applyButton = new Button("registerButton") {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit() {
				super.onSubmit();
				String userName = nameInput.getValue();
				String mail = mailInput.getValue();
				String password = passwordInput.getValue();
				String repeatPassword = repeatPasswordInput.getValue();
				if(mail.isEmpty()){
					getFeedbackPanel().error("Please provide a mail adress.");
					return;
				}
				if(password.isEmpty()){
					getFeedbackPanel().error("Please provide a password.");
					return;
				}
				if(repeatPassword.isEmpty()){
					getFeedbackPanel().error("Please provide the repeated password.");
					return;
				}
				if(userName.isEmpty()){
					getFeedbackPanel().error("Please provide a user name.");
					return;
				}
				if(!password.equals(repeatPassword)){
					getFeedbackPanel().error("Password and repeated password are not equal.");
					return;
				}
				if(UserProvider.isNameAlreadyInUse(userName)){
					getFeedbackPanel().error("User name is already taken.");
					return;
				}
				UserProvider.createUser(userName, repeatPassword, mail);
				getFeedbackPanel().success("User " + userName + " was created!");
			}
	    };
	    layoutForm.add(applyButton);
	}

}
