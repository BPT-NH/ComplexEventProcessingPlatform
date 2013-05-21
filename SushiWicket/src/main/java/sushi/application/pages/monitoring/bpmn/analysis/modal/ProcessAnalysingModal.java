package sushi.application.pages.monitoring.bpmn.analysis.modal;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.table.TreeColumn;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import sushi.application.components.form.BootstrapModal;
import sushi.application.components.tree.SushiLabelTreeTable;
import sushi.application.pages.monitoring.bpmn.analysis.modal.model.ProcessAnalysingTreeTableElement;
import sushi.application.pages.monitoring.bpmn.analysis.modal.model.ProcessAnalysingTreeTableExpansionModel;
import sushi.application.pages.monitoring.bpmn.analysis.modal.model.ProcessAnalysingTreeTableProvider;
import sushi.application.pages.monitoring.bpmn.monitoring.model.ProcessInstanceMonitoringTreeTableExpansionModel;
import sushi.monitoring.bpmn.ProcessMonitor;

public class ProcessAnalysingModal extends BootstrapModal {

	private static final long serialVersionUID = 1L;
	private static final ResourceReference MODAL_SIZE_CSS = new PackageResourceReference(BootstrapModal.class, "modal_size.css");
	private ProcessMonitor processMonitor;
	private SushiLabelTreeTable<ProcessAnalysingTreeTableElement, String> treeTable;
	private Form<Void> layoutForm;
	private ProcessAnalysingTreeTableProvider treeTableProvider = new ProcessAnalysingTreeTableProvider(processMonitor);
	private Label processNameLabel;
	private String processName;
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(MODAL_SIZE_CSS));
	}
	
    public ProcessAnalysingModal(String id) {
    	super(id, "Process Analysis");
    	buildMainLayout();
	}
	
	private void buildMainLayout() {
		layoutForm = new Form<Void>("layoutForm");
		processNameLabel = new Label("processName", new PropertyModel<String>(this, "processName"));
//		processNameLabel = new Label("processName", Model.of(""));
		processNameLabel.setOutputMarkupId(true);
		layoutForm.add(processNameLabel);
		
		createTreeTable();
		add(layoutForm);
	}
	
	private void createTreeTable() {
		List<IColumn<ProcessAnalysingTreeTableElement, String>> columns = createColumns();
		
		treeTable = new SushiLabelTreeTable<ProcessAnalysingTreeTableElement, String>(
					"processAnalysisTreeTable", 
					columns, 
					treeTableProvider, 
					Integer.MAX_VALUE, 
					new ProcessAnalysingTreeTableExpansionModel());
		
		treeTable.setOutputMarkupId(true);

		treeTable.getTable().addTopToolbar(new HeadersToolbar<String>(treeTable.getTable(), treeTableProvider));
		
		ProcessInstanceMonitoringTreeTableExpansionModel.get().expandAll();
		
		layoutForm.addOrReplace(treeTable);
	}
	
	private List<IColumn<ProcessAnalysingTreeTableElement, String>> createColumns() {
		List<IColumn<ProcessAnalysingTreeTableElement, String>> columns = new ArrayList<IColumn<ProcessAnalysingTreeTableElement, String>>();
    
		columns.add(new TreeColumn<ProcessAnalysingTreeTableElement, String>(Model.of("Query"), "query"));
		columns.add(new PropertyColumn<ProcessAnalysingTreeTableElement, String>(Model.of("Monitored Elements"), "monitoredElements"));
		
		columns.add(new PropertyColumn<ProcessAnalysingTreeTableElement, String>(Model.of("Average Runtime"), "averageRuntime"));
		columns.add(new PropertyColumn<ProcessAnalysingTreeTableElement, String>(Model.of("Path frequency"), "pathFrequency"));
		
		return columns;
	}

	public void setProcessMonitor(ProcessMonitor processMonitor, AjaxRequestTarget target) {
		this.processMonitor = processMonitor;
		this.treeTableProvider.setProcessMonitor(processMonitor);
		refreshTreeTable(target);
		refreshLabel(processMonitor, target);
	}
	
	private void refreshLabel(ProcessMonitor processMonitor, AjaxRequestTarget target) {
		processName = (processMonitor != null) ? processMonitor.getProcess().getName() : "";
		target.add(processNameLabel);
	}

	public void refreshTreeTable(AjaxRequestTarget target){
		createTreeTable();
		target.add(treeTable);
	}
	
}
