package sushi.application.pages.eventrepository.processeditor;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

import sushi.application.components.form.WarnOnExitForm;
import sushi.application.pages.AbstractSushiPage;
import sushi.process.SushiProcess;

/**
 * This class is page to create and delete {@link SushiProcess}es from the database.
 * @author micha
 */
public class ProcessEditor extends AbstractSushiPage {
	
	private static final long serialVersionUID = 1L;
	private ListChoice<String> existingProcessesList;
	private List<String> processNames = new ArrayList<String>();
	private static String selectedProcessName = new String();
	
	/**
	 * Constructor for a page to create and delete {@link SushiProcess}es from the database.
	 */
	public ProcessEditor(){
		super();
		Form<Void> processEditForm = new WarnOnExitForm("processEditForm");
		add(processEditForm);
    	
    	//Input für neuen Prozess
		final TextField<String> processNameInput = new TextField<String>("processNameInput", Model.of(""));
    	processNameInput.setOutputMarkupId(true);
    	processEditForm.add(processNameInput);
		
    	//Plus-Button zum Speichern
    	AjaxButton addProcessButton = new AjaxButton("addProcessButton", processEditForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				String processName = processNameInput.getValue();
				SushiProcess process = new SushiProcess(processName);
				process.save();
				processNames.add(processName);
				target.add(existingProcessesList);
	        }
	    };
	    processEditForm.add(addProcessButton);
	    
	    //ListChoice für bestehende Prozesse
	    for(SushiProcess process : SushiProcess.findAll()){
	    	processNames.add(process.getName());
	    }
	    existingProcessesList = new ListChoice<String>("existingProcessSelect", new Model(selectedProcessName), processNames);
	    existingProcessesList.setOutputMarkupId(true);
		
	    existingProcessesList.add(new AjaxFormComponentUpdatingBehavior("onchange"){ 
			@Override 
			protected void onUpdate(AjaxRequestTarget target) { 
			}
		});
	    processEditForm.add(existingProcessesList);
	    
    	//Minus-Button zum Löschen bestehender Prozesse
	    AjaxButton removeProcessButton = new AjaxButton("removeProcessButton", processEditForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onSubmit(AjaxRequestTarget target, Form form) {
				SushiProcess selectedProcess = getSelectedProcess();
				if(selectedProcess != null){
					String processName = selectedProcess.getName();
					processNames.remove(processName);
					selectedProcess.remove();
				}
				target.add(existingProcessesList);
	        }

	    };
	    processEditForm.add(removeProcessButton);
    }
    
    private SushiProcess getSelectedProcess() {
    	try{
    		int processListIndex = Integer.parseInt(existingProcessesList.getValue());
    		String processName = existingProcessesList.getChoices().get(processListIndex);
    		return SushiProcess.findByName(processName).get(0);
    	}
		catch(Exception e){
			return null;
		}
	}
}
