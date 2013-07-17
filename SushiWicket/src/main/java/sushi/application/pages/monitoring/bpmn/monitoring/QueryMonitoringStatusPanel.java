package sushi.application.pages.monitoring.bpmn.monitoring;

import org.apache.wicket.markup.html.panel.Panel;

import sushi.application.components.form.BootStrapLabel;
import sushi.application.components.form.BootStrapTextEmphasisClass;
import sushi.application.components.table.model.AbstractDataProvider;
import sushi.application.pages.monitoring.bpmn.monitoring.model.ProcessInstanceMonitoringTreeTableElement;
import sushi.monitoring.bpmn.QueryStatus;
import sushi.query.SushiPatternQuery;

/**
 * This panel contains a label for displaying the {@link QueryStatus} of a {@link SushiPatternQuery}.
 */
public class QueryMonitoringStatusPanel extends Panel {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for a panel, which contains a label for displaying the {@link QueryStatus} of a {@link SushiPatternQuery}.
	 * @param id
	 * @param entryId
	 * @param dataprovider
	 */
	public QueryMonitoringStatusPanel(String id, final int entryId, final AbstractDataProvider dataprovider) {
		super(id);
		ProcessInstanceMonitoringTreeTableElement treeTableElement = (ProcessInstanceMonitoringTreeTableElement) dataprovider.getEntry(entryId);
		BootStrapTextEmphasisClass textEmphasisClass = BootStrapTextEmphasisClass.Muted;
		QueryStatus queryStatus = treeTableElement.getProcessInstanceMonitor().getStatusForQuery(treeTableElement.getQuery());
		
		switch(queryStatus){
		case Finished:
			textEmphasisClass = BootStrapTextEmphasisClass.Success;
			break;
		case NotExisting:
			textEmphasisClass = BootStrapTextEmphasisClass.Muted;
			break;
		case Skipped:
			textEmphasisClass = BootStrapTextEmphasisClass.Error;
			break;
		case Started:
			textEmphasisClass = BootStrapTextEmphasisClass.Info;
			break;
		default:
			break;
		}
		add(new BootStrapLabel("label", queryStatus.getTextValue(), textEmphasisClass));
	}
	

	

}
