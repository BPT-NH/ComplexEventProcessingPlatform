package sushi.application.pages.input.bpmn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import sushi.application.components.form.ExternalPage;
import sushi.application.components.form.WarnOnExitForm;
import sushi.application.pages.AbstractSushiPage;
import sushi.application.pages.input.bpmn.model.ProcessModelProvider;
import sushi.application.pages.process.modal.ProcessEditorModal;
import sushi.application.pages.simulator.BPMNSimulationPanel;
import sushi.application.pages.simulator.SimulationPanel;
import sushi.bpmn.element.AbstractBPMNElement;
import sushi.bpmn.element.BPMNProcess;
import sushi.process.SushiProcess;
import sushi.xml.importer.BPM2XMLToSignavioXMLConverter;
import sushi.xml.importer.BPMNParser;

/**
 * This panel allows the upload and visualisation of a BPMN process model from a BPMN2.0-XML file.
 * Furthermore it is possible to simulate this process with the {@link SimulationPanel}.
 * @author micha
 * @author benni
 */
public class BPMNProcessUploadPanel extends Panel{

	private static final long serialVersionUID = 1L;
	private FileUploadField fileUpload;
	private ProcessEditorModal processEditorModal;
	private ArrayList<String> processNameList;
	private BPMNProcess processModel;
	private DropDownChoice<String> processSelect;
	private WarnOnExitForm uploadForm;
	private Button uploadButton;
	private AjaxButton saveModelButton;
	private AjaxButton deleteModelButton;
	private SushiProcess process;
	private AjaxButton cancelButton;
	private AjaxButton saveChangesButton;
	private TextField<String> bpmnProcessNameInput;
	private String fileNameWithoutExtension;
	private String bpmnProcessNameInputValue;
	private AbstractSushiPage abstractSushiPage;
	private ArrayList<IColumn<AbstractBPMNElement, String>> columns;
	private DefaultDataTable<AbstractBPMNElement, String> processModelTable;
	private ProcessModelProvider processModelProvider;
	private BPMNProcessUploadPanel bpmnProcessUploadPanel;
	private ExternalPage externalPage;
	private BPMNSimulationPanel simulationPanel;
	private final String pathToCoreComponents = "http://localhost:8080/signaviocore/p/editor?id=c%3A%3Btemp%3B";
	private final String signavioCoreWorkspace = "~/signaviocore-workspace";
	private List<Component> targets;
	
	/**
	 * This is the constructor for a panel, which allows the upload and visualisation of a BPMN process model from a BPMN2.0-XML file.
	 * Furthermore it is possible to simulate this process with the {@link SimulationPanel}.
	 * @param id
	 * @param abstractSushiPage
	 */
	@SuppressWarnings("unchecked")
	public BPMNProcessUploadPanel(String id, final AbstractSushiPage abstractSushiPage) {
		super(id);
		
		this.abstractSushiPage = abstractSushiPage;
		this.bpmnProcessUploadPanel = this;
		this.processModelProvider = new ProcessModelProvider();
		
		Form<Void> layoutForm = new Form<Void>("layoutForm");
		add(layoutForm);
		
		
		addProcessSelect(layoutForm);
		
		addUploadForm();
        
        createProcessEditModalWindow(layoutForm);
        
        addResultForm();
        //TODO:
		externalPage = new ExternalPage("iframe", "http://localhost:8181/signaviocore/p/explorer");
		externalPage.setOutputMarkupId(true);
		add(externalPage);
		
		createTargetList(abstractSushiPage);
	}

	private void createTargetList(final AbstractSushiPage abstractSushiPage) {
		targets = new ArrayList<Component>();
		targets.add(deleteModelButton);
		targets.add(saveChangesButton);
		targets.add(saveModelButton);
		targets.add(cancelButton);
		targets.add(abstractSushiPage.getFeedbackPanel());
		targets.add(processModelTable);
		targets.add(bpmnProcessNameInput);
		targets.add(uploadButton);
		targets.add(fileUpload);
	}

	private void addResultForm() {
		Form<Void> resultForm = new Form<Void>("resultForm");
		add(resultForm);
    	resultForm.add(addProcessModelTable());
    	
		
		saveModelButton = (new AjaxButton("saveModel"){
			
			private static final long serialVersionUID = 1L;

			@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form){
            	if(process.getBpmnProcess() != null){
            		process.getBpmnProcess().remove();
            	}
				if(bpmnProcessNameInputValue == null || bpmnProcessNameInputValue.isEmpty()){
					bpmnProcessNameInputValue = fileNameWithoutExtension;
				}
				if(!containsOtherProcessSameBPMNProcessName(process, bpmnProcessNameInputValue)){
					processModel.setName(bpmnProcessNameInputValue);
	            	process.setBpmnProcess(processModel);
	            	processModel.save();
	            	process.save();
	            	saveModelButton.setVisible(false);
	            	cancelButton.setVisible(false);
	            	saveChangesButton.setVisible(true);
	            	deleteModelButton.setVisible(true);
	            	abstractSushiPage.getFeedbackPanel().success("Saved process!");
				} else {
					abstractSushiPage.getFeedbackPanel().error("Another process has the same BPMN process name!");
				}
				addTargets(target);
            }

        });
		saveModelButton.setVisible(false);
		resultForm.add(saveModelButton);
		saveModelButton.setOutputMarkupId(true);
		saveModelButton.setOutputMarkupPlaceholderTag(true);
		
