package sushi.application.pages.monitoring.bpmn.monitoring;

import org.apache.wicket.markup.html.panel.Panel;

import sushi.application.components.form.BootStrapLabel;
import sushi.application.components.form.BootStrapTextEmphasisClass;
import sushi.application.components.table.model.AbstractDataProvider;
import sushi.monitoring.bpmn.ProcessInstanceMonitor;

/**
 * This panel has a label for displaying text.
 */
public class ProcessInstanceMonitoringStatusPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public ProcessInstanceMonitoringStatusPanel(String id, final int entryId, final AbstractDataProvider dataprovider) {
		super(id);
		ProcessInstanceMonitor processInstanceMonitor = (ProcessInstanceMonitor) dataprovider.getEntry(entryId);
		BootStrapTextEmphasisClass textEmphasisClass = BootStrapTextEmphasisClass.Muted;
		
		switch(processInstanceMonitor.getStatus()){
		case Aborted:
			textEmphasisClass = BootStrapTextEmphasisClass.Error;
			break;
		case Finished:
			textEmphasisClass = BootStrapTextEmphasisClass.Success;
			break;
		case NotExisting:
			textEmphasisClass = BootStrapTextEmphasisClass.Muted;
			break;
		case Running:
			textEmphasisClass = BootStrapTextEmphasisClass.Info;
			break;
		default:
			break;
		
		}
		add(new BootStrapLabel("label", processInstanceMonitor.getStatus().name(), textEmphasisClass));
	}
	

	

}
