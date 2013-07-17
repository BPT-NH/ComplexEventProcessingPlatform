package sushi.application.pages.monitoring.bpmn.monitoring.modal;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.table.TreeColumn;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import sushi.application.components.form.BootstrapModal;
import sushi.application.components.tree.SushiLabelTreeTable;
import sushi.application.pages.monitoring.bpmn.monitoring.QueryMonitoringStatusPanel;
import sushi.application.pages.monitoring.bpmn.monitoring.QueryViolationMonitoringStatusPanel;
import sushi.application.pages.monitoring.bpmn.monitoring.model.ProcessInstanceMonitoringTreeTableElement;
import sushi.application.pages.monitoring.bpmn.monitoring.model.ProcessInstanceMonitoringTreeTableExpansionModel;
import sushi.application.pages.monitoring.bpmn.monitoring.model.ProcessInstanceMonitoringTreeTableProvider;
import sushi.monitoring.bpmn.ProcessInstanceMonitor;
import sushi.process.SushiProcess;

/**
 * This is a modal for displaying the monitoring status for a {@link SushiProcess}.
 * @author micha
 */
public class ProcessInstanceMonitoringModal extends BootstrapModal {

	private static final long serialVersionUID = 1L;
	private static final ResourceReference MODAL_SIZE_CSS = new PackageResourceReference(BootstrapModal.class, "modal_size.css");
	private ProcessInstanceMonitor processInstanceMonitor;
	private Form<Void> layoutForm;
	private SushiLabelTreeTable<ProcessInstanceMonitoringTreeTableElement, String> treeTable;
	private ProcessInstanceMonitoringTreeTableProvider treeTableProvider = new ProcessInstanceMonitoringTreeTableProvider(processInstanceMonitor);
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(MODAL_SIZE_CSS));
	}
	
	/**
     * Constructor for a modal, which displays the monitoring status for a {@link SushiProcess}.
     * @param id
     */
    public ProcessInstanceMonitoringModal(String id) {
    	super(id, "Process Instance Monitoring");
    	buildMainLayout();
	}
	
	private void buildMainLayout() {
		layoutForm = new Form<Void>("layoutForm");
		
		//Add componentTree
		createTreeTable();
		
		add(layoutForm);
	}
	
	private void createTreeTable() {
		List<IColumn<ProcessInstanceMonitoringTreeTableElement, String>> columns = createColumns();
		
		treeTable = new SushiLabelTreeTable<ProcessInstanceMonitoringTreeTableElement, String>(
					"processInstanceMonitoringTreeTable", 
					columns, 
					treeTableProvider, 
					Integer.MAX_VALUE, 
					new ProcessInstanceMonitoringTreeTableExpansionModel());
		
		treeTable.setOutputMarkupId(true);

		treeTable.getTable().addTopToolbar(new HeadersToolbar<String>(treeTable.getTable(), treeTableProvider));
		
		ProcessInstanceMonitoringTreeTableExpansionModel.get().expandAll();
		
		layoutForm.addOrReplace(treeTable);
	}
	
	private List<IColumn<ProcessInstanceMonitoringTreeTableElement, String>> createColumns() {
		List<IColumn<ProcessInstanceMonitoringTreeTableElement, String>> columns = new ArrayList<IColumn<ProcessInstanceMonitoringTreeTableElement, String>>();
    
		columns.add(new TreeColumn<ProcessInstanceMonitoringTreeTableElement, String>(Model.of("Query"), "query"));
		columns.add(new PropertyColumn<ProcessInstanceMonitoringTreeTableElement, String>(Model.of("Monitored Elements"), "monitoredElements"));
		
		columns.add(new AbstractColumn(new Model("Status")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				int entryId = ((ProcessInstanceMonitoringTreeTableElement) rowModel.getObject()).getID();
				cellItem.add(new QueryMonitoringStatusPanel(componentId, entryId, treeTableProvider));
			}
		});
		
		columns.add(new AbstractColumn(new Model("Violation Status")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				int entryId = ((ProcessInstanceMonitoringTreeTableElement) rowModel.getObject()).getID();
				cellItem.add(new QueryViolationMonitoringStatusPanel(componentId, entryId, treeTableProvider));
			}
		});
		
		columns.add(new PropertyColumn<ProcessInstanceMonitoringTreeTableElement, String>(Model.of("Start Time"), "startTime"));
		columns.add(new PropertyColumn<ProcessInstanceMonitoringTreeTableElement, String>(Model.of("End Time"), "endTime"));
		
		return columns;
	}

	public void setProcessInstanceMonitor(ProcessInstanceMonitor processInstanceMonitor, AjaxRequestTarget target) {
		this.processInstanceMonitor = processInstanceMonitor;
		this.treeTableProvider.setProcessInstanceMonitor(processInstanceMonitor);
		refreshTreeTable(target);
	}
	
	public void refreshTreeTable(AjaxRequestTarget target){
		createTreeTable();
		target.add(treeTable);
	}
}
