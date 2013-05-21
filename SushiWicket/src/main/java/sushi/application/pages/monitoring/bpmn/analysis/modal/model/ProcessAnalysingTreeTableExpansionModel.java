package sushi.application.pages.monitoring.bpmn.analysis.modal.model;

import java.util.Set;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Session;
import org.apache.wicket.model.AbstractReadOnlyModel;


/**
 * model that wraps the expansion state handler of a tree component
 */
public class ProcessAnalysingTreeTableExpansionModel extends AbstractReadOnlyModel<Set<ProcessAnalysingTreeTableElement>> {

	private static final long serialVersionUID = 1L;
	private static MetaDataKey<ProcessAnalysingTreeTableExpansion> KEY = new MetaDataKey<ProcessAnalysingTreeTableExpansion>(){};
	
	@Override
	public Set<ProcessAnalysingTreeTableElement> getObject() {
		return ProcessAnalysingTreeTableExpansion.get();
	}

	public static ProcessAnalysingTreeTableExpansion get() {
		ProcessAnalysingTreeTableExpansion expansion = Session.get().getMetaData(KEY);
		if (expansion == null) {
			expansion = new ProcessAnalysingTreeTableExpansion();
			Session.get().setMetaData(KEY, expansion);
		}
		return expansion;
	}
}
