package sushi.application.pages.monitoring.bpmn.monitoring;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import sushi.application.components.table.model.AbstractDataProvider;
import sushi.application.pages.monitoring.bpmn.monitoring.modal.ProcessInstanceMonitoringModal;
import sushi.monitoring.bpmn.ProcessInstanceMonitor;

/**
 * This is a button within a form, which shows on an ajax submit of the button the {@link ProcessInstanceMonitoringModal}.
 * @author micha
 */
public class ProcessInstanceMonitorEntryDetailsPanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	private ProcessInstanceMonitoringModal processInstanceMonitorModal;

	/**
	 * Constructor for a form, which contains a button.
	 * The button shows on an ajax submit the {@link ProcessInstanceMonitoringModal}.
	 * @param id
	 * @param entryId
	 * @param dataprovider
	 * @param modal
	 */
	public ProcessInstanceMonitorEntryDetailsPanel(String id, final int entryId, final AbstractDataProvider dataprovider, ProcessInstanceMonitoringModal modal) {
		super(id);
		this.processInstanceMonitorModal = modal;
		Form<Void> form = new Form<Void>("form");
		
		AjaxButton detailsButton = new AjaxButton("detailsButton") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {
				ProcessInstanceMonitor processInstanceMonitor = (ProcessInstanceMonitor) dataprovider.getEntry(entryId);
				processInstanceMonitorModal.setProcessInstanceMonitor(processInstanceMonitor, target);
				processInstanceMonitorModal.show(target);
			}
		};
		
		form.add(detailsButton);
		
		add(form);
	}
}
