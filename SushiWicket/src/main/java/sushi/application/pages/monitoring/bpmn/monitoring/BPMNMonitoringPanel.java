package sushi.application.pages.monitoring.bpmn.monitoring;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.monitoring.bpmn.monitoring.modal.ProcessInstanceMonitoringModal;
import sushi.application.pages.monitoring.bpmn.monitoring.model.ProcessInstanceMonitoringFilter;
import sushi.application.pages.monitoring.bpmn.monitoring.model.ProcessInstanceMonitoringProvider;
import sushi.monitoring.bpmn.ProcessInstanceMonitor;
import sushi.process.SushiProcess;
import sushi.process.SushiProcessInstance;

/**
 * This panel facilitates the monitoring of running {@link SushiProcessInstance}s.
 * The details for a single process instance are visualized with a {@link ProcessInstanceMonitoringStatusPanel}.
 * @author micha
 */
@SuppressWarnings("serial")
public class BPMNMonitoringPanel extends Panel {
	
	private AbstractSushiPage abstractSushiPage;
	private ArrayList<String> processNameList;
	private DropDownChoice<String> processSelect;
	private DefaultDataTable<ProcessInstanceMonitor, String> dataTable;
	private ProcessInstanceMonitoringProvider processInstanceMonitoringProvider;
	private ProcessInstanceMonitoringFilter processInstanceMonitoringFilter;
	private SushiProcess process;
	private ProcessInstanceMonitoringModal processInstanceMonitorModal;
	
	/**
	 * Constructor for a panel, which facilitates the monitoring of running {@link SushiProcessInstance}s.
	 * @param id
	 * @param abstractSushiPage
	 */
	public BPMNMonitoringPanel(String id, final AbstractSushiPage abstractSushiPage) {
		super(id);
		this.abstractSushiPage = abstractSushiPage;
		
		createProcessInstanceMonitoringProvider();
		
		buildMainLayout();
	}

	private void buildMainLayout() {
		Form<Void> layoutForm = new Form<Void>("layoutForm");
		add(layoutForm);
		addProcessSelect(layoutForm);
		
		addProcessInstanceMonitorModal(layoutForm);
		
		addProcessInstanceTable(layoutForm);
	}
	
	private void addProcessInstanceTable(Form<Void> layoutForm) {
		dataTable = new DefaultDataTable<ProcessInstanceMonitor, String>("processInstancesMonitoringTable", createColumns(), processInstanceMonitoringProvider, 20);
		dataTable.setOutputMarkupId(true);
		
		add(dataTable);
		
	}
	
	private void addProcessInstanceMonitorModal(Form<Void> layoutForm) {
        processInstanceMonitorModal = new ProcessInstanceMonitoringModal("processInstanceMonitorModal");
		add(processInstanceMonitorModal);
	}

	private List<? extends IColumn<ProcessInstanceMonitor, String>> createColumns() {
		ArrayList<IColumn<ProcessInstanceMonitor, String>> columns = new ArrayList<IColumn<ProcessInstanceMonitor, String>>();
		columns.add(new PropertyColumn<ProcessInstanceMonitor, String>(Model.of("ID"), "ID"));
		columns.add(new PropertyColumn<ProcessInstanceMonitor, String>(Model.of("ProcessInstance"), "processInstance"));
//		columns.add(new PropertyColumn<ProcessInstanceMonitor, String>(Model.of("Status"), "status"));
		
		columns.add(new AbstractColumn(new Model("Status")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				int entryId = ((ProcessInstanceMonitor) rowModel.getObject()).getID();
				cellItem.add(new ProcessInstanceMonitoringStatusPanel(componentId, entryId, processInstanceMonitoringProvider));
			}
		});
		
		columns.add(new AbstractColumn(new Model("Status Details")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				int entryId = ((ProcessInstanceMonitor) rowModel.getObject()).getID();
				cellItem.add(new ProcessInstanceMonitorEntryDetailsPanel(componentId, entryId, processInstanceMonitoringProvider, processInstanceMonitorModal));
			}
		});
		
		return columns;
	}

	private void addProcessSelect(Form<Void> layoutForm) {
		processNameList = new ArrayList<String>();
		for (SushiProcess process : SushiProcess.findAll()) {
			processNameList.add(process.getName());
		}
		
		processSelect = new DropDownChoice<String>("processSelect", new Model<String>(), processNameList);
		processSelect.setOutputMarkupId(true);
		processSelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				process = SushiProcess.findByName(processSelect.getChoices().get(Integer.parseInt(processSelect.getValue()))).get(0);
				createProcessInstanceMonitoringProvider();
				target.add(dataTable);
			}

			
		});
		
		layoutForm.add(processSelect);
	}
	
	private void createProcessInstanceMonitoringProvider() {
		processInstanceMonitoringProvider = new ProcessInstanceMonitoringProvider(process);
		processInstanceMonitoringFilter = new ProcessInstanceMonitoringFilter();
		processInstanceMonitoringProvider.setProcessInstanceMonitorFilter(processInstanceMonitoringFilter);
	}
}
