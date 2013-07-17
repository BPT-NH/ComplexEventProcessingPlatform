package sushi.application.pages.querying.bpmn;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.tree.table.TreeColumn;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import sushi.application.components.form.BlockingAjaxButton;
import sushi.application.components.form.BlockingForm;
import sushi.application.components.tree.SushiLabelTreeTable;
import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.querying.bpmn.modal.BPMNQueryEditorHelpModal;
import sushi.application.pages.querying.bpmn.model.BPMNTreeTableElement;
import sushi.application.pages.querying.bpmn.model.BPMNTreeTableExpansionModel;
import sushi.application.pages.querying.bpmn.model.BPMNTreeTableProvider;
import sushi.application.pages.simulator.EmptyPanel;
import sushi.bpmn.decomposition.RPSTBuilder;
import sushi.bpmn.element.BPMNEventType;
import sushi.bpmn.element.BPMNIntermediateEvent;
import sushi.bpmn.element.BPMNProcess;
import sushi.bpmn.element.BPMNStartEvent;
import sushi.bpmn.element.BPMNTask;
import sushi.monitoring.bpmn.BPMNQueryMonitor;
import sushi.process.SushiProcess;
import sushi.query.SushiPatternQuery;
import sushi.query.bpmn.PatternQueryGenerator;

/**
 * This page facilitates the creation of {@link SushiPatternQuery}s from a {@link BPMNProcess}.
 * The user has to choose a {@link SushiProcess} on the page and can associate monitoring points 
 * to the BPMN process elements in the {@link MonitoringPointsPanel}.
 * @author micha
 */
public class BPMNQueryEditor extends AbstractSushiPage {

	private static final long serialVersionUID = -7896431319431474548L;
	private Form<Void> layoutForm;
	private ArrayList<String> bpmnProcessNameList;
	private DropDownChoice<String> bpmnProcessSelect;
	private BPMNProcess selectedBPMNProcess;
	private BlockingAjaxButton createQueriesButton;
	private BPMNQueryEditor page;
	private SushiLabelTreeTable<BPMNTreeTableElement, String> treeTable;
	private BPMNTreeTableProvider treeTableProvider;
	private BPMNQueryEditorHelpModal helpModal;

	/**
	 * Constructor for a page, which facilitates the creation of {@link SushiPatternQuery}s from a {@link BPMNProcess}.
	 */
	public BPMNQueryEditor() {
		super();
		this.page = this;
		this.treeTableProvider = new BPMNTreeTableProvider(selectedBPMNProcess);
		buildMainLayout();
	}
	
	private void buildMainLayout() {
		layoutForm = new BlockingForm("layoutForm");
		
		layoutForm.add(addBPMNProcessSelect());
		
		//Add componentTree
		createTreeTable();
		addCreateQueriesButton();
		addHelpModal();
		
		add(layoutForm);
	}
	
	private DropDownChoice<String> addBPMNProcessSelect(){
		bpmnProcessNameList = new ArrayList<String>();
		for (BPMNProcess bpmnProcess : BPMNProcess.findAll()) {
			bpmnProcessNameList.add(bpmnProcess.getName());
		}
		
		bpmnProcessSelect = new DropDownChoice<String>("bpmnProcessSelect", new Model<String>(), bpmnProcessNameList);
		bpmnProcessSelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;
			

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				String processValue = bpmnProcessSelect.getValue();
				if(processValue != null && !processValue.isEmpty()){
					List<BPMNProcess> processList = BPMNProcess.findByName(bpmnProcessSelect.getChoices().get(Integer.parseInt(bpmnProcessSelect.getValue())));
					if(processList.size() > 0){
						selectedBPMNProcess = processList.get(0);
						
						treeTableProvider.setProcess(selectedBPMNProcess);
						
						createTreeTable();
						target.add(treeTable);
					}
				}
			}
		});
		return bpmnProcessSelect;
	}
	
	private void addHelpModal() {
        helpModal = new BPMNQueryEditorHelpModal("helpModal");
		add(helpModal);

		layoutForm.add(new AjaxLink<Void>("showHelpModal"){

		private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target){
					helpModal.show(target);
				}
  		});
		
	}
	
	private void createTreeTable() {
		List<IColumn<BPMNTreeTableElement, String>> columns = createColumns();
		
		treeTable = new SushiLabelTreeTable<BPMNTreeTableElement, String>(
					"bpmnComponentTreeTable", 
					columns, 
					treeTableProvider, 
					Integer.MAX_VALUE, 
					new BPMNTreeTableExpansionModel());
		
		treeTable.setOutputMarkupId(true);

		treeTable.getTable().addTopToolbar(new HeadersToolbar<String>(treeTable.getTable(), treeTableProvider));
		
		layoutForm.addOrReplace(treeTable);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	private List<IColumn<BPMNTreeTableElement, String>> createColumns() {
		List<IColumn<BPMNTreeTableElement, String>> columns = new ArrayList<IColumn<BPMNTreeTableElement, String>>();
    
		columns.add(new TreeColumn<BPMNTreeTableElement, String>(Model.of("BPMN element"), "content"));
		
		columns.add(new AbstractColumn(new Model("Monitoring Points")) {
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				
				BPMNTreeTableElement treeTableElement = (BPMNTreeTableElement) rowModel.getObject();
				
				int entryId = treeTableElement.getID();
				boolean isTask = treeTableElement.getContent() instanceof BPMNTask;
				boolean isMonitorableEvent = 
						(treeTableElement.getContent() instanceof BPMNIntermediateEvent && !((BPMNIntermediateEvent)treeTableElement.getContent()).getIntermediateEventType().equals(BPMNEventType.Timer)) 
						|| (treeTableElement.getContent() instanceof BPMNStartEvent);
				
				if(isTask || isMonitorableEvent){
					MonitoringPointsPanel monitoringPointsPanel = new MonitoringPointsPanel(componentId, entryId, treeTableElement);
					cellItem.add(monitoringPointsPanel);
				}
				else{
					cellItem.add(new EmptyPanel(componentId, entryId, treeTableProvider));
				}
			}
		});
		
		//TODO: Add value selection possibility

		return columns;
	}
	
	
	
	private void addCreateQueriesButton() {
		createQueriesButton = new BlockingAjaxButton("createQueries", layoutForm) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				
				if(selectedBPMNProcess == null){
					page.getFeedbackPanel().error("Select a BPMN process!");
					target.add(page.getFeedbackPanel());
				} else {
					SushiProcess process = SushiProcess.findByBPMNProcess(selectedBPMNProcess);
					//Prüfen, ob Correlation besteht, sonst Warning
					if(!process.hasCorrelation()){
						page.getFeedbackPanel().error("No correlation exists for process!");
						target.add(page.getFeedbackPanel());
					} else {
						//BPMNProcess in RPST umwandeln
						RPSTBuilder rpst = new RPSTBuilder(selectedBPMNProcess);
						
						BPMNQueryMonitor.getInstance().getProcessMonitorForProcess(process).getProcess().setProcessDecompositionTree(rpst.getProcessDecompositionTree());
						
						//Queries erzeugen und bei Esper registrieren
						//RPST in Queries umwandeln
						try{
							PatternQueryGenerator queryGenerator = new PatternQueryGenerator(rpst);
							
							queryGenerator.generateQueries();
							
							page.getFeedbackPanel().success("Queries created!");
							target.add(page.getFeedbackPanel());
						} catch(Exception e){
							page.getFeedbackPanel().error("An error occured during query creation!");
							target.add(page.getFeedbackPanel());
						}
						
					}
				}
			}
		};
		layoutForm.add(createQueriesButton);
	}
}
