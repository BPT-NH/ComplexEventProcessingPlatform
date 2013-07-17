package sushi.application.pages.process.modal;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

import sushi.application.components.form.BootstrapModal;
import sushi.application.components.form.WarnOnExitForm;
import sushi.process.SushiProcess;

/**
 * This panel is a {@link BootstrapModal} and allows the creation and deletion of {@link SushiProcess}es.
 * @author micha
 */
public class ProcessEditorModal extends BootstrapModal {

	private static final long serialVersionUID = 1L;
	private ListChoice<String> existingProcessesList;
	private List<String> processNames = new ArrayList<String>();
	private DropDownChoice<String> processSelect;
	private static String selectedProcessName = new String();
	
	/**
     * Constructor for a panel, which is a {@link BootstrapModal} and allows the creation and deletion of {@link SushiProcess}es.
     * @param processSelect
     * @param window
     */
    public ProcessEditorModal(String id, final DropDownChoice<String> processSelect) {
    	super(id, "Process Editor");
    	this.processSelect = processSelect;
    	Form<Void> processEditForm = new WarnOnExitForm("processEditForm");
		add(processEditForm);
    	
    	// Input für neuen Prozess
		final TextField<String> processNameInput = new TextField<String>("processNameInput", Model.of(""));
    	processNameInput.setOutputMarkupId(true);
    	processEditForm.add(processNameInput);
		
    	// Button zum Speichern
    	AjaxButton addProcessButton = new AjaxButton("addProcessButton", processEditForm) {
			private static final long serialVersionUID = -8422505767509635904L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				String processName = processNameInput.getValue();
				if (!processName.isEmpty() && !SushiProcess.exists(processName)) {
					SushiProcess process = new SushiProcess(processName);
					process.save();
					processNames.add(processName);
					target.add(existingProcessesList);
					processSelect.setChoices(processNames);
					target.add(processSelect);
				}
	        }
	    };
	    processEditForm.add(addProcessButton);
	    
	    // ListChoice für bestehende Prozesse
	    for (SushiProcess process : SushiProcess.findAll()) {
	    	processNames.add(process.getName());
	    }
	    existingProcessesList = new ListChoice<String>("existingProcessSelect", new Model<String>(selectedProcessName), processNames);
	    existingProcessesList.setOutputMarkupId(true);
	    processEditForm.add(existingProcessesList);
	    
    	// Button zum Löschen bestehender Prozesse
	    AjaxButton removeProcessButton = new AjaxButton("removeProcessButton", processEditForm) {
			private static final long serialVersionUID = 3874692865572427214L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				SushiProcess selectedProcess = getSelectedProcess();
				if (selectedProcess != null) {
					String processName = selectedProcess.getName();
					processNames.remove(processName);
					selectedProcess.remove();
				}
				target.add(existingProcessesList);
				
				processSelect.setChoices(processNames);
        		target.add(processSelect);
	        }
	    };
	    processEditForm.add(removeProcessButton);
    }
    
    private SushiProcess getSelectedProcess() {
    	try {
    		int processListIndex = Integer.parseInt(existingProcessesList.getValue());
    		String processName = existingProcessesList.getChoices().get(processListIndex);
    		return SushiProcess.findByName(processName).get(0);
    	}
		catch (Exception e) {
			return null;
		}
	}
}
