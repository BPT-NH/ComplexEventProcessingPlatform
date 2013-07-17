package sushi.application.pages.monitoring.bpmn.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.monitoring.bpmn.analysis.modal.ProcessAnalysingModal;
import sushi.application.pages.monitoring.bpmn.analysis.model.ProcessMonitoringFilter;
import sushi.application.pages.monitoring.bpmn.analysis.model.ProcessMonitoringProvider;
import sushi.monitoring.bpmn.ProcessMonitor;
import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;

/**
 * This panel facilitates the analysis of finished {@link SushiProcessInstance}s.
 * The details for a single process instance are visualized with a {@link ProcessAnalysingModal}.
 * @author micha
 */
@SuppressWarnings("serial")
public class BPMNAnalysisPanel extends Panel {
	
	private AbstractSushiPage abstractSushiPage;
	private ArrayList<String> processNameList;
	private DropDownChoice<String> processSelect;
	private DefaultDataTable<ProcessMonitor, String> dataTable;
	private ProcessMonitoringProvider processMonitoringProvider;
	private ProcessMonitoringFilter processMonitoringFilter;
	private SushiProcess process;
	private ProcessAnalysingModal processMonitorModal;
	
	/**
	 * Constructor for a panel, which facilitates the analysis of finished {@link SushiProcessInstance}s.
	 * @param id
	 * @param abstractSushiPage
	 */
	public BPMNAnalysisPanel(String id, final AbstractSushiPage abstractSushiPage) {
		super(id);
		this.abstractSushiPage = abstractSushiPage;
		
		createProcessInstanceMonitoringProvider();
		
		buildMainLayout();
	}

	private void buildMainLayout() {
		addProcessInstanceMonitorModal();
		
		addProcessTable();
	}
	
	private void addProcessTable() {
		dataTable = new DefaultDataTable<ProcessMonitor, String>("processAnalysisTable", createColumns(), processMonitoringProvider, 20);
		dataTable.setOutputMarkupId(true);
		
		add(dataTable);
		
	}
	
	private void addProcessInstanceMonitorModal() {
        processMonitorModal = new ProcessAnalysingModal("processMonitorModal");
		add(processMonitorModal);
	}

	private List<? extends IColumn<ProcessMonitor, String>> createColumns() {
		ArrayList<IColumn<ProcessMonitor, String>> columns = new ArrayList<IColumn<ProcessMonitor, String>>();
		columns.add(new PropertyColumn<ProcessMonitor, String>(Model.of("ID"), "ID"));
		columns.add(new PropertyColumn<ProcessMonitor, String>(Model.of("Process"), "process"));
		columns.add(new PropertyColumn<ProcessMonitor, String>(Model.of("# of Process Instances"), "numberOfProcessInstances"));
		columns.add(new PropertyColumn<ProcessMonitor, String>(Model.of("Average runtime"), "averageRuntimeMillis"));
		
//		columns.add(new AbstractColumn(new Model("Status")) {
//			@Override
//			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
//				int entryId = ((ProcessInstanceMonitor) rowModel.getObject()).getID();
//				cellItem.add(new ProcessInstanceMonitoringStatusPanel(componentId, entryId, processInstanceMonitoringProvider));
//			}
//		});
		
		columns.add(new AbstractColumn(new Model("Status Details")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				int entryId = ((ProcessMonitor) rowModel.getObject()).getID();
				cellItem.add(new ProcessMonitorEntryDetailsPanel(componentId, entryId, processMonitoringProvider, processMonitorModal));
			}
		});
		
		return columns;
	}

	private void createProcessInstanceMonitoringProvider() {
		processMonitoringProvider = new ProcessMonitoringProvider();
		processMonitoringFilter = new ProcessMonitoringFilter();
		processMonitoringProvider.setProcessMonitorFilter(processMonitoringFilter);
	}
}
