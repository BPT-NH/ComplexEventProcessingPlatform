package sushi.application.pages.correlation;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import sushi.application.components.form.BlockingAjaxButton;
import sushi.correlation.Correlator;
import sushi.process.SushiProcess;
import de.agilecoders.wicket.markup.html.bootstrap.button.ButtonType;
import de.agilecoders.wicket.markup.html.bootstrap.button.TypedAjaxButton;
import de.agilecoders.wicket.markup.html.bootstrap.dialog.Alert;

class ExistingCorrelationAlert extends Alert {
	
	private ExistingCorrelationAlert alert;
	private CorrelationPage correlationPage;
	private SushiProcess selectedProcess;
	
	/**
	 * Constructor.
	 * 
	 * @param id
	 *            the wicket component id.
	 * @param correlationPage 
	 */
	public ExistingCorrelationAlert(String id, String message, CorrelationPage correlationPage) {
		super(id, Model.of(message), Model.of(""));
		this.type(Alert.Type.Warning);
		alert = this;
		this.correlationPage = correlationPage;
	}

	/**
	 * creates a new message component.
	 * 
	 * @param markupId
	 *            The component id
	 * @param message
	 *            The message as {@link IModel}
	 * @return new message component
	 */
	protected Component createMessage(final String markupId, final IModel<String> message) {
		final Form container = new Form(markupId);
		
		container.add(new Label("messageText", new Model<Serializable>("Correlation exists! Do you want to override it?")));
		BlockingAjaxButton correlateButton = new BlockingAjaxButton("correlateButton", new Model("Correlate")) {
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				Correlator.removeExistingCorrelation(selectedProcess);
				correlationPage.correlateEvents(selectedProcess);
				
				alert.setVisible(false);
				
				target.add(correlationPage.getFeedbackPanel());
				target.add(alert);
	        }
		};
		
		
		container.add(correlateButton);
		TypedAjaxButton abortButton = new TypedAjaxButton("abortButton", new Model("Abort"), ButtonType.Primary) {
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				correlationPage.getAlert().setVisible(false);
				target.add(correlationPage);
	        }
		};
		container.add(abortButton);
		return container;
	}

	public void setSelectedProcess(SushiProcess selectedProcess) {
		this.selectedProcess = selectedProcess;
	}
}
