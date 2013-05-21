package sushi.application.pages.monitoring.bpmn.monitoring.model;

import java.util.Set;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Session;
import org.apache.wicket.model.AbstractReadOnlyModel;


/**
 * model that wraps the expansion state handler of a tree component
 */
public class ProcessInstanceMonitoringTreeTableExpansionModel extends AbstractReadOnlyModel<Set<ProcessInstanceMonitoringTreeTableElement>> {

	private static final long serialVersionUID = 1L;
	private static MetaDataKey<ProcessInstanceMonitoringTreeTableExpansion> KEY = new MetaDataKey<ProcessInstanceMonitoringTreeTableExpansion>(){};
	
	@Override
	public Set<ProcessInstanceMonitoringTreeTableElement> getObject() {
		return ProcessInstanceMonitoringTreeTableExpansion.get();
	}

	public static ProcessInstanceMonitoringTreeTableExpansion get() {
		ProcessInstanceMonitoringTreeTableExpansion expansion = Session.get().getMetaData(KEY);
		if (expansion == null) {
			expansion = new ProcessInstanceMonitoringTreeTableExpansion();
			Session.get().setMetaData(KEY, expansion);
		}
		return expansion;
	}
}
