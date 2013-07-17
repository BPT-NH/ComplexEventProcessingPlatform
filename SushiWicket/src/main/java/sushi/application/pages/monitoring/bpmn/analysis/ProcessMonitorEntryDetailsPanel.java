package sushi.application.pages.monitoring.bpmn.analysis;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import sushi.application.components.table.model.AbstractDataProvider;
import sushi.application.pages.monitoring.bpmn.analysis.modal.ProcessAnalysingModal;
import sushi.monitoring.bpmn.ProcessMonitor;

/**
 * This is a button within a form, which shows on an ajax submit of the button the {@link ProcessAnalysingModal}.
 * @author micha
 */
public class ProcessMonitorEntryDetailsPanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	private ProcessAnalysingModal processMonitorModal;

	/**
	 * Constructor for a form, which contains a button.
	 * The button shows on an ajax submit the {@link ProcessAnalysingModal}.
	 * @param id
	 * @param entryId
	 * @param dataprovider
	 * @param modal
	 */
	public ProcessMonitorEntryDetailsPanel(String id, final int entryId, final AbstractDataProvider dataprovider, ProcessAnalysingModal modal) {
		super(id);
		this.processMonitorModal = modal;
		Form<Void> form = new Form<Void>("form");
		
		AjaxButton detailsButton = new AjaxButton("detailsButton") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				ProcessMonitor processMonitor = (ProcessMonitor) dataprovider.getEntry(entryId);
				processMonitorModal.setProcessMonitor(processMonitor, target);
				processMonitorModal.show(target);
			}
		};
		
		form.add(detailsButton);
		
		add(form);
	}
}
