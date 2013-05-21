package sushi.application.pages.adapter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;

import sushi.application.components.form.ExternalPage;
import sushi.application.pages.AbstractSushiPage;

public class AdapterPage extends AbstractSushiPage {
	
	private static final long serialVersionUID = 1L;
	private ExternalPage externalPage;

	public AdapterPage() {
		super();
		
		buildMainLayout();
	}

	private void buildMainLayout() {
		Form<Void> layoutForm = new Form<Void>("layoutForm");
		
		AjaxButton handelsblattButton = new AjaxButton("handelsblattButton", layoutForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				externalPage.setURL("http://www.handelsblatt.de");
				
				target.add(externalPage);
			}
	    };
	    layoutForm.add(handelsblattButton);
	    
	    AjaxButton zeitButton = new AjaxButton("zeitButton", layoutForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				externalPage.setURL("http://www.zeit.de");
				
				target.add(externalPage);
			}
	    };
	    layoutForm.add(zeitButton);
		
		add(layoutForm);
		
		externalPage = new ExternalPage("iframe", "http://www.handelsblatt.de");
		externalPage.setOutputMarkupId(true);
		
		add(externalPage);
	}

}
