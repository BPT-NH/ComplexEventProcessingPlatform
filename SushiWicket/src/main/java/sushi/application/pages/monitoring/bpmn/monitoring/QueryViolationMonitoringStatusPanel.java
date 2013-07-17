package sushi.application.pages.monitoring.bpmn.monitoring;

import java.util.Set;

import org.apache.wicket.markup.html.panel.Panel;

import sushi.application.components.form.BootStrapLabel;
import sushi.application.components.form.BootStrapTextEmphasisClass;
import sushi.application.components.table.model.AbstractDataProvider;
import sushi.application.pages.monitoring.bpmn.monitoring.model.ProcessInstanceMonitoringTreeTableElement;
import sushi.monitoring.bpmn.ViolationStatus;
import sushi.query.SushiPatternQuery;

/**
 * This panel contains a label for displaying the {@link ViolationStatus} of a {@link SushiPatternQuery}.
 */
public class QueryViolationMonitoringStatusPanel extends Panel {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for a panel, which contains a label for displaying the {@link ViolationStatus} of a {@link SushiPatternQuery}.
	 * @param id
	 * @param entryId
	 * @param dataprovider
	 */
	public QueryViolationMonitoringStatusPanel(String id, final int entryId, final AbstractDataProvider dataprovider) {
		super(id);
		ProcessInstanceMonitoringTreeTableElement treeTableElement = (ProcessInstanceMonitoringTreeTableElement) dataprovider.getEntry(entryId);
		BootStrapTextEmphasisClass textEmphasisClass = BootStrapTextEmphasisClass.Muted;
		Set<ViolationStatus> violationStatus = treeTableElement.getProcessInstanceMonitor().getViolationStatusForQuery(treeTableElement.getQuery());
		
		if(violationStatus != null){
			if(violationStatus.isEmpty()){
				textEmphasisClass = BootStrapTextEmphasisClass.Success;
				add(new BootStrapLabel("label", "No violations", textEmphasisClass));
			} else {
				textEmphasisClass = BootStrapTextEmphasisClass.Error;
				add(new BootStrapLabel("label", violationStatus.toString(), textEmphasisClass));
			}
		} else {
			add(new BootStrapLabel("label", "", textEmphasisClass));
		}
	}
	

	

}
