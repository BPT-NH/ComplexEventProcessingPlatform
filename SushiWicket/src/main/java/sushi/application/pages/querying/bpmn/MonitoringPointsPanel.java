package sushi.application.pages.querying.bpmn;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import sushi.application.pages.querying.bpmn.model.BPMNTreeTableElement;
import sushi.bpmn.monitoringpoint.MonitoringPointStateTransition;

/**
 * A panel on which monitoring points can be assigned to BPMN process elements with {@link MonitoringPointField}s.
 * @author micha
 */
public class MonitoringPointsPanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	private BPMNTreeTableElement treeTableElement;

	/**
	 * Constructor for a panel on which monitoring points can be assigned to BPMN process elements with {@link MonitoringPointField}s.
	 * @param id
	 * @param entryId
	 * @param treeTableElement
	 */
	public MonitoringPointsPanel(String id, final int entryId, BPMNTreeTableElement treeTableElement) {
		super(id);
		this.treeTableElement = treeTableElement;
		buildLayout();
	}

	private void buildLayout() {
		Form<Void> monitoringForm = new Form<Void>("monitoringForm");
		
		new MonitoringPointField(treeTableElement).addMonitoringField(monitoringForm, MonitoringPointStateTransition.enable);
		new MonitoringPointField(treeTableElement).addMonitoringField(monitoringForm, MonitoringPointStateTransition.begin);
		new MonitoringPointField(treeTableElement).addMonitoringField(monitoringForm, MonitoringPointStateTransition.terminate);
		new MonitoringPointField(treeTableElement).addMonitoringField(monitoringForm, MonitoringPointStateTransition.skip);
		
		add(monitoringForm);
		
	}

}