		cancelButton = (new AjaxButton("cancel"){

			private static final long serialVersionUID = 1L;

			@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            	processModel = process.getBpmnProcess();
            	processModelProvider.setProcessModel(processModel);
            	if (processModel != null){
            		deleteModelButton.setVisible(true);
            		saveChangesButton.setVisible(true);
            	}
            	else{
            		deleteModelButton.setVisible(false);
            		saveChangesButton.setVisible(false);
            	}
            	cancelButton.setVisible(false);
            	saveModelButton.setVisible(false);
            	addTargets(target);
            }
        });
		cancelButton.setVisible(false);
		resultForm.add(cancelButton);
		cancelButton.setOutputMarkupId(true);
		cancelButton.setOutputMarkupPlaceholderTag(true);
		
		saveChangesButton = (new AjaxButton("saveChanges"){

			private static final long serialVersionUID = 1L;

			@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form){
				if(bpmnProcessNameInputValue == null || bpmnProcessNameInputValue.isEmpty()){
					bpmnProcessNameInputValue = fileNameWithoutExtension;
				}
				processModel.setName(bpmnProcessNameInputValue);
				//TODO: geändertes Modell einlesen
				
            	processModel.save();
            	process.save();
            }
        });
		saveChangesButton.setVisible(false);
		saveChangesButton.setEnabled(false);
		resultForm.add(saveChangesButton);
		saveChangesButton.setOutputMarkupId(true);
		saveChangesButton.setOutputMarkupPlaceholderTag(true);
		
		deleteModelButton = (new AjaxButton("deleteModel"){
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form){
            	if(process.getBpmnProcess() != null){
            		process.getBpmnProcess().remove();
            	}
            	
            	abstractSushiPage.getFeedbackPanel().error("No process model exists!");
            	processModelProvider.setProcessModel(null);
            	deleteModelButton.setVisible(false);
            	saveChangesButton.setVisible(false);            	
            	
            	addTargets(target);
            }
        });
		deleteModelButton.setVisible(false);
		resultForm.add(deleteModelButton);
		deleteModelButton.setOutputMarkupId(true);
		deleteModelButton.setOutputMarkupPlaceholderTag(true);
	}

	private void addUploadForm() {
		uploadForm = new WarnOnExitForm("uploadForm"){
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				final FileUpload uploadedFile = fileUpload.getFileUpload();
				if (uploadedFile != null) {
					String uploadFolder;
					
					if (System.getProperty("os.name").contains("Windows")) {
						uploadFolder = "C:\\temp\\";
					}
					else {
						File _uploadFolder = new File(signavioCoreWorkspace);
						_uploadFolder.mkdirs();
						try {
							uploadFolder = _uploadFolder.getCanonicalPath();
						} catch (IOException e) {
							uploadFolder = "/tmp/";
							e.printStackTrace();
						}
					}
					
					String fileName = uploadedFile.getClientFileName();
					File newFile = new File(uploadFolder + fileName);
					
					if (newFile.exists()) {
						newFile.delete();
					}
			
					try {
						newFile.createNewFile();
						uploadedFile.writeTo(newFile);
//							info("Saved file: " + fileName);
					} catch (IOException e) {
						throw new IllegalStateException("Error: File could not be saved.");
					}
					
					int index = fileName.lastIndexOf('.');
					fileNameWithoutExtension = fileName.substring(0,index);
					String fileExtension = fileName.substring(index + 1, fileName.length());
					if(fileExtension.toLowerCase().contains("xml") || fileExtension.toLowerCase().contains("bpmn")){
						processModel = BPMNParser.generateProcessFromXML(newFile.getAbsolutePath());
						BPM2XMLToSignavioXMLConverter signavioConverter = new BPM2XMLToSignavioXMLConverter(newFile.getAbsolutePath());
//						String newFileName = signavioConverter.generateSignavioXMLFromBPM2XML();
						processModelProvider.setProcessModel(processModel);
						cancelButton.setVisible(true);
						saveModelButton.setVisible(true);
	            		deleteModelButton.setVisible(false);
	            		saveChangesButton.setVisible(false);
//	    				externalPage.setURL(pathToCoreComponents + newFileName);
						
					} else {
						System.out.println("no xml");
					}
					uploadButton.setEnabled(false);
				} else {
					error("File not found");
				}

				if(simulationPanel != null){
					simulationPanel.updateMonitoringPoints(null);
				}
			}
		};
		
		uploadForm.add(fileUpload = new FileUploadField("fileUpload"));
		uploadForm.setMultiPart(true);
		fileUpload.setOutputMarkupId(true);
		fileUpload.setEnabled(false);
		add(uploadForm);

		
		uploadButton = new Button("upload");
		uploadButton.setEnabled(false);
		uploadButton.setOutputMarkupId(true);
		uploadForm.add(uploadButton);
		
		bpmnProcessNameInput = new TextField<String>("bpmnProcessNameInput",  new PropertyModel(this,"bpmnProcessNameInputValue"));
		bpmnProcessNameInput.setOutputMarkupId(true);
		
		bpmnProcessNameInput.add(new OnChangeAjaxBehavior(){

			private static final long serialVersionUID = 1L;

			@Override
	        protected void onUpdate(final AjaxRequestTarget target){
	        	bpmnProcessNameInputValue = ((TextField<String>) getComponent()).getModelObject();
	        }
	    });
		
		uploadForm.add(bpmnProcessNameInput);
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
				fileUpload.setEnabled(true);
				uploadButton.setEnabled(true);
            	process = SushiProcess.findByName(processSelect.getChoices().get(Integer.parseInt(processSelect.getValue()))).get(0);
            	processModel = process.getBpmnProcess();
            	processModelProvider.setProcessModel(processModel);
            	
            	if (processModel != null){
            		deleteModelButton.setVisible(true);
            		saveChangesButton.setVisible(true);
            		bpmnProcessNameInputValue = processModel.getName();
    				if(simulationPanel != null){
    					simulationPanel.updateMonitoringPoints(target);
    				}
            	}
            	addTargets(target);
			}
		});
		
		layoutForm.add(processSelect);
	}

	@SuppressWarnings("unchecked")
	private Component addProcessModelTable() {
		columns = new ArrayList<IColumn<AbstractBPMNElement, String>>();
		columns.add(new AbstractColumn<AbstractBPMNElement, String>(new Model("Element")) {
			
			@Override
			public void populateItem(Item cellItem, String componentId, IModel rowModel) {
				String elementName = ((AbstractBPMNElement) rowModel.getObject()).toString();
				cellItem.add(new Label(componentId, elementName));
			}
		});
		columns.add(new PropertyColumn<AbstractBPMNElement, String>(Model.of("Predecessors"), "predecessors"));
		columns.add(new PropertyColumn<AbstractBPMNElement, String>(Model.of("Successors"), "successors"));
		columns.add(new PropertyColumn<AbstractBPMNElement, String>(Model.of("Monitoring Points"), "monitoringPoints"));

		processModelTable = new DefaultDataTable<AbstractBPMNElement, String>("processModelElements", columns, processModelProvider, 40);
		processModelTable.setOutputMarkupId(true);
		processModelTable.setOutputMarkupPlaceholderTag(true);
		
		return processModelTable;
	}

	private void createProcessEditModalWindow(Form<Void> layoutForm){
		processEditorModal = new ProcessEditorModal("processEditorModal", processSelect);
		add(processEditorModal);

		layoutForm.add(new AjaxLink<Void>("showProcessEditModal"){

		private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target){
					processEditorModal.show(target);
				}
  		});
	}

	public String getSelectedProcessName() {
		return processSelect.getModelObject();
	}
	
	public SushiProcess getProcess() {
		return process;
	}

	public BPMNProcess getProcessModel() {
		return processModel;
	}

	public void setSimulationPanel(BPMNSimulationPanel simulationPanel){
		this.simulationPanel = simulationPanel;
		targets.add(simulationPanel.getMonitoringPointTable());
	}
	
	private void addTargets(AjaxRequestTarget target) {
		for(Component targetComponent : targets){
			target.add(targetComponent);
		}
	}
	
	/**
	 * Searches for BPMN processes with the same, but a different containing process.
	 * @param process
	 * @param bpmnProcessName
	 * @return
	 */
	private boolean containsOtherProcessSameBPMNProcessName(SushiProcess process, String bpmnProcessName) {
		List<BPMNProcess> bpmnProcesses = BPMNProcess.findByName(bpmnProcessName);
		for(BPMNProcess bpmnProcess : bpmnProcesses){
			if(!SushiProcess.findByBPMNProcess(bpmnProcess).equals(process)){
				return true;
			}
		}
		return false;
	}
}
